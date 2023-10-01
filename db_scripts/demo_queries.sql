CREATE TABLE IF NOT EXISTS Employee(
  employeeID INT PRIMARY KEY,
  isManager BOOLEAN,
  firstName VARCHAR(20),
  lastName VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Ingredient(
  ingredientID INT PRIMARY KEY,
  ingredientName VARCHAR(50),
  quantity INT,
  cost FLOAT
);

CREATE TABLE IF NOT EXISTS Product(
  productID INT PRIMARY KEY,
  ingredientIDs VARCHAR(50)[], -- FK with ingredientIDs
  price FLOAT
);

CREATE TABLE IF NOT EXISTS Order(
  orderID INT PRIMARY KEY,
  cashier VARCHAR(50), -- employeeID of employee
  transactionID DATETIME,
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

-- see least profitable 3 days
SELECT dateId, profit,
FROM Finances
ORDER BY profit ASC
LIMIT 3;

-- see recent 5 orders from a specific cashier
SELECT o.orderId, o.dateTime, p.productName, o.price
FROM Order o
JOIN Product p ON o.product = p.productId
WHERE o.cashier = {{cashier_id}}
ORDER BY o.dateTime DESC
LIMIT 3;