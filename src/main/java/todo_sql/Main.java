package todo_sql;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String CATEGORIES_TABLE = "categories";
    private static final String PRODUCTS_TABLE = "products";

    public static void main(String[] args) {
        DatabaseConnection.initSchema();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Izvēle: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addCategory(scanner);
                    break;
                case "2":
                    addProduct(scanner);
                    break;
                case "3":
                    showAllCategories();
                    break;
                case "4":
                    showAllProducts();
                    break;
                case "5":
                    searchProductsByCategory(scanner);
                    break;
                case "0":
                    running = false;
                    System.out.println("Līdz redzēšanai!");
                    break;
                default:
                    System.out.println("Nepareiza izvēle. Lūdzu mēģiniet vēlreiz.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Shop Menu ===");
        System.out.println("1 - Pievienot kategoriju");
        System.out.println("2 - Pievienot produktu");
        System.out.println("3 - Parādīt visas kategorijas");
        System.out.println("4 - Parādīt visus produktus");
        System.out.println("5 - Meklēt produktus pēc kategorijas");
        System.out.println("0 - Iziet");
    }

    private static void addCategory(Scanner scanner) {
        System.out.print("Category name: ");
        String name = scanner.nextLine();

        String sql = "INSERT INTO " + CATEGORIES_TABLE + " (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Kategorija pievienota veiksmīgi!");
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    private static void addProduct(Scanner scanner) {
        // Show all categories first
        showAllCategories();

        System.out.print("Product name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        String sql = "INSERT INTO " + PRODUCTS_TABLE + " (name, price, category_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, categoryId);
            pstmt.executeUpdate();
            System.out.println("Produkts pievienots veiksmīgi!");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void showAllCategories() {
        String sql = "SELECT id, name FROM " + CATEGORIES_TABLE;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n=== Kategorijas ===");
            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(id + ": " + name);
            }
            if (!hasRows) {
                System.out.println("Nav kategoriju.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }

    private static void showAllProducts() {
        String sql = "SELECT p.id, p.name, p.price, c.name as category_name " +
                "FROM " + PRODUCTS_TABLE + " p " +
                "LEFT JOIN " + CATEGORIES_TABLE + " c ON p.category_id = c.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n=== Produkti ===");
            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String categoryName = rs.getString("category_name");
                if (categoryName == null) {
                    categoryName = "Bez kategorijas";
                }
                System.out.println(id + ": " + name + ", " + price + ", [" + categoryName + "]");
            }
            if (!hasRows) {
                System.out.println("Nav produktu.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
    }

    private static void searchProductsByCategory(Scanner scanner) {
        System.out.print("Category ID or name: ");
        String input = scanner.nextLine();

        String sql = "SELECT p.id, p.name, p.price, c.name as category_name " +
                "FROM " + PRODUCTS_TABLE + " p " +
                "LEFT JOIN " + CATEGORIES_TABLE + " c ON p.category_id = c.id " +
                "WHERE p.category_id = ? OR c.name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Try to parse as ID first
            try {
                int categoryId = Integer.parseInt(input);
                pstmt.setInt(1, categoryId);
                pstmt.setString(2, input);
            } catch (NumberFormatException e) {
                // If not a number, use 0 for ID and the string for name
                pstmt.setInt(1, 0);
                pstmt.setString(2, input);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== Produkti kategorijā ===");
                boolean hasRows = false;
                while (rs.next()) {
                    hasRows = true;
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    String categoryName = rs.getString("category_name");
                    if (categoryName == null) {
                        categoryName = "Bez kategorijas";
                    }
                    System.out.println(id + ": " + name + ", " + price + ", [" + categoryName + "]");
                }
                if (!hasRows) {
                    System.out.println("Nav atrasts produktu.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching products: " + e.getMessage());
        }
    }
}
