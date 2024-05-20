
insert into topping (
ToppingName,
ToppingCostToCust,
ToppingCostToBiz,
ToppingCurrInvLevel,
ToppingMinInvLevel,
ToppingQTYSmall,
ToppingQTYMedium,
ToppingQTYLarge,
ToppingQTYXLarge)
VALUES
('Pepperoni', 1.25, 0.2, 100, 50, 2, 2.75, 3.5, 4.5),
('Sausage', 1.25, 0.15, 100, 50, 2.5, 3, 3.5, 4.25),
('Ham', 1.5, 0.15, 78, 25, 2, 2.5, 3.25, 4)
,('Chicken', 1.75, 0.25, 56, 25, 1.5, 2, 2.25, 3)
,('Green Pepper', 0.5, 0.02, 79, 25, 1, 1.5, 2, 2.5)
,('Onion', 0.5, 0.02, 85, 25, 1, 1.5, 2, 2.75)
,('Roma Tomato', 0.75, 0.03, 86, 10, 2, 3, 3.5, 4.5)
,('Mushrooms', 0.75, 0.1, 52, 50, 1.5, 2, 2.5, 3)
,('Black Olives', 0.6, 0.1, 39, 25, 0.75, 1, 1.5, 2)
,('Pineapple', 1, 0.25, 15, 0, 1, 1.25, 1.75, 2)
,('Jalapenos', 0.5, 0.05, 64, 0, 0.5, 0.75, 1.25, 1.75)
,('Banana Peppers', 0.5, 0.05, 36, 0, 0.6, 1, 1.3, 1.75)
,('Regular Cheese', 0.5, 0.12, 250, 50, 2, 3.5, 5, 7)
,('Four Cheese Blend', 1, 0.15, 150, 25, 2, 3.5, 5, 7)
,('Feta Cheese', 1.5, 0.18, 75, 0, 1.75, 3, 4, 5.5)
,('Goat Cheese', 1.5, 0.2, 54, 0, 1.6, 2.75, 4, 5.5)
,('Bacon', 1.5, 0.25, 89, 0, 1, 1.5, 2, 3);

insert into discount (
DiscountName, 
DiscountPercentage,
DiscountPrice)
VALUES
('Employee',15.00,NULL),
('Lunch Special Medium',NULL, 1.00),
('Lunch Special Large',NULL,2.00),
('Specialty Pizza',NULL,1.50),
('Happy Hour',10.00,NULL),
('Gameday Special',20.00,NULL);

insert into base(
BaseType, 
BaseSize,
BasePrice,
BaseCost)
VALUES
('Thin','Small',3,0.5),
('Original','Small',3,0.75),
('Pan','Small',3.5,1),
('Gluten-Free','Small',4,2),
('Thin','Medium',5,1),
('Original','Medium',5,1.5),
('Pan','Medium',6,2.25),
('Gluten-Free','Medium',6.25,3),
('Thin','Large',8,1.25),
('Original','Large',8,2),
('Pan','Large',9,3),
('Gluten-Free','Large',9.5,4),
('Thin','XLarge',10,2),
('Original','XLarge',10,3),
('Pan','XLarge',11.5,4.5),
('Gluten-Free','XLarge',12.5,6);



## 1st 

INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(9,'Completed',3.68,20.75,18.75);

INSERT INTO toppings
VALUES
(13,1,TRUE),
(13,1,FALSE),
(1,1,FALSE),
(2,1,FALSE);

INSERT INTO discpizza
VALUES
(1,2);

INSERT INTO customer(CustomerID)
VALUES (1);

INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(1,1,'2023-03-05','12:03:00',18.75,3.68,null,'dinein');

INSERT INTO dinein 
VALUES
(1,21);

# 2nd
INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(7,'Completed',3.23,12.85,9.35);

INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(2,'Completed',1.40,6.93,null);

INSERT INTO toppings
VALUES
(15,2,FALSE),
(9,2,FALSE),
(7,2,FALSE),
(8,2,FALSE),
(12,2,FALSE),
(13,3,FALSE),
(12,3,FALSE),
(4,3,false);



INSERT INTO discpizza
VALUES
(2,2),
(2,4);

INSERT INTO customer(CustomerID)
VALUES (2);

INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(2,2,'2023-04-03','12:05:00',9.35,3.23,null,'dinein'),
(3,2,'2023-04-03','12:05:00',16.28,4.63,null,'dinein');

INSERT INTO dinein 
VALUES
(2,4);

-- 3 order

-- this pizza was inserted 6 times
INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(10,'Completed',3.30,14.88,null),
(10,'Completed',3.30,14.88,null),
(10,'Completed',3.30,14.88,null),
(10,'Completed',3.30,14.88,null),
(10,'Completed',3.30,14.88,null),
(10,'Completed',3.30,14.88,null);



INSERT INTO toppings
VALUES
(13,4,FALSE),
(1,4,False),
(1,5,FALSE),
(13,5,false),
(1,6,FALSE),
(13,6,false),
(1,7,FALSE),
(13,7,false),
(1,8,FALSE),
(13,8,false),
(1,9,FALSE),
(13,9,false);


