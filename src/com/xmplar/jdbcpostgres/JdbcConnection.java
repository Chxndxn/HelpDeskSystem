package com.xmplar.jdbcpostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

  public static Connection con = null;

  public static void Connection() throws SQLException {
    String host = "localhost";
    String port = "5432";
    String db_name = "HelpDesk";
    String username = "postgres";
    String password = "password";
    con =
      DriverManager.getConnection(
        "jdbc:postgresql://" + host + ":" + port + "/" + db_name + "",
        username,
        password
      );
    if (con != null) {
      System.out.println("Connected to " + host + ":" + port + "/" + db_name);
    } else {
      System.out.println("Connection Failed");
    }
  }
}
