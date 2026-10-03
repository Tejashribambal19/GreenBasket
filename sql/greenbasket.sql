CREATE DATABASE IF NOT EXISTS greenbasket;
USE greenbasket;

CREATE TABLE IF NOT EXISTS userreg (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  mob VARCHAR(20),
  gender VARCHAR(30),
  address VARCHAR(500),
  city VARCHAR(100),
  state VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS farmers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  mob VARCHAR(20),
  gender VARCHAR(30),
  address VARCHAR(500),
  city VARCHAR(100),
  state VARCHAR(100),
  farm_name VARCHAR(180) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  icon VARCHAR(20) DEFAULT '🌿'
);

INSERT IGNORE INTO categories(name,icon) VALUES
('Vegetables','🥬'),('Fruits','🍎'),('Grains & Rice','🌾'),('Pulses & Beans','🫘'),('Spices & Herbs','🌿'),('Dairy & Eggs','🥛'),('Nuts & Seeds','🥜'),('Oils & Ghee','🫙'),('Honey & Natural Sweeteners','🍯');

CREATE TABLE IF NOT EXISTS products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  farmer_id INT NOT NULL,
  category_id INT NOT NULL,
  name VARCHAR(140) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  unit VARCHAR(30) NOT NULL DEFAULT 'kg',
  image_url VARCHAR(500),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX(farmer_id), INDEX(category_id)
);

CREATE TABLE IF NOT EXISTS farmer_verification (
  farmer_id INT PRIMARY KEY,
  certificate_no VARCHAR(120),
  note VARCHAR(500),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'PLACED',
  delivery_address VARCHAR(500) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX(user_id)
);

CREATE TABLE IF NOT EXISTS order_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  product_name VARCHAR(140) NOT NULL,
  quantity INT NOT NULL,
  price DECIMAL(10,2) NOT NULL
);


CREATE TABLE IF NOT EXISTS feedback (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  order_id INT NOT NULL,
  order_item_id INT NOT NULL,
  product_id INT NOT NULL,
  rating INT NOT NULL,
  feedback_type VARCHAR(20) NOT NULL DEFAULT 'REVIEW',
  comment VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_feedback_user_item(user_id, order_item_id),
  INDEX(product_id), INDEX(order_id), INDEX(user_id)
);

CREATE TABLE IF NOT EXISTS app_settings (
  setting_key VARCHAR(100) PRIMARY KEY,
  setting_value VARCHAR(255),
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- GreenBasket v4 note: buyer feedback/complaints are stored in the feedback table.
-- Demo catalog note:
-- The application automatically creates the showcase farmers, buyer account,
-- approved verification records and 36 Indian demo products with INR pricing on first run.
-- Existing records are not deleted or overwritten.
-- For an existing database, the Java application automatically adds farmers.status if missing.
