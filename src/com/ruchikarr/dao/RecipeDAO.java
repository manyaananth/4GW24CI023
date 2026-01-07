package com.ruchikarr.dao;

import com.ruchikarr.models.Recipe;
import com.ruchikarr.utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Recipe Data Access Object (DAO)
 * Handles all database operations for recipes
 * Implements DSA concepts: ArrayList for storage, filtering algorithms
 * 
 * @author Ruchikarr Team
 */
public class RecipeDAO {
    
    /**
     * Get filtered recipes based on user criteria
     * DSA: ArrayList, Filtering algorithm
     * 
     * @param region Selected region
     * @param weather Selected weather
     * @param maxTime Maximum cooking time
     * @return List of matching recipes
     */
    public List<Recipe> getFilteredRecipes(String region, String weather, int maxTime) {
        List<Recipe> recipes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseManager.getConnection();
            
            // Dynamic SQL query building based on filters
            StringBuilder sql = new StringBuilder(
                "SELECT * FROM recipes WHERE 1=1"
            );
            
            List<Object> parameters = new ArrayList<>();
            
            // Add region filter
            if (region != null && !region.equals("All India")) {
                sql.append(" AND (region = ? OR region = 'All India')");
                parameters.add(region);
            }
            
            // Add weather filter
            if (weather != null && !weather.equals("All")) {
                sql.append(" AND (weather_suitable = ? OR weather_suitable = 'Pleasant')");
                parameters.add(weather);
            }
            
            // Add time filter
            if (maxTime > 0) {
                sql.append(" AND total_time_mins <= ?");
                parameters.add(maxTime);
            }
            
            pstmt = conn.prepareStatement(sql.toString());
            
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            rs = pstmt.executeQuery();
            
            // Convert ResultSet to Recipe objects
            while (rs.next()) {
                recipes.add(extractRecipeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching filtered recipes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResources(rs, pstmt, conn);
        }
        
        return recipes;
    }
    
    /**
     * Search recipes by ingredient
     * DSA: String matching algorithm, ArrayList
     * 
     * @param ingredient Ingredient to search for
     * @return List of matching recipes
     */
    public List<Recipe> searchByIngredient(String ingredient) {
        List<Recipe> recipes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseManager.getConnection();
            
            String sql = "SELECT * FROM recipes WHERE LOWER(ingredients) LIKE ? " +
                        "OR LOWER(translated_ingredients) LIKE ?";
            
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + ingredient.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                recipes.add(extractRecipeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching recipes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResources(rs, pstmt, conn);
        }
        
        return recipes;
    }
    
    /**
     * Search recipes by multiple ingredients (at least one match)
     * DSA: Multiple string matching, OR logic
     */
    public List<Recipe> searchByMultipleIngredients(String[] ingredients, 
                                                      String region, String weather, int maxTime) {
        List<Recipe> recipes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseManager.getConnection();
            
            // Build query with multiple ingredient searches
            StringBuilder sql = new StringBuilder(
                "SELECT * FROM recipes WHERE ("
            );
            
            List<Object> parameters = new ArrayList<>();
            
            // Add ingredient conditions
            for (int i = 0; i < ingredients.length; i++) {
                if (i > 0) sql.append(" OR ");
                sql.append("LOWER(ingredients) LIKE ? OR LOWER(translated_ingredients) LIKE ?");
                String pattern = "%" + ingredients[i].toLowerCase().trim() + "%";
                parameters.add(pattern);
                parameters.add(pattern);
            }
            
            sql.append(")");
            
            // Add other filters
            if (region != null && !region.equals("All India")) {
                sql.append(" AND (region = ? OR region = 'All India')");
                parameters.add(region);
            }
            
            if (weather != null && !weather.equals("All")) {
                sql.append(" AND (weather_suitable = ? OR weather_suitable = 'Pleasant')");
                parameters.add(weather);
            }
            
            if (maxTime > 0) {
                sql.append(" AND total_time_mins <= ?");
                parameters.add(maxTime);
            }
            
            pstmt = conn.prepareStatement(sql.toString());
            
            // Set all parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                recipes.add(extractRecipeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching with multiple ingredients: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResources(rs, pstmt, conn);
        }
        
        return recipes;
    }
    
    /**
     * Get recipe by ID
     */
    public Recipe getRecipeById(int recipeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseManager.getConnection();
            String sql = "SELECT * FROM recipes WHERE recipe_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recipeId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractRecipeFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching recipe by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseManager.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Get random recipes (for mood spin)
     * DSA: Random selection algorithm
     */
    public List<Recipe> getRandomRecipes(int count, String region, String weather, int maxTime) {
        List<Recipe> allRecipes = getFilteredRecipes(region, weather, maxTime);
        List<Recipe> randomRecipes = new ArrayList<>();
        
        if (allRecipes.isEmpty()) return randomRecipes;
        
        // Fisher-Yates shuffle algorithm for random selection
        java.util.Random random = new java.util.Random();
        int selectCount = Math.min(count, allRecipes.size());
        
        for (int i = 0; i < selectCount; i++) {
            int randomIndex = random.nextInt(allRecipes.size());
            randomRecipes.add(allRecipes.remove(randomIndex));
        }
        
        return randomRecipes;
    }
    
    /**
     * Get total recipe count
     */
    public int getTotalRecipeCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM recipes");
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error counting recipes: " + e.getMessage());
        } finally {
            DatabaseManager.closeResources(rs, stmt, conn);
        }
        
        return 0;
    }
    
    /**
     * Helper method to extract Recipe object from ResultSet
     */
    private Recipe extractRecipeFromResultSet(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        
        recipe.setRecipeId(rs.getInt("recipe_id"));
        recipe.setSrNo(rs.getInt("sr_no"));
        recipe.setRecipeName(rs.getString("recipe_name"));
        recipe.setTranslatedRecipeName(rs.getString("translated_recipe_name"));
        recipe.setIngredients(rs.getString("ingredients"));
        recipe.setTranslatedIngredients(rs.getString("translated_ingredients"));
        recipe.setPrepTimeMins(rs.getInt("prep_time_mins"));
        recipe.setCookTimeMins(rs.getInt("cook_time_mins"));
        recipe.setTotalTimeMins(rs.getInt("total_time_mins"));
        recipe.setServings(rs.getDouble("servings"));
        recipe.setCuisine(rs.getString("cuisine"));
        recipe.setCourse(rs.getString("course"));
        recipe.setDiet(rs.getString("diet"));
        recipe.setInstructions(rs.getString("instructions"));
        recipe.setTranslatedInstructions(rs.getString("translated_instructions"));
        recipe.setUrl(rs.getString("url"));
        recipe.setRegion(rs.getString("region"));
        recipe.setWeatherSuitable(rs.getString("weather_suitable"));
        
        return recipe;
    }
    
    /**
     * Test method
     */
    public static void main(String[] args) {
        RecipeDAO dao = new RecipeDAO();
        
        System.out.println("Total recipes: " + dao.getTotalRecipeCount());
        
        System.out.println("\nTesting filtered search:");
        List<Recipe> recipes = dao.getFilteredRecipes("South India", "Hot", 30);
        System.out.println("Found " + recipes.size() + " recipes");
        
        if (!recipes.isEmpty()) {
            System.out.println("\nSample recipe:");
            System.out.println(recipes.get(0));
        }
    }
}