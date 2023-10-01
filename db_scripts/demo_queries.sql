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

CREATE TABLE IF NOT EXISTS tempProducts(
  productID SERIAL PRIMARY KEY,
  ingredientIDs VARCHAR(50)[], -- FK with ingredientIDs
  price FLOAT
);

CREATE TABLE IF NOT EXISTS Finances(
  reportDate DATE PRIMARY KEY,
  revenue FLOAT,
  profit FLOAT,
  expenses FLOAT,
  orders VARCHAR(50)[] -- FK with orderIDs
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
  FOREIGN KEY (productID) REFERENCES tempProducts(productID);

-- MISC Queries, to show functionality:
-- list in stock ingredients 
SELECT ingredientName
FROM Ingredients
WHERE quantity > 0;

-- list low stock ingredients
SELECT ingredientName
FROM Ingredients
WHERE quantity < 10;

-- see most profitable 3 days
SELECT reportDate, profit
FROM Finances
ORDER BY profit DESC
LIMIT 3;

-- see least profitable 3 days
SELECT reportDate, profit
FROM Finances
ORDER BY profit ASC
LIMIT 3;

-- list Employees currently in database
SELECT firstName, lastName
FROM Employees;

GRANT ALL PRIVILEGES ON Employees, Ingredients, tempProducts, Finances, Orders, OrderProducts TO csce331_970_mohsin, csce331_970_dineshb, csce331_970_ia601612
, csce331_970_gpresent, csce331_970_nicholasdienstbier;
