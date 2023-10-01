CREATE TABLE IF NOT EXISTS Employee(
  employeeID SERIAL PRIMARY KEY,
  isManager BOOLEAN,
  firstName VARCHAR(20),
  lastName VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Ingredient(
  ingredientID SERIAL PRIMARY KEY,
  ingredientName VARCHAR(50),
  quantity INT,
  cost FLOAT
);

CREATE TABLE IF NOT EXISTS Product(
  productID SERIAL PRIMARY KEY,
  ingredientIDs VARCHAR(50)[], -- FK with ingredientIDs
  price FLOAT
);

CREATE TABLE IF NOT EXISTS Order(
  orderID SERIAL PRIMARY KEY,
  cashier VARCHAR(50), -- employeeID of employee
  transactionID TIMESTAMP,
  products VARCHAR(50)[], -- FK with productIDs
  price FLOAT
);

CREATE TABLE IF NOT EXISTS Finances(
  reportDate DATE PRIMARY KEY,
  revenue FLOAT,
  profit FLOAT,
  expenses FLOAT,
  orders VARCHAR(50)[] -- FK with orderIDs
);

-- list in stock ingredients 
SELECT ingredientName
FROM Ingredient
WHERE quantity > 0;

-- list low stock ingredients
SELECT ingredientName
FROM Ingredient
WHERE quantity < 10;

-- see most profitable 3 days
SELECT dateId, profit
FROM Finances
ORDER BY profit DESC
LIMIT 3;


CREATE TABLE IF NOT EXISTS Orders(
  orderID SERIAL PRIMARY KEY,
  cashier VARCHAR(50), -- employeeID of employee
  transactionTime TIMESTAMP,
  price FLOAT
);

CREATE TABLE IF NOT EXISTS OrderProducts(
  orderID INT,
  productID VARCHAR(50),
  PRIMARY KEY (orderID, productID)
);

ALTER TABLE OrderProducts ADD CONSTRAINT fk_order 
  FOREIGN KEY (orderID) REFERENCES Orders(orderID);

-- Assuming you have a Products table:
ALTER TABLE OrderProducts ADD CONSTRAINT fk_product 
  FOREIGN KEY (productID) REFERENCES Products(productID
