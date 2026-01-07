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
 * Ingredient Search Servlet
 * Searches recipes by ingredients
 * 
 * @author Ruchikarr Team
 */
@WebServlet("/api/search")
public class IngredientSearchServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
        System.out.println("✅ IngredientSearchServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            // Get search parameters
            String ingredients = request.getParameter("ingredients");
            String region = request.getParameter("region");
            String weather = request.getParameter("weather");
            String timeStr = request.getParameter("time");
            
            if (ingredients == null || ingredients.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Ingredients parameter is required\"}");
                out.flush();
                return;
            }
            
            // Parse ingredients (comma-separated)
            String[] ingredientArray = ingredients.split(",");
            
            // Parse other parameters
            int maxTime = (timeStr != null) ? Integer.parseInt(timeStr) : 0;
            if (region == null) region = "All India";
            if (weather == null) weather = "Pleasant";
            
            System.out.println("Searching recipes with ingredients: " + ingredients);
            
            // Search recipes
            List<Recipe> recipes = recipeDAO.searchByMultipleIngredients(
                ingredientArray, region, weather, maxTime
            );
            
            // Send JSON response
            String jsonResponse = convertRecipesToJson(recipes);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
            System.out.println("✅ Found " + recipes.size() + " recipes");
            
        } catch (Exception e) {
            System.err.println("❌ Error in IngredientSearchServlet: " + e.getMessage());
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