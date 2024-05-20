create view ToppingPopularity as 
select topping.ToppingName as Topping, count(toppings.ToppingsID) as ToppingCount
from topping left join toppings on topping.ToppingID = toppings.ToppingsID
group by ToppingID
order by ToppingCount desc;

select * from ToppingPopularity;

select base.BaseSize as Size, base.BaseType as Crust, sum(pizza.PizzaPriceToCust-pizza.PizzaPriceToBiz) as Profit, max(DATE_FORMAT(orders.OrderDate, '%m/%Y')) as 'Order Month'
from base join pizza on
base.BaseID = pizza.PizzaBaseID
join orders on 
pizza.PizzaID = orders.OrderPizzaID
group by Size, Crust 
order by Profit;


