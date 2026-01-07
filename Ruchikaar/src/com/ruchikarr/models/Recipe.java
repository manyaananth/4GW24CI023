package com.ruchikarr.models;

/**
 * Recipe Model Class
 * Represents a single recipe with all its properties
 * 
 * @author Ruchikarr Team
 */
public class Recipe {
    
    // Private fields
    private int recipeId;
    private int srNo;
    private String recipeName;
    private String translatedRecipeName;
    private String ingredients;
    private String translatedIngredients;
    private int prepTimeMins;
    private int cookTimeMins;
    private int totalTimeMins;
    private double servings;
    private String cuisine;
    private String course;
    private String diet;
    private String instructions;
    private String translatedInstructions;
    private String url;
    private String region;
    private String weatherSuitable;
    
    // Constructors
    
    /**
     * Default Constructor
     */
    public Recipe() {
    }
    
    /**
     * Parameterized Constructor - Full
     */
    public Recipe(int recipeId, int srNo, String recipeName, String translatedRecipeName,
                  String ingredients, String translatedIngredients, int prepTimeMins,
                  int cookTimeMins, int totalTimeMins, double servings, String cuisine,
                  String course, String diet, String instructions, String translatedInstructions,
                  String url, String region, String weatherSuitable) {
        this.recipeId = recipeId;
        this.srNo = srNo;
        this.recipeName = recipeName;
        this.translatedRecipeName = translatedRecipeName;
        this.ingredients = ingredients;
        this.translatedIngredients = translatedIngredients;
        this.prepTimeMins = prepTimeMins;
        this.cookTimeMins = cookTimeMins;
        this.totalTimeMins = totalTimeMins;
        this.servings = servings;
        this.cuisine = cuisine;
        this.course = course;
        this.diet = diet;
        this.instructions = instructions;
        this.translatedInstructions = translatedInstructions;
        this.url = url;
        this.region = region;
        this.weatherSuitable = weatherSuitable;
    }
    
    /**
     * Constructor - Essential fields only
     */
    public Recipe(String recipeName, String ingredients, int totalTimeMins, 
                  double servings, String cuisine, String region, String weatherSuitable) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.totalTimeMins = totalTimeMins;
        this.servings = servings;
        this.cuisine = cuisine;
        this.region = region;
        this.weatherSuitable = weatherSuitable;
    }
    
    // Getters and Setters
    
    public int getRecipeId() {
        return recipeId;
    }
    
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    
    public int getSrNo() {
        return srNo;
    }
    
    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }
    
    public String getRecipeName() {
        return recipeName;
    }
    
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    
    public String getTranslatedRecipeName() {
        return translatedRecipeName;
    }
    
    public void setTranslatedRecipeName(String translatedRecipeName) {
        this.translatedRecipeName = translatedRecipeName;
    }
    
    public String getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    
    public String getTranslatedIngredients() {
        return translatedIngredients;
    }
    
    public void setTranslatedIngredients(String translatedIngredients) {
        this.translatedIngredients = translatedIngredients;
    }
    
    public int getPrepTimeMins() {
        return prepTimeMins;
    }
    
    public void setPrepTimeMins(int prepTimeMins) {
        this.prepTimeMins = prepTimeMins;
    }
    
    public int getCookTimeMins() {
        return cookTimeMins;
    }
    
    public void setCookTimeMins(int cookTimeMins) {
        this.cookTimeMins = cookTimeMins;
    }
    
    public int getTotalTimeMins() {
        return totalTimeMins;
    }
    
    public void setTotalTimeMins(int totalTimeMins) {
        this.totalTimeMins = totalTimeMins;
    }
    
    public double getServings() {
        return servings;
    }
    
    public void setServings(double servings) {
        this.servings = servings;
    }
    
    public String getCuisine() {
        return cuisine;
    }
    
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    
    public String getCourse() {
        return course;
    }
    
    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getDiet() {
        return diet;
    }
    
    public void setDiet(String diet) {
        this.diet = diet;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public String getTranslatedInstructions() {
        return translatedInstructions;
    }
    
    public void setTranslatedInstructions(String translatedInstructions) {
        this.translatedInstructions = translatedInstructions;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getWeatherSuitable() {
        return weatherSuitable;
    }
    
    public void setWeatherSuitable(String weatherSuitable) {
        this.weatherSuitable = weatherSuitable;
    }
    
    // Utility Methods
    
    /**
     * Scale ingredient quantities based on desired servings
     * This demonstrates algorithm implementation (DSA concept)
     */
    public String getScaledIngredients(double desiredServings) {
        if (this.servings == 0) return this.ingredients;
        
        double scaleFactor = desiredServings / this.servings;
        String scaled = this.ingredients;
        
        // Simple scaling logic - multiply all numbers by scale factor
        // In real implementation, you'd parse and scale each ingredient
        scaled = "Scaled for " + desiredServings + " servings (factor: " + 
                 String.format("%.2f", scaleFactor) + ")\n" + this.ingredients;
        
        return scaled;
    }
    
    /**
     * Get ingredients as array (for searching/filtering)
     */
    public String[] getIngredientsArray() {
        if (ingredients == null) return new String[0];
        return ingredients.toLowerCase().split("[,;]");
    }
    
    /**
     * Check if recipe contains a specific ingredient
     * Used in ingredient-based search (DSA: String matching)
     */
    public boolean containsIngredient(String ingredient) {
        if (this.ingredients == null || ingredient == null) return false;
        return this.ingredients.toLowerCase().contains(ingredient.toLowerCase());
    }
    
    /**
     * Get recipe summary for display
     */
    public String getSummary() {
        return String.format("%s (%s) - %d mins, %s servings", 
                           recipeName, cuisine, totalTimeMins, servings);
    }
    
    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", recipeName='" + recipeName + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", totalTime=" + totalTimeMins + " mins" +
                ", servings=" + servings +
                ", region='" + region + '\'' +
                ", weather='" + weatherSuitable + '\'' +
                '}';
    }
}
