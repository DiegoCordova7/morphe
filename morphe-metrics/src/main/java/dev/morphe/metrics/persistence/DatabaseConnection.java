
package dev.morphe.metrics.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

  private static final String URL = "jdbc:postgresql://localhost:5432/morphe";
  private static final String USER = "postgres";
  private static final String PASSWORD = "Daigus7105CC";

  private DatabaseConnection() {}

  public static Connection open() throws SQLException {
    Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
    connection.setSchema("morphe_dw");
    return connection;
  }

  private static String requireEnvironmentVariable(String name) {
    String value = System.getenv(name);
    if (value == null || value.isBlank()) throw new IllegalStateException("Missing environment variable: " + name);
    return value;
  }
}
