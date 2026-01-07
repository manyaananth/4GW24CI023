// ============================================
// RUCHIKARR - FILTER PAGE JAVASCRIPT (PHP VERSION)
// ============================================

const API_BASE = 'http://localhost/Ruchikarr'; // Updated for Apache

let currentFilters = {
    region: 'All India',
    weather: 'Pleasant',
    time: 30,
    servings: 4
};

document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Ruchikarr Filter Page Loaded');
    initializeEventListeners();
});

function initializeEventListeners() {
    // Dropdown listeners
    document.getElementById('region').addEventListener('change', updateFilters);
    document.getElementById('weather').addEventListener('change', updateFilters);
    document.getElementById('time').addEventListener('change', updateFilters);
    document.getElementById('servings').addEventListener('change', updateFilters);
    
    // Button listeners
    document.getElementById('moodSpinBtn').addEventListener('click', selectMoodSpin);
    document.getElementById('ingredientSearchBtn').addEventListener('click', selectIngredientSearch);
    document.getElementById('searchBtn').addEventListener('click', performIngredientSearch);
}

function updateFilters() {
    currentFilters.region = document.getElementById('region').value;
    currentFilters.weather = document.getElementById('weather').value;
    currentFilters.time = parseInt(document.getElementById('time').value);
    currentFilters.servings = parseFloat(document.getElementById('servings').value);
}

function selectMoodSpin() {
    document.getElementById('moodSpinBtn').classList.add('active');
    document.getElementById('ingredientSearchBtn').classList.remove('active');
    document.getElementById('ingredientSection').style.display = 'none';
    performMoodSpin();
}

function selectIngredientSearch() {
    document.getElementById('ingredientSearchBtn').classList.add('active');
    document.getElementById('moodSpinBtn').classList.remove('active');
    document.getElementById('ingredientSection').style.display = 'block';
    hideResults();
}

// ============================================
// MOOD SPIN (Pulls from PHP)
// ============================================
function performMoodSpin() {
    updateFilters();
    showLoading();
    hideResults();
    
    // Backticks for template literals
    const url = `${API_BASE}/get_random_recipe.php?region=${encodeURIComponent(currentFilters.region)}&time=${currentFilters.time}`;
    
    fetch(url)
        .then(response => response.json())
        .then(recipes => {
            hideLoading();
            displayResults(recipes);
        })
        .catch(error => {
            console.error('❌ Error:', error);
            hideLoading();
        });
}

// ============================================
// DISPLAY RESULTS (FIXED FOR LONG NAMES & 0 MINS)
// ============================================
function displayResults(recipes) {
    const resultsSection = document.getElementById('resultsSection');
    const recipeList = document.getElementById('recipeList');
    const resultCount = document.getElementById('resultCount');
    
    resultCount.textContent = recipes.length;
    recipeList.innerHTML = '';
    
    if (recipes.length === 0) {
        recipeList.innerHTML = '<p>No recipes found. Try adjusting filters.</p>';
    } else {
        recipes.forEach(recipe => {
            const card = document.createElement('div');
            card.className = 'recipe-card';
            
            // 1. FIX LONG NAMES: If name > 50 chars, truncate it
            let displayTitle = recipe.recipeName || "Authentic Indian Recipe";
            if (displayTitle.length > 50) {
                displayTitle = displayTitle.substring(0, 47) + "...";
            }

            // 2. FIX 0 MINS/SERVINGS: Use labels instead of 0
            const timeLabel = (recipe.totalTimeMins > 0) ? `⏱️ ${recipe.totalTimeMins} mins` : "🍽️ Chef Pick";
            const servingLabel = (recipe.servings > 0) ? `🍽️ ${recipe.servings} Servings` : "✨ Fresh";

            card.innerHTML = `
                <h3 class="recipe-card-title" style="color: #e67e22; font-weight: bold; min-height: 50px;">
                    ${displayTitle}
                </h3>
                <div class="recipe-card-meta">
                    <span class="recipe-card-badge">${timeLabel}</span>
                    <span class="recipe-card-badge">${servingLabel}</span>
                </div>
                <button onclick="viewRecipe(${recipe.recipeId}, ${currentFilters.servings})" 
                        class="btn btn-primary recipe-card-btn" style="margin-top: 10px;">
                    View Full Recipe
                </button>
            `;
            recipeList.appendChild(card);
        });
    }
    
    resultsSection.style.display = 'block';
    resultsSection.scrollIntoView({ behavior: 'smooth' });
}

function performIngredientSearch() {
    // Implementation for ingredient search PHP...
}

function viewRecipe(recipeId, servings) {
    window.location.href = `recipe.php?id=${recipeId}&servings=${servings}`;
}

function showLoading() { document.getElementById('loadingIndicator').style.display = 'block'; }
function hideLoading() { document.getElementById('loadingIndicator').style.display = 'none'; }
function hideResults() { document.getElementById('resultsSection').style.display = 'none'; }