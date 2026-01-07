// ============================================
// RUCHIKARR - RECIPE DETAIL PAGE JAVASCRIPT (PHP VERSION)
// Handles recipe display and serving adjustments
// ============================================

// API Base URL - Updated for Apache
const API_BASE = 'http://localhost/Ruchikarr';

// Global variables
let currentRecipe = null;
let originalServings = 4;
let requestedServings = 4;

// ============================================
// INITIALIZATION
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Recipe Detail Page Loaded');
    
    // Get recipe ID and servings from URL
    const urlParams = new URLSearchParams(window.location.search);
    const recipeId = urlParams.get('id');
    const servings = urlParams.get('servings');
    
    if (!recipeId) {
        showError();
        return;
    }
    
    if (servings) {
        requestedServings = parseFloat(servings);
    }
    
    // Load recipe data from PHP backend
    loadRecipe(recipeId);
    initializeEventListeners();
});

function initializeEventListeners() {
    document.getElementById('decreaseServings')?.addEventListener('click', decreaseServings);
    document.getElementById('increaseServings')?.addEventListener('click', increaseServings);
    document.getElementById('updateServings')?.addEventListener('click', updateServings);
}

// ============================================
// LOAD RECIPE (Talking to PHP)
// ============================================
function loadRecipe(recipeId) {
    showLoading();
    
    // Pointing to your specific recipe detail PHP file
    const url = `${API_BASE}/get_recipe_details.php?id=${recipeId}`;
    
    console.log('📡 Fetching recipe details:', url);
    
    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Recipe not found');
            return response.json();
        })
        .then(recipe => {
            console.log('✅ Recipe loaded:', recipe);
            currentRecipe = recipe;
            originalServings = recipe.servings || 4;
            displayRecipe(recipe);
            hideLoading();
        })
        .catch(error => {
            console.error('❌ Error loading recipe:', error);
            hideLoading();
            showError();
        });
}

// ============================================
// DISPLAY RECIPE
// ============================================
function displayRecipe(recipe) {
    // Mapping to your database column names
    document.getElementById('recipeName').textContent = recipe.recipeName || 'Unnamed Recipe';
    document.getElementById('recipeRegion').textContent = recipe.region || 'All India';
    document.getElementById('recipeTime').textContent = recipe.totalTimeMins || 0;
    document.getElementById('recipeServings').textContent = requestedServings || recipe.servings || 4;
    
    // Time breakdown
    document.getElementById('prepTime').textContent = (recipe.prepTimeMins || 0) + ' mins';
    document.getElementById('cookTime').textContent = (recipe.cookTimeMins || 0) + ' mins';
    
    // Display Ingredients and Instructions
    const ingredientsList = document.getElementById('ingredientsList');
    ingredientsList.innerHTML = `<p style="white-space: pre-line;">${recipe.ingredients || 'No ingredients listed'}</p>`;
    
    const instructionsList = document.getElementById('instructionsList');
    instructionsList.innerHTML = `<p style="white-space: pre-line;">${recipe.instructions || 'No instructions listed'}</p>`;
    
    document.getElementById('recipeDetail').style.display = 'block';
}

// ============================================
// UI HELPERS
// ============================================
function showLoading() {
    const loader = document.getElementById('loadingRecipe');
    if (loader) loader.style.display = 'block';
    document.getElementById('recipeDetail').style.display = 'none';
}

function hideLoading() {
    const loader = document.getElementById('loadingRecipe');
    if (loader) loader.style.display = 'none';
}

function showError() {
    const errorMsg = document.getElementById('errorMessage');
    if (errorMsg) errorMsg.style.display = 'block';
}

// Simple Servings Logic
function decreaseServings() {
    const input = document.getElementById('servingsInput');
    if (input && input.value > 1) input.value = parseInt(input.value) - 1;
}

function increaseServings() {
    const input = document.getElementById('servingsInput');
    if (input) input.value = parseInt(input.value) + 1;
}

function updateServings() {
    const newServings = document.getElementById('servingsInput').value;
    window.location.href = `recipe.php?id=${currentRecipe.recipeId}&servings=${newServings}`;
}