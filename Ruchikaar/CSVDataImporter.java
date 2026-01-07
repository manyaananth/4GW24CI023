import java.io.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Robust CSV Data Importer for Ruchikarr Project
 * Designed to handle common CSV formatting errors and data type mismatches.
 */
public class CSVDataImporter {
    
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ruchikarr_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Default XAMPP password is empty
    
    public static void main(String[] args) {
        String csvFilePath = "recipes_data.csv"; 
        
        try {
            importCSVToDatabase(csvFilePath);
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void importCSVToDatabase(String csvFilePath) throws Exception {
        // Load Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Updated SQL to match your table columns exactly
            String sql = "INSERT INTO recipes (sr_no, recipe_name, translated_recipe_name, " +
                         "ingredients, translated_ingredients, prep_time_mins, cook_time_mins, " +
                         "total_time_mins, servings, cuisine, course, diet, instructions, " +
                         "translated_instructions, url, region, weather_suitable) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
                
                String line;
                int rowCount = 0;
                int successCount = 0;
                
                br.readLine(); // Skip the header row
                
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    try {
                        String[] data = parseCSVLine(line);
                        
                        // Ensure we have the minimum required columns
                        if (data.length < 15) continue;

                        // 1. Clean and Parse ID (sr_no)
                        pstmt.setInt(1, safeParseInt(data[0]));
                        
                        // 2. Map String Fields
                        pstmt.setString(2, data[1]); 
                        pstmt.setString(3, data[2]); 
                        pstmt.setString(4, data[3]); 
                        pstmt.setString(5, data[4]); 

                        // 3. Clean and Parse Numbers
                        pstmt.setInt(6, safeParseInt(data[5])); // prep_time
                        pstmt.setInt(7, safeParseInt(data[6])); // cook_time
                        pstmt.setInt(8, safeParseInt(data[7])); // total_time
                        pstmt.setDouble(9, safeParseDouble(data[8])); // servings

                        // 4. Map Remaining String Fields
                        pstmt.setString(10, data[9]); 
                        pstmt.setString(11, data[10]); 
                        pstmt.setString(12, data[11]); 
                        pstmt.setString(13, data[12]); 
                        pstmt.setString(14, data[13]); 
                        pstmt.setString(15, data[14]); 
                        
                        // 5. Default Values for new columns
                        pstmt.setString(16, "All India");
                        pstmt.setString(17, "Pleasant");

                        pstmt.executeUpdate();
                        successCount++;
                        
                    } catch (Exception e) {
                        // Logs the specific row that failed without stopping the whole script
                        System.err.println("Skipping row " + (rowCount + 2) + " due to data error: " + e.getMessage());
                    }
                    
                    rowCount++;
                    if (successCount % 100 == 0 && successCount > 0) {
                        System.out.println("Imported " + successCount + " recipes so far...");
                    }
                }
                
                System.out.println("------------------------------------------");
                System.out.println("IMPORT SUMMARY");
                System.out.println("Total Rows Processed: " + rowCount);
                System.out.println("Successfully Imported: " + successCount);
                System.out.println("Failed Rows: " + (rowCount - successCount));
                System.out.println("------------------------------------------");
            }
        }
    }

    // Helper to clean strings and extract only numbers
    private static int safeParseInt(String s) {
        if (s == null) return 0;
        try {
            String clean = s.replaceAll("[^0-9.]", "").trim();
            if (clean.isEmpty()) return 0;
            return (int) Double.parseDouble(clean);
        } catch (Exception e) { return 0; }
    }

    private static double safeParseDouble(String s) {
        if (s == null) return 0.0;
        try {
            String clean = s.replaceAll("[^0-9.]", "").trim();
            if (clean.isEmpty()) return 0.0;
            return Double.parseDouble(clean);
        } catch (Exception e) { return 0.0; }
    }

    // Improved CSV Parser to handle commas inside quotes
    private static String[] parseCSVLine(String line) {
        ArrayList<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString().trim());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        result.add(field.toString().trim());
        return result.toArray(new String[0]);
    }
}