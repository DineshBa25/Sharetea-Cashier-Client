CREATE TABLE IF NOT EXISTS Employees(
  employeeID SERIAL PRIMARY KEY,
  isManager BOOLEAN,
  firstName VARCHAR(20),
  lastName VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Ingredients(
  ingredientID SERIAL PRIMARY KEY,
  ingredientName VARCHAR(50),
  quantity INT,
  cost FLOAT
);

CREATE TABLE IF NOT EXISTS Products(
  productID SERIAL PRIMARY KEY,
  productName VARCHAR(200),
  ingredientIDs INTEGER[], -- FK with ingredientIDs
  price FLOAT,
  saleprice FLOAT
);

CREATE TABLE IF NOT EXISTS Finances(
  reportDate DATE PRIMARY KEY,
  revenue FLOAT,
  profit FLOAT,
  expenses FLOAT,
  ordercount INT
);

CREATE TABLE IF NOT EXISTS Orders(
  orderID SERIAL PRIMARY KEY,
  cashier VARCHAR(50), -- employeeID of employee
  transactionTime TIMESTAMP,
  price FLOAT
);

CREATE TABLE IF NOT EXISTS OrderProducts(
  orderID INT,
  productID SERIAL,
  PRIMARY KEY (orderID, productID)
);

ALTER TABLE OrderProducts ADD CONSTRAINT fk_order 
  FOREIGN KEY (orderID) REFERENCES Orders(orderID);

ALTER TABLE OrderProducts ADD CONSTRAINT fk_product 
  FOREIGN KEY (productID) REFERENCES Products(productID);

-- MISC Queries, to show functionality:
-- list in stock ingredients - Query 1
SELECT ingredientName, quantity
FROM Ingredients
WHERE quantity > 0;

-- list low stock ingredients - Query 2
SELECT ingredientName, quantity
FROM Ingredients
WHERE quantity < 10;

-- sorting ingredients by price from high to low - Query 3
SELECT *
FROM ingredients
ORDER BY price cost;

-- see least profitable 3 days - Query 4
SELECT
    DATE(transactiontime) AS day,
    SUM(price) AS total_revenue
FROM orders
GROUP BY day
ORDER BY total_revenue ASC
LIMIT 3;

-- list Employees currently in database - Query 5
SELECT firstName, lastName
FROM Employees;


-- list Employees last names sorted alphabetically - Query 6
SELECT lastName
FROM Employees
ORDER BY lastName ASC;

-- Show orders by a certain cashier on a certain day - Query 7
SELECT
    orderid,
    transactiontime,
    price
FROM orders
WHERE cashier = 'Nicholas'
    AND DATE(transactiontime) = '2023-11-24';

-- Calculate sales per week - Query 8
SELECT 
    DATE_TRUNC('week', transactiontime) AS week_start,
    SUM(price) AS total_sales
FROM 
    orders
GROUP BY 
    week_start
ORDER BY 
    week_start;
    
-- Sort products alphabetically - Query 9
SELECT *
FROM products
ORDER BY productname;

-- Top 3 weeks in terms of order count - Query 10
SELECT 
    DATE_TRUNC('week', transactiontime) AS week_start,
    COUNT(*) AS order_count
FROM 
    orders
GROUP BY 
    week_start
ORDER BY 
    order_count DESC
LIMIT 3;

-- sorting products by price from high to low - Query 11
SELECT *
FROM products
ORDER BY price DESC;

-- REQUIRED QUERIES
-- 1. 52 Weeks of Sales History 
SELECT 
    DATE_TRUNC('week', transactiontime) AS week_start,
    COUNT(*) AS order_count
FROM 
    orders
GROUP BY 
    week_start
ORDER BY 
    week_start;

-- 2. Realistic Sales History
SELECT
    EXTRACT(HOUR FROM transactiontime) AS hour_of_day,
    SUM(price) AS total_revenue
FROM orders
WHERE DATE(transactiontime) = '2023-10-03' -- the date to analyze
GROUP BY hour_of_day
ORDER BY hour_of_day;

-- 3. 2 Peak Days
SELECT
    DATE(transactiontime) AS day,
    SUM(price) AS total_revenue
FROM orders
GROUP BY day
ORDER BY total_revenue DESC
LIMIT 10;

-- 4. 20 Items in Invetory
SELECT COUNT(*) AS row_count
FROM ingredients;

GRANT ALL PRIVILEGES ON Employees, Ingredients, Products, Finances, Orders, OrderProducts TO csce331_970_mohsin, csce331_970_dineshb, csce331_970_ia601612
, csce331_970_gpresent, csce331_970_nicholasdienstbier;
