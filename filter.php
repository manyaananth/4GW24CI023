<?php include 'db_connect.php'; ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Find Your Recipe - Ruchikarr</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <div class="logo">
                <span class="logo-icon">👨‍🍳</span>
                <h1>Ruchikarr</h1>
            </div>
            <a href="index.php" class="back-link">← Back to Home</a>
        </header>

        <main class="main-content">
            <div class="filter-section">
                <h2 class="section-title">🎯 Generate Your Perfect Recipe</h2>
                <p class="section-subtitle">
                    Tell us about your cooking situation and we'll find the perfect dish
                </p>

                <form id="filterForm" class="filter-form">
                    <div class="form-group">
                        <label for="region">
                            <span class="label-icon">🗺️</span>
                            Choose Region
                        </label>
                        <select id="region" name="region" class="form-control" required>
                            <option value="All India">All India</option>
                            <option value="South India">South India</option>
                            <option value="North India">North India</option>
                            <option value="East India">East India</option>
                            <option value="West India">West India</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="weather">
                            <span class="label-icon">🌦️</span>
                            What's the weather like?
                        </label>
                        <select id="weather" name="weather" class="form-control" required>
                            <option value="Pleasant">Pleasant</option>
                            <option value="Hot">Hot & Sunny</option>
                            <option value="Cold">Cold</option>
                            <option value="Rainy">Rainy</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="time">
                            <span class="label-icon">⏱️</span>
                            How much time do you have?
                        </label>
                        <select id="time" name="time" class="form-control" required>
                            <option value="15">Quick (15 mins)</option>
                            <option value="30" selected>Medium (30 mins)</option>
                            <option value="45">Standard (45 mins)</option>
                            <option value="60">Relaxed (60 mins)</option>
                            <option value="120">Chef Mode (2+ hours)</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="servings">
                            <span class="label-icon">🍽️</span>
                            Number of Servings
                        </label>
                        <input type="number" id="servings" name="servings" 
                               class="form-control" min="0.5" max="20" step="0.5" 
                               value="4" required>
                    </div>

                    <div class="form-group">
                        <label class="label-main">
                            <span class="label-icon">🎲</span>
                            Choose Your Method
                        </label>
                        <div class="method-selection">
                            <button type="button" id="moodSpinBtn" class="method-btn method-btn-spin">
                                <span class="method-icon">🎡</span>
                                <span class="method-title">Mood Spin</span>
                                <span class="method-desc">Surprise me with a random recipe!</span>
                            </button>
                            <button type="button" id="ingredientSearchBtn" class="method-btn method-btn-search">
                                <span class="method-icon">🔍</span>
                                <span class="method-title">Ingredient Search</span>
                                <span class="method-desc">I have specific ingredients</span>
                            </button>
                        </div>
                    </div>

                    <div id="ingredientSection" class="form-group" style="display: none;">
                        <label for="ingredients">
                            <span class="label-icon">🥘</span>
                            Enter Your Ingredients
                        </label>
                        <input type="text" id="ingredients" name="ingredients" 
                               class="form-control" 
                               placeholder="e.g., potato, onion, rice">
                        <button type="button" id="searchBtn" class="btn btn-primary">
                            🔍 Search Recipes
                        </button>
                    </div>
                </form>

                <div id="loadingIndicator" class="loading" style="display: none;">
                    <div class="spinner"></div>
                    <p>Finding perfect recipes for you...</p>
                </div>

                <div id="resultsSection" style="display: none;">
                    <h3 class="results-title">✨ Found <span id="resultCount">0</span> Recipes</h3>
                    <div id="recipeList" class="recipe-grid"></div>
                </div>
            </div>
        </main>
    </div>

    <script src="js/filter.js"></script>
</body>
</html>