-- Use the wistoria database
USE wistoria;


-- =========================================
-- INSERT USERS (10 users + the admin already in the DB)
-- =========================================
INSERT INTO Users (name, email, phone_number, password, role) VALUES
('John Smith', 'john.smith@email.com', '5551234567', 'password123', 'user'),
('Emma Johnson', 'emma.j@email.com', '5552345678', 'securepass1', 'user'),
('Michael Brown', 'mbrown@email.com', '5553456789', 'brownie22', 'user'),
('Sophia Garcia', 'sophia.g@email.com', '5554567890', 'sophiapwd', 'user'),
('William Taylor', 'wtaylor@email.com', '5555678901', 'taylorbill', 'user'),
('Olivia Wilson', 'olivia.w@email.com', '5556789012', 'wilson123', 'user'),
('James Anderson', 'james.a@email.com', '5557890123', 'anderson456', 'user'),
('Ava Martinez', 'ava.m@email.com', '5558901234', 'avapwd789', 'user'),
('Alexander Lee', 'alex.lee@email.com', '5559012345', 'alexlee123', 'user'),
('Isabella Clark', 'isabella.c@email.com', '5550123456', 'clarkbella', 'user');

-- =========================================
-- INSERT PRODUCTS (20 watches)
-- =========================================
INSERT INTO Products (name, description, price, stock_quantity, category, image_url, case_material, movement_type, brand, water_resistance_meters, warranty_months) VALUES
('Chronos Elite', 'Luxury automatic chronograph with sapphire crystal', 1299.99, 15, 'Luxury', '/images/chronos-elite.jpg', 'Stainless Steel', 'Automatic', 'Wistoria', 100, 24),
('Aqua Diver Pro', 'Professional diving watch water resistant to 300m', 899.99, 20, 'Sport', '/images/aqua-diver.jpg', 'Titanium', 'Automatic', 'Wistoria', 300, 36),
('Classic Vintage', 'Elegant dress watch with leather strap', 599.99, 25, 'Dress', '/images/classic-vintage.jpg', 'Stainless Steel', 'Mechanical', 'Wistoria', 50, 24),
('Smart Pulse', 'Advanced smartwatch with heart rate monitor', 349.99, 50, 'Smart', '/images/smart-pulse.jpg', 'Titanium', 'Smart', 'Wistoria', 50, 12),
('Pilot Navigator', 'Aviation inspired chronograph with multiple timezones', 899.99, 15, 'Luxury', '/images/pilot-nav.jpg', 'Stainless Steel', 'Automatic', 'Wistoria', 100, 24),
('Solar Explorer', 'Eco-friendly solar powered with perpetual calendar', 429.99, 30, 'Casual', '/images/solar-explorer.jpg', 'Titanium', 'Solar', 'Wistoria', 100, 36),
('Minimalist Steel', 'Clean design with ultra-thin case', 299.99, 40, 'Dress', '/images/minimalist-steel.jpg', 'Stainless Steel', 'Quartz', 'Wistoria', 30, 12),
('Ocean Mariner', 'Robust sailing watch with tide indicator', 749.99, 25, 'Sport', '/images/ocean-mariner.jpg', 'Titanium', 'Automatic', 'Wistoria', 200, 24),
('Carbon Racer', 'Lightweight racing chronograph', 1099.99, 10, 'Sport', '/images/carbon-racer.jpg', 'Carbon Fiber', 'Automatic', 'Wistoria', 100, 24),
('Diamond Elite', 'Luxury dress watch with diamond indices', 1999.99, 5, 'Luxury', '/images/diamond-elite.jpg', 'Stainless Steel', 'Automatic', 'Wistoria', 50, 36),
('Urban Quartz', 'Everyday casual quartz watch', 199.99, 60, 'Casual', '/images/urban-quartz.jpg', 'Stainless Steel', 'Quartz', 'Wistoria', 50, 12),
('Ceramic Pro', 'Scratch-resistant ceramic case with sapphire crystal', 799.99, 20, 'Dress', '/images/ceramic-pro.jpg', 'Ceramic', 'Automatic', 'Wistoria', 50, 24),
('Adventure GMT', 'Dual time zone watch for travelers', 649.99, 25, 'Casual', '/images/adventure-gmt.jpg', 'Stainless Steel', 'Automatic', 'Wistoria', 100, 24),
('Fitness Tracker Plus', 'Advanced fitness tracking smartwatch', 249.99, 45, 'Smart', '/images/fitness-tracker.jpg', 'Plastic', 'Smart', 'Wistoria', 50, 12),
('Lunar Phase', 'Elegant watch with moonphase complication', 899.99, 15, 'Luxury', '/images/lunar-phase.jpg', 'Stainless Steel', 'Automatic', 'Wistoria', 50, 24),
('Diver Quartz', 'Affordable diving watch with rotating bezel', 299.99, 35, 'Sport', '/images/diver-quartz.jpg', 'Stainless Steel', 'Quartz', 'Wistoria', 200, 12),
('Carbon Sport', 'Ultralight sports watch with chronograph', 799.99, 20, 'Sport', '/images/carbon-sport.jpg', 'Carbon Fiber', 'Quartz', 'Wistoria', 100, 24),
('Classic Rose Gold', 'Elegant rose gold dress watch', 699.99, 20, 'Dress', '/images/rose-gold.jpg', 'Stainless Steel', 'Mechanical', 'Wistoria', 30, 24),
('Smart Connect', 'Connected smartwatch with smartphone notifications', 299.99, 50, 'Smart', '/images/smart-connect.jpg', 'Plastic', 'Smart', 'Wistoria', 30, 12),
('Titanium Chrono', 'Lightweight chronograph with titanium case', 899.99, 15, 'Luxury', '/images/titanium-chrono.jpg', 'Titanium', 'Automatic', 'Wistoria', 100, 36);