INSERT INTO customer(CustomerID, CustomerFname, CustomerLname, CustomerPhone)
VALUES (3,'Andrew', 'Wilkes-Krier', '864-254-5861');

INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(4,3,'2023-03-03','21:30:00',14.88,3.30,null,'pickup'),
(5,3,'2023-03-03','21:30:00',14.88,3.30,null,'pickup'),
(6,3,'2023-03-03','21:30:00',14.88,3.30,null,'pickup'),
(7,3,'2023-03-03','21:30:00',14.88,3.30,null,'pickup'),
(8,3,'2023-03-03','21:30:00',14.88,3.30,null,'pickup'),
(9,3,'2023-03-03','21:30:00',14.88,3.30,null,'pickup');

insert into pickup
values
(4,3),
(5,3),
(6,3),
(7,3),
(8,3),
(9,3);

-- 4th order

INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(14,'Completed',9.19,27.94,null),
(14,'Completed',6.25,31.50,30.00),
(14, 'Completed',8.18,26.75,null);

INSERT INTO toppings
VALUES
(1,10,FALSE),
(2,10,False),
(14,10,false),
(3,11,true),
(10,11,true),
(3,11,false),
(10,11,false),
(14,11,false),
(4,12,false),
(17,12,false),
(14,12,false);


INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(10,3,'2023-04-20','19:11:00',27.94,9.19,20.00,'delivery'),
(11,3,'2023-04-20','19:11:00',30.00,6.25,20.00,'delivery'),
(12,3,'2023-04-20','19:11:00',26.75,8.18,20.00,'delivery');

insert into discpizza
values
(11,4);

insert into discorder
values
(10,6),
(11,6),
(12,6);

insert into delivery 
(DeliveryOrderNum,DeliveryCustID, DeliveryHouseNum, DeliveryStreetname, DeliveryCity,DeliveryZipCode, DeliveryState, DeliveryPhone)
values
(10,3, 115, 'Party Blvd','Anderson','29621','SC','864-254-5861'),
(11,3, 115, 'Party Blvd','Anderson','29621','SC','864-254-5861'),
(12,3, 115, 'Party Blvd','Anderson','29621','SC','864-254-5861');

-- 5th order

INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(16,'Completed',7.88, 27.45,25.95);

INSERT INTO toppings
VALUES
(9,13,false),
(5,13,false),
(6,13,false),
(7,13,false),
(8,13,false),
(16,13,false);

insert into discpizza
values
(13,4);

INSERT INTO customer(CustomerID, CustomerFname, CustomerLname, CustomerPhone)
VALUES (4,'Matt', 'Engers', '864-474-9953');

INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(13,4,'2023-03-02','17:30:00',25.95,7.88,null,'pickup');

insert into pickup
values
(13,4);

-- 6th order

INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(9,'Completed',4.24, 25.81,null);

INSERT INTO toppings
VALUES
(4,14,false),
(5,14,false),
(6,14,false),
(8,14,false),
(14,14,true),
(14,14,false);

INSERT INTO customer(CustomerID, CustomerFname, CustomerLname, CustomerPhone)
VALUES (5,'Frank', 'Turner', '864-232-8944');

INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(14,5,'2023-03-02','18:17:00',25.81,4.24,null,'delivery');

insert into delivery 
(DeliveryOrderNum,DeliveryCustID, DeliveryHouseNum, DeliveryStreetname, DeliveryCity,DeliveryZipCode, DeliveryState, DeliveryPhone)
values
(14,5, 6745, 'Wessex St','Anderson','29621','SC','864-232-8944');

-- 7th order

INSERT INTO pizza(PizzaBaseID,PizzaStatus,PriceToBiz,PriceToCust,PizzaDiscountPrice)
VALUES
(9,'Completed',2.75, 18.00,null),
(9,'Completed',3.25, 19.25,null);

INSERT INTO toppings
VALUES
(14,15,true),
(14,15,false),
(1,16,true),
(1,16,false),
(13,16,false);

INSERT INTO customer(CustomerID, CustomerFname, CustomerLname, CustomerPhone)
VALUES (6,'Milo', 'Auckerman', '864-878-5679');

INSERT INTO orders(OrderPizzaID,OrderCustID,OrderDate,OrderTime,OrderCostToCust,OrderCostToBiz,OrderDiscount,OrderType)
VALUES
(15,6,'2023-04-13','20:32:00',18.00,2.75,15,'delivery'),
(16,6,'2023-04-13','20:32:00',19.25,3.25,15,'delivery');

insert into discorder
values
(15,1),
(16,1);

insert into delivery 
(DeliveryOrderNum,DeliveryCustID, DeliveryHouseNum, DeliveryStreetname, DeliveryCity,DeliveryZipCode, DeliveryState, DeliveryPhone)
values
(15,6, 8879, 'Suburban Home','Anderson','29621','SC','864-878-5679'),
(16,6, 8879, 'Suburban Home','Anderson','29621','SC','864-878-5679');

commit;

