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