-- Create the database
CREATE DATABASE IF NOT EXISTS wistoria;
USE wistoria;

-- =========================================
-- USERS TABLE
-- Stores user information (both regular and admin)
-- =========================================
CREATE TABLE Users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  phone_number VARCHAR(15),
  password VARCHAR(255) NOT NULL, -- Should be hashed in application
  role ENUM('user', 'admin') NOT NULL DEFAULT 'user',
  is_deleted BOOLEAN DEFAULT FALSE
);

-- =========================================
-- PRODUCTS TABLE
-- Stores product (watch) details
-- =========================================
CREATE TABLE Products (
  product_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  stock_quantity INT NOT NULL,
  category VARCHAR(50) NOT NULL,
  image_url VARCHAR(255),
  case_material ENUM('Stainless Steel', 'Titanium', 'Plastic', 'Ceramic', 'Carbon Fiber') NOT NULL,
  movement_type ENUM('Automatic', 'Quartz', 'Mechanical', 'Solar', 'Smart') NOT NULL,
  brand VARCHAR(100),
  water_resistance_meters INT,
  warranty_months INT,
  is_deleted BOOLEAN DEFAULT FALSE
);

-- =========================================
-- SHIPPING INFO TABLE
-- Stores shipping details for orders
-- =========================================
CREATE TABLE ShippingInfo (
  shipping_id INT PRIMARY KEY AUTO_INCREMENT,
  address VARCHAR(255) NOT NULL,
  contact VARCHAR(15) NOT NULL,
  shipping_method ENUM('standard', 'express') NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE
);

-- =========================================
-- CART ITEMS TABLE
-- Stores items added to cart by users
-- =========================================
CREATE TABLE CartItems (
  cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  product_id INT,
  quantity INT NOT NULL,
  added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  is_deleted BOOLEAN DEFAULT FALSE,
  UNIQUE(user_id, product_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- =========================================
-- WISHLIST TABLE
-- Stores wishlist items added by users
-- =========================================
CREATE TABLE Wishlist (
  wishlist_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  product_id INT,
  added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  is_deleted BOOLEAN DEFAULT FALSE,
  UNIQUE(user_id, product_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- =========================================
-- ORDERS TABLE
-- Stores basic order data for each user
-- =========================================
CREATE TABLE Orders (
  order_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  total_amount DECIMAL(10,2) NOT NULL,
  status ENUM('pending', 'paid', 'shipped', 'cancelled') NOT NULL DEFAULT 'pending',
  is_deleted BOOLEAN DEFAULT FALSE,
  shipping_id INT UNIQUE,
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (shipping_id) REFERENCES ShippingInfo(shipping_id) ON DELETE SET NULL
);

-- =========================================
-- ORDER DETAILS TABLE
-- Stores product-level detail for each order
-- =========================================
CREATE TABLE OrderDetails (
  order_details_id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT,
  product_id INT,
  quantity INT NOT NULL,
  price DECIMAL(10,2) NOT NULL, -- price at time of order
  is_deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- =========================================
-- CONTACT MESSAGES TABLE
-- Stores contact form submissions from users or guests
-- =========================================
CREATE TABLE ContactMessages (
  message_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NULL, -- nullable for guest users
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  subject VARCHAR(150),
  message TEXT NOT NULL,
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL
);


-- =========================================
-- INSERT DEFAULT ADMIN USER
-- Replace password with a secure hash before deployment
-- =========================================
INSERT INTO Users (name, email, phone_number, password, role)
VALUES ('Admin', 'admin@gmail.com', '0000000000', 'admin', 'admin');
