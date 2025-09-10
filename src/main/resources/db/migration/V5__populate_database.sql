-- 1. Insert categories
INSERT INTO categories (name) VALUES
('Fruits'),
('Vegetables'),
('Dairy'),
('Bakery'),
('Beverages');

-- 2. Insert products
INSERT INTO products (name, price, description, category_id) VALUES
('Bananas', 1.20, 'Fresh organic bananas from local farms.', 1),
('Apples', 2.50, 'Crisp red apples, perfect for snacking.', 1),
('Carrots', 1.00, 'Fresh carrots, rich in vitamin A.', 2),
('Broccoli', 1.80, 'Green broccoli, perfect for steaming or stir-fry.', 2),
('Milk', 3.00, 'Whole milk from grass-fed cows, 1L.', 3),
('Cheddar Cheese', 4.50, 'Aged cheddar cheese, 200g block.', 3),
('Whole Wheat Bread', 2.00, 'Freshly baked whole wheat bread loaf.', 4),
('Croissant', 1.75, 'Buttery, flaky croissant, perfect for breakfast.', 4),
('Orange Juice', 3.25, 'Freshly squeezed orange juice, 1L.', 5),
('Green Tea', 2.80, 'Organic green tea bags, 20-count box.', 5);