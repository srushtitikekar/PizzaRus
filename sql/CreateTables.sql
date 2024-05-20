-- CreateTables.sql
CREATE SCHEMA PizzaRUs;

use PizzaRUs;

Drop schema PizzaRUs;

create table base (
	BaseID INTEGER AUTO_INCREMENT PRIMARY KEY,
    BaseSize varchar(10) not null,
    BaseType VARCHAR(15) not null,
    BasePrice decimal(5,2) not null,
    BaseCost decimal(5,2) not null
);


create table pizza (
	PizzaID INTEGER AUTO_INCREMENT PRIMARY KEY,
    PizzaBaseID integer not null,
    PizzaStatus varchar(10) NOT NULL,
    PizzaPriceToBiz decimal(5,2) not null,
    PizzaPriceToCust decimal(5,2) not null,
    PizzaDiscountPrice decimal(5,2) default null,
    FOREIGN KEY (PizzaBaseID) REFERENCES base(BaseID)
);


create table topping(
	ToppingID INTEGER AUTO_INCREMENT PRIMARY KEY,
    ToppingName varchar(20) not null,
    ToppingCostToBiz decimal(5,2) not null,
    ToppingCostToCust decimal(5,2) not null ,
    ToppingCurrInvLevel integer not null,
    ToppingMinInvLevel integer not null,
    ToppingSizeAmount decimal(5,2) default null,
    ToppingQTYSmall decimal(3,2) not null ,
    ToppingQTYMedium decimal(3,2)  not null,
    ToppingQTYLarge decimal(3,2)  not null,
    ToppingQTYXLarge decimal(3,2)  not null
);


create table toppings(
	ToppingsID integer not null,
    ToppingsPizzaID integer not null,
    ToppingsExtraTopping bool default 0,
    primary key(ToppingsID, ToppingsPizzaID,ToppingsExtraTopping), 
    FOREIGN KEY (ToppingsID) REFERENCES topping(ToppingID),
    foreign key (ToppingsPizzaID) references pizza(PizzaID)
);


create table discount (
	DiscountID integer auto_increment primary key,
    DiscountName varchar(20) not null,
    DiscountPercentage decimal(5,2) DEFAULT NULL,
    DiscountPrice decimal(5,2) DEFAULT NULL,
    DiscountAppliedType varchar(15) DEFAULT NULL
);


create table discpizza (
	DiscpizzaPizzaID integer not null,
    DiscpizzaDiscountID integer not null,
    primary key (DiscpizzaPizzaID, DiscpizzaDiscountID),
    foreign key (DiscpizzaPizzaID) references pizza(PizzaID),
    foreign key (DiscpizzaDiscountID) references discount(DiscountID)
);


create table customer (
	CustomerID integer primary key,
    CustomerFname varchar(15) default null,
    CustomerMiddleInitial char(1) default null,
    CustomerLname varchar(15) default null,
    CustomerPhone varchar(13) default null
);


create table orders (
	OrderNum INTEGER AUTO_INCREMENT PRIMARY KEY,
    OrderPizzaID integer not null ,
    OrderCustID integer not null ,
    OrderDate date not null,
    OrderTime time not null,
    OrderCostToCust decimal(5,2) not null,
    OrderCostToBiz decimal(5,2) not null,
    OrderDiscount decimal(5,2) default null,
    OrderType varchar(10) not null,
    foreign key (OrderPizzaID) references pizza(PizzaID),
    foreign key (OrderCustID) references customer(CustomerID)
);


create table dinein (
	DineinOrderNum integer not null,
    DineinTableNum integer not null,
    primary key (DineinOrderNum),
    foreign key (DineinOrderNum) references orders(OrderNum)
);


create table pickup (
	PickupOrderNum integer not null,
    PickupCustID integer not null,
	primary key (PickupOrderNum),
	foreign key (PickupCustID) references customer(CustomerID),
    foreign key (PickupOrderNum) references orders(OrderNum)
);


create table delivery (
	DeliveryOrderNum integer not null,
    DeliveryCustID integer not null,
    DeliveryCharge decimal(3,2),
    DeliveryHouseNum integer not null,
    DeliveryStreetName varchar(25) not null,
    DeliveryCity varchar(20) not null,
    DeliveryZipCode varchar(5) not null,
    DeliveryState varchar(2) not null,
    DeliveryPhone varchar(13) not null,
    primary key (DeliveryOrderNum),
    foreign key (DeliveryCustID) references customer(CustomerID),
    foreign key (DeliveryOrderNum) references orders(OrderNum)
);


create table discorder (
	DiscorderOrderNum integer not null,
    DiscorderDiscID integer not null,
    primary key (DiscorderOrderNum, DiscorderDiscId),
    foreign key (DiscorderOrderNum) references orders(OrderNum),
    foreign key (DiscorderDiscID) references discount(DiscountID)
);