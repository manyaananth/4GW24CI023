<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recipe Details - Ruchikarr</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <!-- Header -->
        <header class="header">
            <div class="logo">
                <span class="logo-icon">👨‍🍳</span>
                <h1>Ruchikarr</h1>
            </div>
            <a href="filter.html" class="back-link">← Back to Search</a>
        </header>

        <!-- Main Content -->
        <main class="main-content">
            <div id="loadingRecipe" class="loading">
                <div class="spinner"></div>
                <p>Loading recipe details...</p>
            </div>

            <div id="recipeDetail" class="recipe-detail" style="display: none;">
                <!-- Recipe Header -->
                <div class="recipe-header">
                    <h1 id="recipeName" class="recipe-title"></h1>
                    <p id="recipeTranslated" class="recipe-subtitle"></p>
                    
                    <div class="recipe-meta">
                        <span class="meta-item">
                            <span class="meta-icon">🗺️</span>
                            <span id="recipeRegion"></span>
                        </span>
                        <span class="meta-item">
                            <span class="meta-icon">🌦️</span>
                            <span id="recipeWeather"></span>
                        </span>
                        <span class="meta-item">
                            <span class="meta-icon">⏱️</span>
                            <span id="recipeTime"></span> mins
                        </span>
                        <span class="meta-item">
                            <span class="meta-icon">🍽️</span>
                            <span id="recipeServings"></span> servings
                        </span>
                    </div>

                    <div class="recipe-tags">
                        <span class="tag" id="recipeCuisine"></span>
                        <span class="tag" id="recipeCourse"></span>
                        <span class="tag" id="recipeDiet"></span>
                    </div>
                </div>

                <!-- Recipe Content -->
                <div class="recipe-content">
                    <!-- Ingredients Section -->
                    <div class="recipe-section">
                        <h2 class="section-heading">
                            <span class="section-icon">🥘</span>
                            Ingredients
                        </h2>
                        
                        <!-- Servings Adjuster -->
                        <div class="servings-adjuster">
                            <label>Adjust Servings:</label>
                            <div class="adjuster-controls">
                                <button id="decreaseServings" class="adjuster-btn">-</button>
                                <input type="number" id="servingsInput" class="servings-input" 
                                       min="0.5" max="20" step="0.5" value="4">
                                <button id="increaseServings" class="adjuster-btn">+</button>
                                <button id="updateServings" class="btn btn-small">Update</button>
                            </div>
                        </div>

                        <div id="ingredientsList" class="ingredients-list"></div>
                        
                        <details class="translated-section">
                            <summary>Show in Regional Language</summary>
                            <div id="translatedIngredients" class="translated-text"></div>
                        </details>
                    </div>

                    <!-- Instructions Section -->
                    <div class="recipe-section">
                        <h2 class="section-heading">
                            <span class="section-icon">📝</span>
                            Instructions
                        </h2>
                        <div id="instructionsList" class="instructions-list"></div>
                        
                        <details class="translated-section">
                            <summary>Show in Regional Language</summary>
                            <div id="translatedInstructions" class="translated-text"></div>
                        </details>
                    </div>

                    <!-- Additional Info -->
                    <div class="recipe-section">
                        <h2 class="section-heading">
                            <span class="section-icon">ℹ️</span>
                            Cooking Times
                        </h2>
                        <div class="time-breakdown">
                            <div class="time-item">
                                <span class="time-label">Prep Time:</span>
                                <span id="prepTime" class="time-value">-</span>
                            </div>
                            <div class="time-item">
                                <span class="time-label">Cook Time:</span>
                                <span id="cookTime" class="time-value">-</span>
                            </div>
                            <div class="time-item">
                                <span class="time-label">Total Time:</span>
                                <span id="totalTime" class="time-value">-</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="recipe-actions">
                    <button onclick="window.print()" class="btn btn-secondary">
                        🖨️ Print Recipe
                    </button>
                    <button id="shareBtn" class="btn btn-secondary">
                        📤 Share
                    </button>
                    <button onclick="window.location.href='filter.html'" class="btn btn-primary">
                        🔍 Find Another Recipe
                    </button>
                </div>
            </div>

            <!-- Error Message -->
            <div id="errorMessage" class="error-message" style="display: none;">
                <h2>😕 Recipe Not Found</h2>
                <p>We couldn't find this recipe. It may have been removed or the link is incorrect.</p>
                <a href="filter.html" class="btn btn-primary">Find Another Recipe</a>
            </div>
        </main>
    </div>

    <script src="js/recipe.js"></script>
</body>
</html>