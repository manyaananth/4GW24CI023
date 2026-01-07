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

/**
 * Recipe Detail Servlet
 * Returns detailed information about a specific recipe
 * 
 * @author Ruchikarr Team
 */
@WebServlet("/api/recipe")
public class RecipeDetailServlet extends HttpServlet {
    
    private RecipeDAO recipeDAO;
    
    @Override
    public void init() throws ServletException {
        recipeDAO = new RecipeDAO();
        System.out.println("✅ RecipeDetailServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            String idStr = request.getParameter("id");
            String servingsStr = request.getParameter("servings");
            
            if (idStr == null || idStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Recipe ID is required\"}");
                out.flush();
                return;
            }
            
            int recipeId = Integer.parseInt(idStr);
            double desiredServings = 0;
            
            if (servingsStr != null && !servingsStr.isEmpty()) {
                desiredServings = Double.parseDouble(servingsStr);
            }
            
            System.out.println("Fetching recipe details for ID: " + recipeId);
            
            Recipe recipe = recipeDAO.getRecipeById(recipeId);
            
            if (recipe == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter out = response.getWriter();
                out.print("{\"error\": \"Recipe not found\"}");
                out.flush();
                return;
            }
            
            // If servings scaling is requested
            if (desiredServings > 0) {
                // Scale ingredients (this is where DSA algorithm comes in)
                String scaledIngredients = recipe.getScaledIngredients(desiredServings);
                recipe.setIngredients(scaledIngredients);
            }
            
            String jsonResponse = convertRecipeToJson(recipe);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
            System.out.println("✅ Recipe details sent successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error in RecipeDetailServlet: " + e.getMessage());
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            out.flush();
        }
    }
    
    /**
     * Manually convert a recipe to JSON format
     */
    private String convertRecipeToJson(Recipe recipe) {
        StringBuilder json = new StringBuilder("{");
        
        json.append("\"recipeId\":" + recipe.getRecipeId() + ",")
            .append("\"srNo\":" + recipe.getSrNo() + ",")
            .append("\"recipeName\":\"" + escapeJson(recipe.getRecipeName()) + "\",")
            .append("\"translatedRecipeName\":\"" + escapeJson(recipe.getTranslatedRecipeName()) + "\",")
            .append("\"ingredients\":\"" + escapeJson(recipe.getIngredients()) + "\",")
            .append("\"translatedIngredients\":\"" + escapeJson(recipe.getTranslatedIngredients()) + "\",")
            .append("\"prepTimeMins\":" + recipe.getPrepTimeMins() + ",")
            .append("\"cookTimeMins\":" + recipe.getCookTimeMins() + ",")
            .append("\"totalTimeMins\":" + recipe.getTotalTimeMins() + ",")
            .append("\"servings\":" + recipe.getServings() + ",")
            .append("\"cuisine\":\"" + escapeJson(recipe.getCuisine()) + "\",")
            .append("\"course\":\"" + escapeJson(recipe.getCourse()) + "\",")
            .append("\"diet\":\"" + escapeJson(recipe.getDiet()) + "\",")
            .append("\"instructions\":\"" + escapeJson(recipe.getInstructions()) + "\",")
            .append("\"translatedInstructions\":\"" + escapeJson(recipe.getTranslatedInstructions()) + "\",")
            .append("\"url\":\"" + escapeJson(recipe.getUrl()) + "\",")
            .append("\"region\":\"" + escapeJson(recipe.getRegion()) + "\",")
            .append("\"weatherSuitable\":\"" + escapeJson(recipe.getWeatherSuitable()) + "\"")
            .append("}");
        
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