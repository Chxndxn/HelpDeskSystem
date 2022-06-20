import java.util.InputMismatchException;
import java.util.Scanner;

import com.xmplar.jdbcpostgres.HelpDeskDAO;
import com.xmplar.jdbcpostgres.JdbcConnection;

public class App {

        public static void main(String[] args) throws Exception {
                JdbcConnection.Connection();
                int ch = 0;
                try {
                        do {
                                System.out.println("1: Create Ticket");
                                System.out.println("2: Create User");
                                System.out.println("3: Assign Ticket to User");
                                System.out.println("4: List All Tickets");
                                System.out.println("5: List All Unassigned Tickets");
                                System.out.println("6: Exit");
                                System.out.println("Enter choice(1-6):");
                                Scanner sc = new Scanner(System.in);
                                String choice = "";
                                choice = sc.next();
                                sc.nextLine();
                                try {
                                        ch = Integer.parseInt(choice);
                                        if (ch > 0 && ch <= 6) {
                                                switch (ch) {
                                                        case 1:
                                                                try {
                                                                        System.out.println(
                                                                                        "Enter number of tickets to create:");
                                                                        int numberOfTickets = sc.nextInt();
                                                                        if (numberOfTickets >= 1) {
                                                                                for (int i = 0; i < numberOfTickets; i++) {
                                                                                        System.out.println(
                                                                                                        "Enter Ticket ID:");
                                                                                        int ticketID = sc.nextInt();
                                                                                        System.out.println(
                                                                                                        "Enter Description:");
                                                                                        String description = sc
                                                                                                        .next();
                                                                                        description += sc.nextLine();
                                                                                        String status = "Open";
                                                                                        HelpDeskDAO.createTicket(
                                                                                                        ticketID,
                                                                                                        description,
                                                                                                        status);
                                                                                }
                                                                                System.out.println(
                                                                                                "Tickets added successfully");
                                                                        } else {
                                                                                System.out.println(
                                                                                                "Number of tickets can't be less than 1");
                                                                        }
                                                                } catch (InputMismatchException e) {
                                                                        System.out.println("Enter a valid input!");
                                                                }
                                                                break;
                                                        case 2:
                                                                try {
                                                                        System.out.println(
                                                                                        "Enter the number of users to create: ");
                                                                        int numberOfUsers = sc.nextInt();
                                                                        if (numberOfUsers > 0) {
                                                                                for (int i = 0; i < numberOfUsers; i++) {
                                                                                        System.out.println(
                                                                                                        "Creating User");
                                                                                        System.out.println(
                                                                                                        "Enter User ID:");
                                                                                        int userID = sc.nextInt();
                                                                                        System.out.println(
                                                                                                        "Enter Username:");
                                                                                        String username = sc.next();
                                                                                        username += sc.nextLine();

                                                                                        HelpDeskDAO.createUser(userID,
                                                                                                        username);
                                                                                }
                                                                        } else {
                                                                                System.out.println(
                                                                                                "Number of users can't be less than 1");
                                                                        }
                                                                } catch (InputMismatchException e) {
                                                                        System.out.println("Enter a valid input!");
                                                                }
                                                                break;
                                                        case 3:
                                                                try {
                                                                        HelpDeskDAO.assignTickets();
                                                                } catch (Exception e) {
                                                                        System.out.println("Invalid assigning input");
                                                                }
                                                                break;
                                                        case 4:
                                                                HelpDeskDAO.displayAllTickets();
                                                                break;
                                                        case 5:
                                                                HelpDeskDAO.displayUnassignedTickets();
                                                                break;
                                                        default:
                                                                System.out.println("Exiting...\nThank you!");
                                                                break;
                                                }
                                        } else {
                                                System.out.println("Invalid input. Please choose between 1 to 6");
                                        }
                                } catch (Exception e) {
                                        System.out.println("Invalid input. Please enter valid number");
                                }
                        } while (ch != 6);
                } catch (InputMismatchException e) {
                        System.out.println("Please enter valid input!");
                }
        }
}