-- =========================================
-- INSERT CART ITEMS (8 items)
-- =========================================
INSERT INTO CartItems (user_id, product_id, quantity) VALUES
(2, 1, 1), -- John has Chronos Elite in cart
(3, 5, 1), -- Emma has Pilot Navigator in cart
(4, 10, 1), -- Michael has Diamond Elite in cart
(5, 15, 2), -- Sophia has 2 Lunar Phase watches in cart
(8, 3, 1), -- James has Classic Vintage in cart
(8, 7, 1), -- James also has Minimalist Steel in cart
(9, 14, 1), -- Ava has Fitness Tracker Plus in cart
(10, 18, 1); -- Alexander has Classic Rose Gold in cart

-- =========================================
-- INSERT WISHLIST ITEMS (10 items)
-- =========================================
INSERT INTO Wishlist (user_id, product_id) VALUES
(2, 9), -- John wishlist: Carbon Racer
(3, 10), -- Emma wishlist: Diamond Elite
(4, 2), -- Michael wishlist: Aqua Diver Pro
(5, 8), -- Sophia wishlist: Ocean Mariner
(6, 7), -- William wishlist: Minimalist Steel
(7, 15), -- Olivia wishlist: Lunar Phase
(8, 12), -- James wishlist: Ceramic Pro
(9, 18), -- Ava wishlist: Classic Rose Gold
(10, 6), -- Alexander wishlist: Solar Explorer
(11, 20); -- Isabella wishlist: Titanium Chrono

-- =========================================
-- INSERT SHIPPING INFO (8 records)
-- =========================================
INSERT INTO ShippingInfo (address, contact, shipping_method) VALUES
('123 Main St, New York, NY 10001', '5551234567', 'standard'),
('456 Oak Ave, Los Angeles, CA 90001', '5552345678', 'express'),
('789 Pine Rd, Chicago, IL 60007', '5553456789', 'standard'),
('321 Maple Dr, Houston, TX 77001', '5554567890', 'express'),
('654 Elm St, Miami, FL 33101', '5555678901', 'standard'),
('987 Cedar Ln, Seattle, WA 98101', '5556789012', 'express'),
('159 Birch Blvd, Boston, MA 02101', '5557890123', 'standard'),
('753 Spruce Way, Denver, CO 80201', '5559012345', 'express');

-- =========================================
-- INSERT ORDERS (8 orders)
-- =========================================
INSERT INTO Orders (user_id, total_amount, status, shipping_id) VALUES
(2, 1299.99, 'paid', 1), -- John ordered Chronos Elite
(3, 899.99, 'shipped', 2), -- Emma ordered Pilot Navigator
(4, 599.99, 'pending', 3), -- Michael ordered Classic Vintage
(5, 349.99, 'paid', 4), -- Sophia ordered Smart Pulse
(7, 429.99, 'shipped', 5), -- Olivia ordered Solar Explorer
(8, 1099.99, 'paid', 6), -- James ordered Carbon Racer
(9, 249.99, 'pending', 7), -- Ava ordered Fitness Tracker Plus
(10, 699.99, 'cancelled', 8); -- Alexander ordered Classic Rose Gold but cancelled

-- =========================================
-- INSERT ORDER DETAILS (10 records - some orders have multiple items)
-- =========================================
INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 1299.99), -- John's order: Chronos Elite
(2, 5, 1, 899.99), -- Emma's order: Pilot Navigator
(3, 3, 1, 599.99), -- Michael's order: Classic Vintage
(4, 4, 1, 349.99), -- Sophia's order: Smart Pulse
(5, 6, 1, 429.99), -- Olivia's order: Solar Explorer
(6, 9, 1, 1099.99), -- James's order: Carbon Racer
(7, 14, 1, 249.99), -- Ava's order: Fitness Tracker Plus
(8, 18, 1, 699.99), -- Alexander's cancelled order: Classic Rose Gold
(6, 7, 1, 299.99), -- James also ordered Minimalist Steel
(5, 11, 2, 399.98); -- Olivia also ordered 2 Urban Quartz

-- =========================================
-- INSERT CONTACT MESSAGES (8 messages)
-- =========================================
INSERT INTO ContactMessages (user_id, name, email, subject, message) VALUES
(2, 'John Smith', 'john.smith@email.com', 'Order Inquiry', 'When can I expect my order #1 to be shipped?'),
(3, 'Emma Johnson', 'emma.j@email.com', 'Product Question', 'Does the Pilot Navigator watch have interchangeable straps?'),
(NULL, 'Robert Davis', 'robert.davis@email.com', 'Return Policy', 'What is your return policy for unworn watches?'),
(5, 'Sophia Garcia', 'sophia.g@email.com', 'Warranty Information', 'How do I register my watch for the warranty?'),
(NULL, 'David Wilson', 'david.w@email.com', 'International Shipping', 'Do you ship to Canada and what are the fees?'),
(8, 'James Anderson', 'james.a@email.com', 'Battery Replacement', 'How often should I replace the battery in my Minimalist Steel watch?'),
(NULL, 'Jennifer Lopez', 'jennifer.l@email.com', 'Wholesale Inquiry', 'Do you offer wholesale pricing for retailers?'),
(11, 'Isabella Clark', 'isabella.c@email.com', 'Product Suggestion', 'Have you considered making watches with ceramic bezels?');