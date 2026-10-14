
package dev.morphe.metrics.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnectionTest {

  public static void main(String[] args) throws Exception {
    try (Connection connection = DatabaseConnection.open();
         Statement statement = connection.createStatement();
         ResultSet result = statement.executeQuery(
             "SELECT current_database(), current_schema()")) {

      if (result.next()) {
        System.out.println("Database: " + result.getString(1));
        System.out.println("Schema: " + result.getString(2));
      }
    }
  }
}