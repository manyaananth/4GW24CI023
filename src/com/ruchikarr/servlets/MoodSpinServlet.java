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
 * Mood Spin Servlet
 * Returns random recipes for the spinning wheel
 * 
 * @author Ruchikarr Team
 */
@WebServlet("/api/moodspin")
public class MoodSpinServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
        System.out.println("✅ MoodSpinServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            // Get parameters
            String region = request.getParameter("region");
            String weather = request.getParameter("weather");
            String timeStr = request.getParameter("time");
            String countStr = request.getParameter("count");
            
            // Parse parameters
            int maxTime = (timeStr != null) ? Integer.parseInt(timeStr) : 60;
            int count = (countStr != null) ? Integer.parseInt(countStr) : 10;
            
            if (region == null) region = "All India";
            if (weather == null) weather = "Pleasant";
            
            System.out.println("Getting random recipes for mood spin: " + count + " recipes");
            
            // Get random recipes
            List<Recipe> recipes = recipeDAO.getRandomRecipes(count, region, weather, maxTime);
            
            // Send JSON response
            String jsonResponse = convertRecipesToJson(recipes);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
            System.out.println("✅ Returned " + recipes.size() + " random recipes");
            
        } catch (Exception e) {
            System.err.println("❌ Error in MoodSpinServlet: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            out.flush();
        }
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