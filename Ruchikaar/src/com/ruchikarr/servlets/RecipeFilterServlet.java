package com.ruchikarr.servlets;

import com.ruchikarr.dao.RecipeDAO;
import com.ruchikarr.models.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Recipe Filter Servlet
 * Handles filtering recipes based on region, weather, and time
 * 
 * @author Ruchikarr Team
 */
@WebServlet("/api/filter")
public class RecipeFilterServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
        System.out.println("✅ RecipeFilterServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Enable CORS (for development)
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            // Get filter parameters
            String region = request.getParameter("region");
            String weather = request.getParameter("weather");
            String timeStr = request.getParameter("time");
            
            // Parse time (default to 60 mins if not provided)
            int maxTime = 60;
            if (timeStr != null && !timeStr.isEmpty()) {
                try {
                    maxTime = Integer.parseInt(timeStr);
                } catch (NumberFormatException e) {
                    maxTime = 60;
                }
            }
            
            // Validate inputs
            if (region == null || region.isEmpty()) {
                region = "All India";
            }
            if (weather == null || weather.isEmpty()) {
                weather = "Pleasant";
            }
            
            System.out.println("Filtering recipes: region=" + region + 
                             ", weather=" + weather + ", maxTime=" + maxTime);
            
            // Get filtered recipes from database
            List<Recipe> recipes = recipeDAO.getFilteredRecipes(region, weather, maxTime);
            
            // Convert to JSON and send response
            String jsonResponse = convertRecipesToJson(recipes);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
            System.out.println("✅ Returned " + recipes.size() + " recipes");
            
        } catch (Exception e) {
            System.err.println("❌ Error in RecipeFilterServlet: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            out.flush();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Manually convert list of recipes to JSON format
     */
    private String convertRecipesToJson(List<Recipe> recipes) {
        StringBuilder json = new StringBuilder("[");
        
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            if (i > 0) json.append(",");
            
            json.append("{")
                .append("\"recipeId\":" + recipe.getRecipeId() + ",")
                .append("\"recipeName\":\"" + escapeJson(recipe.getRecipeName()) + "\",")
                .append("\"region\":\"" + escapeJson(recipe.getRegion()) + "\",")
                .append("\"weatherSuitable\":\"" + escapeJson(recipe.getWeatherSuitable()) + "\",")
                .append("\"totalTimeMins\":" + recipe.getTotalTimeMins() + ",")
                .append("\"servings\":" + recipe.getServings() + ",")
                .append("\"cuisine\":\"" + escapeJson(recipe.getCuisine()) + "\",")
                .append("\"url\":\"" + escapeJson(recipe.getUrl()) + "\",")
                .append("\"ingredients\":\"" + escapeJson(recipe.getIngredients()) + "\",")
                .append("\"instructions\":\"" + escapeJson(recipe.getInstructions()) + "\"")
                .append("}");
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Escape special characters for JSON
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
