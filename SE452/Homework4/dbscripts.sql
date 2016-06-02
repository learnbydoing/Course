/***********************************************************
* Create the database named gamestore, its tables
************************************************************/

DROP DATABASE IF EXISTS gamestore;

CREATE DATABASE gamestore;

USE gamestore;

CREATE TABLE SalesOrder (
  OrderId INT NOT NULL AUTO_INCREMENT,
  UserName VARCHAR(50),
  Address VARCHAR(50),
  CreditCard VARCHAR(50),
  ConfirmationNumber VARCHAR(50),
  DeliveryDate DATETIME,
  
  PRIMARY KEY(OrderID) 
);

CREATE TABLE OrderItem (
  OrderItemId INT NOT NULL AUTO_INCREMENT,
  OrderId INT,
  ProductId VARCHAR(50),
  ProductName VARCHAR(50),
  ProductType INT,
  Price Double(10,2),
  Image VARCHAR(50),
  Maker VARCHAR(50),
  Discount INT,
  Quantity INT,
  
  PRIMARY KEY(OrderItemID) 
);