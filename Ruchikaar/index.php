<?php include 'db_connect.php'; ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ruchikarr - AI Indian Recipe Generator</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <div class="logo">
                <span class="logo-icon">👨‍🍳</span>
                <h1>Ruchikarr</h1>
            </div>
            <p class="tagline">Your Personalized Indian Recipe Companion</p>
        </header>

        <main class="main-content">
            <div class="hero-section">
                <h2 class="hero-title">Discover Perfect Indian Recipes</h2>
                <p class="hero-subtitle">
                    Get personalized recipe recommendations based on weather, 
                    your available time, and ingredients you have!
                </p>
            </div>

            <div class="features">
                <div class="feature-card">
                    <div class="feature-icon">🌦️</div>
                    <h3>Weather-Based</h3>
                    <p>Recipes that match today's weather and your mood</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">⏱️</div>
                    <h3>Time-Aware</h3>
                    <p>Find recipes based on how much time you have</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">🥘</div>
                    <h3>Ingredient Search</h3>
                    <p>Use what you have in your kitchen right now</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">🗺️</div>
                    <h3>Regional Cuisine</h3>
                    <p>Explore authentic dishes from across India</p>
                </div>
            </div>

            <div class="cta-section">
                <a href="filter.php" class="btn btn-primary btn-large">
                    🚀 Start Cooking Journey
                </a>
                <p class="recipe-count">
                    Choose from <strong>1,300+ authentic Indian recipes</strong>
                </p>
            </div>
        </main>

        <footer class="footer">
            <p>&copy; 2026 Ruchikarr | Made with ❤️ for Indian Food Lovers</p>
        </footer>
    </div>
</body>
</html>