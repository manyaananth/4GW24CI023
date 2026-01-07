package com.ruchikarr.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Manager Class
 * Handles database connections and provides connection pooling
 * 
 * @author Ruchikarr Team
 */
public class DatabaseManager {
    
    // Database Configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ruchikarr_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Change if you set password
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Static initialization block to load driver
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("✅ MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get database connection
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database!");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            throw e;
        }
    }
    
    /**
     * Test database connection
     * 
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection test successful!");
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection test failed!");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Close database connection safely
     * 
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✅ Database connection closed");
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Close multiple database resources safely
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("❌ Error closing resource");
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Main method for testing database connection
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   DATABASE CONNECTION TEST");
        System.out.println("========================================\n");
        
        System.out.println("Testing connection to: " + DB_URL);
        
        if (testConnection()) {
            System.out.println("\n🎉 Database is ready for use!");
        } else {
            System.out.println("\n⚠️  Please check your database configuration");
            System.out.println("   - Is XAMPP MySQL running?");
            System.out.println("   - Does database 'ruchikarr_db' exist?");
            System.out.println("   - Are credentials correct?");
        }
    }
}