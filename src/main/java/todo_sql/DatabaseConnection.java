package todo_sql;

import java.io.File;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:data/shop.db";

    public static Connection getConnection() throws SQLException {
        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void initSchema() {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                // Create categories table
                String createCategoriesSQL = "CREATE TABLE IF NOT EXISTS categories (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL)";
                stmt.execute(createCategoriesSQL);

                // Create products table
                String createProductsSQL = "CREATE TABLE IF NOT EXISTS products (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "category_id INTEGER, " +
                        "FOREIGN KEY(category_id) REFERENCES categories(id))";
                stmt.execute(createProductsSQL);
            }
        } catch (SQLException e) {
            System.out.println("Error initializing database schema: " + e.getMessage());
        }
    }
}
