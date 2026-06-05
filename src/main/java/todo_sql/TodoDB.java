package todo_sql;

import java.io.File;
import java.sql.*;

public class TodoDB {
    private static final String DB_URL = "jdbc:sqlite:data/todo.db";
    private static final String TABLE_NAME = "tasks";

    public TodoDB() {
        initSchema();
    }

    public Connection connect() throws SQLException {
        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        return DriverManager.getConnection(DB_URL);
    }

    public void initSchema() {
        try (Connection conn = connect()) {
            try (Statement stmt = conn.createStatement()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "task TEXT NOT NULL," +
                        "category TEXT NOT NULL)";
                stmt.execute(createTableSQL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema: " + e.getMessage(), e);
        }
    }

    public void add(String task, String category) {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (task, category) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, task);
            pstmt.setString(2, category);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }

    public void findAll() {
        String selectSQL = "SELECT id, task, category FROM " + TABLE_NAME + " ORDER BY id";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String task = rs.getString("task");
                String category = rs.getString("category");
                System.out.println(id + ": " + task + " [" + category + "]");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving tasks: " + e.getMessage());
        }
    }

    public void removeById(int id) {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing task: " + e.getMessage());
        }
    }

    public void findByCategory(String category) {
        String selectSQL = "SELECT id, task, category FROM " + TABLE_NAME + " WHERE category = ? ORDER BY id";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String task = rs.getString("task");
                    String cat = rs.getString("category");
                    System.out.println(id + ": " + task + " [" + cat + "]");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving tasks by category: " + e.getMessage());
        }
    }
}
