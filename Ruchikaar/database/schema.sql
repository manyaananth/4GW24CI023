-- ============================================
-- RUCHIKARR DATABASE SCHEMA
-- Step 2: Create All Tables
-- ============================================

USE ruchikarr_db;

-- ============================================
-- Table 1: RECIPES TABLE (Main table for 6000+ dishes)
-- ============================================
CREATE TABLE recipes (
    recipe_id INT PRIMARY KEY AUTO_INCREMENT,
    sr_no INT,
    recipe_name VARCHAR(300) NOT NULL,
    translated_recipe_name VARCHAR(300),
    ingredients TEXT,
    translated_ingredients TEXT,
    prep_time_mins INT DEFAULT 0,
    cook_time_mins INT DEFAULT 0,
    total_time_mins INT DEFAULT 0,
    servings DECIMAL(4,2) DEFAULT 4.0,
    cuisine VARCHAR(100),
    course VARCHAR(100),
    diet VARCHAR(100),
    instructions TEXT,
    translated_instructions TEXT,
    url VARCHAR(500),
    region VARCHAR(100),
    weather_suitable VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_region (region),
    INDEX idx_weather (weather_suitable),
    INDEX idx_time (total_time_mins),
    INDEX idx_cuisine (cuisine),
    INDEX idx_course (course)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================
-- Table 2: USERS TABLE
-- ============================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================
-- Table 3: USER FAVORITES
-- ============================================
CREATE TABLE user_favorites (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    UNIQUE KEY unique_favorite (user_id, recipe_id),
    INDEX idx_user_favorites (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================
-- Table 4: USER HISTORY (Search & View History)
-- ============================================
CREATE TABLE user_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    search_type VARCHAR(50),
    region_filter VARCHAR(100),
    weather_filter VARCHAR(50),
    time_filter INT,
    servings_requested DECIMAL(4,2),
    ingredients_searched TEXT,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    INDEX idx_user_history (user_id),
    INDEX idx_viewed_at (viewed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================
-- Table 5: USER PREFERENCES
-- ============================================
CREATE TABLE user_preferences (
    preference_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    default_region VARCHAR(100) DEFAULT 'All India',
    default_weather VARCHAR(50) DEFAULT 'Pleasant',
    default_servings DECIMAL(4,2) DEFAULT 4.0,
    dietary_restrictions TEXT,
    favorite_cuisine VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================
-- SAMPLE TEST DATA (Optional - for testing)
-- ============================================

-- Sample Users
INSERT INTO users (username, email, password, full_name, phone) VALUES
('test_user', 'test@ruchikarr.com', 'password123', 'Test User', '9876543210'),
('demo_user', 'demo@ruchikarr.com', 'demo123', 'Demo User', '9876543211');

-- Sample Recipes (will be replaced by your 6000 recipes from CSV)
INSERT INTO recipes (sr_no, recipe_name, translated_recipe_name, ingredients, translated_ingredients, 
                     prep_time_mins, cook_time_mins, total_time_mins, servings, cuisine, course, diet, 
                     instructions, translated_instructions, url, region, weather_suitable)
VALUES 
(1, 'Mysuru Masala Dosa', 'ಮೈಸೂರು ಮಸಾಲಾ ದೋಸೆ', 
 '500 ml dosa batter, 2 medium onions, 10 leaves curry leaves, 3 tbsp ghee, 4 medium potatoes, 1 tsp mustard seeds, 0.5 tsp turmeric powder, 2 whole green chilies',
 'Dosa batter, Onions, Curry leaves, Ghee, Potatoes, Mustard seeds, Turmeric, Green chilies',
 10, 20, 30, 4, 'South Indian', 'Breakfast', 'Vegetarian',
 'Boil and mash potatoes coarsely. Heat ghee, add mustard seeds and curry leaves. Add chopped onions and green chilies, sauté until golden. Add turmeric and mashed potatoes, mix well. Heat dosa tawa, spread batter thin and crisp. Place potato masala in center, fold and serve with chutney.',
 'ಆಲೂಗಡ್ಡೆ ಕುದಿಸಿ ಮ್ಯಾಶ್ ಮಾಡಿ. ತುಪ್ಪ ಬಿಸಿ ಮಾಡಿ...', 
 'http://example.com/mysuru-masala-dosa', 'Karnataka', 'Hot'),
 
(2, 'Rajasthani Dal Baati Churma', 'राजस्थानी दाल बाटी चूरमा', 
 '2 cups wheat flour, 1 cup urad dal, 1 cup toor dal, 1 cup ghee, 2 tbsp sugar, spices mix',
 'Wheat flour, Urad dal, Toor dal, Ghee, Sugar, Spices',
 30, 45, 75, 6, 'Rajasthani', 'Main Course', 'Vegetarian',
 'Make baati dough with wheat flour and ghee. Shape into balls and bake until golden. Cook dal with spices. Crush baked baatis and mix with ghee and sugar for churma. Serve hot dal with baatis and churma.',
 'गेहूं के आटे से बाटी बनाएं...', 
 'http://example.com/dal-baati-churma', 'Rajasthan', 'Cold'),

(3, 'Bengali Fish Curry', 'মাছের ঝোল', 
 '500g rohu fish, 2 potatoes, 2 tomatoes, mustard oil, panch phoron, turmeric, red chili powder, ginger paste, green chilies',
 'Rohu fish, Potatoes, Tomatoes, Mustard oil, Spices',
 15, 25, 40, 4, 'Bengali', 'Main Course', 'Non-Vegetarian',
 'Marinate fish with turmeric and salt. Fry fish pieces lightly. In same oil, add panch phoron. Add potatoes and tomatoes. Add spices and water. Add fried fish. Simmer until gravy thickens.',
 'মাছ হলুদ-নুন দিয়ে মাখান...', 
 'http://example.com/bengali-fish-curry', 'West Bengal', 'Pleasant'),

(4, 'Mumbai Pav Bhaji', 'मुंबई पाव भाजी', 
 '4 potatoes, 1 cup green peas, 2 tomatoes, 2 onions, pav bhaji masala, butter, 8 pav buns',
 'Potatoes, Green peas, Tomatoes, Onions, Pav bhaji masala, Butter, Pav',
 15, 20, 35, 4, 'Maharashtrian', 'Street Food', 'Vegetarian',
 'Boil and mash vegetables. Heat butter, add onions and tomatoes. Add pav bhaji masala. Mix in mashed vegetables. Cook until well combined. Toast pav with butter. Serve hot with lemon and onions.',
 'सब्जियां उबालकर मैश करें...', 
 'http://example.com/pav-bhaji', 'Maharashtra', 'Rainy');

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Check if tables are created
SHOW TABLES;

-- Check recipes table structure
DESCRIBE recipes;

-- Count sample recipes
SELECT COUNT(*) as total_recipes FROM recipes;

-- View sample recipes
SELECT recipe_id, recipe_name, cuisine, region, weather_suitable FROM recipes LIMIT 5;