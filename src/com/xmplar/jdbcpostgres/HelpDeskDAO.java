package com.xmplar.jdbcpostgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpDeskDAO {

    private static PreparedStatement pstmt;
    private static Statement stmt;
    private static ResultSet rs;
    private static Scanner sc = new Scanner(System.in);

    public static void createTicket(int ticketID, String description, String status) throws SQLException {
        try {
            Pattern pattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            Matcher matcher = pattern.matcher(description);
            if (ticketID < 1
                    || Character.isWhitespace(description.charAt(0)) || matcher.find() || description.length() > 20) {
                System.out.println("Invalid Ticket");
            } else {
                String createTicketQuery = "INSERT INTO \"HelpDesk\".\"Ticket\" values(?,?,?)";
                pstmt = JdbcConnection.con.prepareStatement(createTicketQuery);
                pstmt.setInt(1, ticketID);
                pstmt.setString(2, description);
                pstmt.setString(3, status);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Ticket already exists. Enter a new ticket!");
        }
    }

    public static void createUser(int userID, String username) throws SQLException {
        try {
            Pattern pattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            Matcher matcher = pattern.matcher(username);
            if (userID < 1
                    || Character.isDigit(username.charAt(0)) || Character.isWhitespace(username.charAt(0))
                    || matcher.find() || username.length() > 15) {
                System.out.println("Invalid User");
            } else {
                String createUserQuery = "INSERT INTO \"HelpDesk\".\"User\" values(?,?)";
                pstmt = JdbcConnection.con.prepareStatement(createUserQuery);
                pstmt.setInt(1, userID);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
                System.out.println("User added successfully");

            }
        } catch (SQLException e) {
            System.out.println("User already exists. Enter a new user!");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("User name not entered");
        }
    }

    public static void assignTickets() throws SQLException {
        try {
            if (displayUnassignedTickets() == false) {
                return;
            } else if (displayUsers() == false) {
                return;
            } else {
                System.out.println("Enter number of tickets to be assigned:");
                int numberOfTicketsToAssign = sc.nextInt();
                if (numberOfTicketsToAssign > 1) {
                    for (int i = 0; i < numberOfTicketsToAssign; i++) {
                        System.out.println("Enter Ticket ID:");
                        int ticketID = sc.nextInt();
                        if (checkForTicketID(ticketID) == false) {
                            System.out.println("Ticket ID does not exist");
                        } else {
                            System.out.println("Enter User ID:");
                            int userID = sc.nextInt();
                            if (checkForUserID(userID) == false) {
                                System.out.println("User ID does not exist");
                            } else {
                                String assignTicketQuery = "INSERT INTO \"HelpDesk\".\"AssignedTickets\" (\"TicketID\", \"UserID\") VALUES(?,?)";
                                pstmt = JdbcConnection.con.prepareStatement(assignTicketQuery);
                                pstmt.setInt(1, ticketID);
                                pstmt.setInt(2, userID);
                                pstmt.executeUpdate();

                                String updateTicketStatusQuery = "UPDATE \"HelpDesk\".\"Ticket\" SET \"Status\" = 'Fixed' WHERE \"TicketID\" = ?";
                                pstmt = JdbcConnection.con.prepareStatement(updateTicketStatusQuery);
                                pstmt.setInt(1, ticketID);
                                pstmt.executeUpdate();
                                System.out.println("Ticket assigned successfully!");
                            }
                        }
                    }
                } else {
                    System.out.println("Number of tickets can't be less than 1");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ticket does not exist or already assigned!");
        }
    }

    public static void displayAllTickets() throws SQLException {
        String displayTicketsQuery = "SELECT * FROM \"HelpDesk\".\"Ticket\" ORDER BY \"TicketID\"";
        stmt = JdbcConnection.con.createStatement();
        rs = stmt.executeQuery(displayTicketsQuery);
        if (!rs.isBeforeFirst()) {
            System.out.println("There are no tickets to show! Please create tickets");
        } else {
            while (rs.next()) {
                int ticketID = rs.getInt("TicketID");
                String description = rs.getString("Description");
                String status = rs.getString("Status");
                System.out.println();
                System.out.println("Ticket ID: " + ticketID + ", Description: " + description + ", Status: " + status);
            }
            System.out.println();
        }
    }

    public static boolean displayUnassignedTickets() throws SQLException {
        boolean flag = false;
        String displayTicketsQuery = "SELECT * FROM \"HelpDesk\".\"Ticket\" WHERE \"Status\" = 'Open' ORDER BY \"TicketID\"";
        stmt = JdbcConnection.con.createStatement();
        rs = stmt.executeQuery(displayTicketsQuery);
        if (!rs.isBeforeFirst()) {
            System.out.println("There are no Unassigned tickets!");
            return flag;
        } else {
            while (rs.next()) {
                int ticketID = rs.getInt("TicketID");
                String description = rs.getString("Description");
                String status = rs.getString("Status");
                System.out.println();
                System.out.println("Ticket ID: " + ticketID + ", Description: " + description + ", Status: " + status);
            }
            System.out.println();
            flag = true;
            return flag;
        }
    }

    public static boolean checkForTicketID(int ticketID) throws SQLException {
        boolean flag = false;
        String checkForTicketIDQuery = "SELECT * FROM \"HelpDesk\".\"Ticket\" WHERE EXISTS (SELECT 1 FROM \"HelpDesk\".\"Ticket\" WHERE \"HelpDesk\".\"Ticket\".\"TicketID\" = ? AND \"HelpDesk\".\"Ticket\".\"Status\" = 'Open')";
        pstmt = JdbcConnection.con.prepareStatement(checkForTicketIDQuery);
        pstmt.setInt(1, ticketID);
        rs = pstmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return flag;
        } else {
            flag = true;
            return flag;
        }
    }

    public static boolean checkForUserID(int userID) throws SQLException {
        boolean flag = false;
        String checkForTicketIDQuery = "SELECT * FROM \"HelpDesk\".\"User\" WHERE EXISTS (SELECT 1 FROM \"HelpDesk\".\"User\" WHERE \"HelpDesk\".\"User\".\"UserID\" = ?)";
        pstmt = JdbcConnection.con.prepareStatement(checkForTicketIDQuery);
        pstmt.setInt(1, userID);
        rs = pstmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return flag;
        } else {
            flag = true;
            return flag;
        }
    }

    public static boolean displayUsers() throws SQLException {
        boolean flag = false;
        System.out.println("Users available:");
        String displayUserQuery = "SELECT * FROM \"HelpDesk\".\"User\" order by \"UserID\"";
        stmt = JdbcConnection.con.createStatement();
        rs = stmt.executeQuery(displayUserQuery);
        if (!rs.isBeforeFirst()) {
            System.out.println("No Users Available");
            return flag;
        } else {
            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String userName = rs.getString("Username");
                System.out.println();
                System.out.println("User ID: " + userID + ", User name: " + userName);
            }
            System.out.println();
            flag = true;
            return flag;
        }
    }
}
