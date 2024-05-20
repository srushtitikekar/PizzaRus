import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";



	
	static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			//System.out.println((conn));
			return true;
		} catch (SQLException e) {
			System.out.println("SQL Exception");
			return false;
		} catch (IOException e) {
			System.out.println("IOExpection");
			return false;
		}

	}

	
	public static void addOrder(Order o) throws SQLException, IOException 
	{
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 * 
		 */
		String query1 = "INSERT INTO orders values (?,?,?,?,?,?,?,?,?,?)";
		String query2 = "SELECT PizzaPriceToBiz, PizzaPriceToCust from pizza where PizzaID= ?";
		Double price_to_biz=0.0;
		Double price_to_cust=0.0;
		try{
			PreparedStatement statement = conn.prepareStatement(query2);
			statement.setInt(1,o.getOrderID());
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				price_to_biz = rs.getDouble("PizzaPriceToBiz");
				price_to_cust = rs.getDouble("PizzaPriceToCust");
			}

			if(o.getOrderType().equals("dinein")){
				Customer lastc = getCustomerList().getLast();
				//o.setCustID();
				o.setCustID(0);
			}
			PreparedStatement statement1 = conn.prepareStatement(query1);
			statement1.setInt(1,o.getOrderID());
			statement1.setInt(2,o.getOrderID());
			statement1.setInt(3,o.getCustID());
			statement1.setString(4,java.time.LocalDate.now().toString());
			statement1.setString(5,java.time.LocalTime.now().toString());
			statement1.setDouble(6,price_to_cust);
			statement1.setDouble(7,price_to_biz);
			statement1.setDouble(8,0.0);
			statement1.setString(9,o.getOrderType());
			statement1.setInt(10,o.getIsComplete());
			statement1.executeUpdate();

			if(o.getOrderType().equals("pickup")){

				String query = "Insert into pickup values (?,?)";
				PreparedStatement statement2 = conn.prepareStatement(query);
				statement2.setInt(1, o.getOrderID());
				statement2.setInt(2,o.getCustID());
				//statement2.setInt(3, ((PickupOrder) o).getIsPickedUp());
				statement2.executeUpdate();
			}
			if(o.getOrderType().equals("delivery")){

				String query = "Insert into delivery(DeliveryOrderNum,DeliveryCustID) values (?,?)";
				PreparedStatement statement2 = conn.prepareStatement(query);
				statement2.setInt(1, o.getOrderID());
				System.out.println(o.getOrderID());
				System.out.println(o.getCustID());
				statement2.setInt(2,o.getCustID());
				//statement2.setInt(3,);
				//statement2.setInt(3, ((PickupOrder) o).getIsPickedUp());
				statement2.executeUpdate();
			}

		}catch (SQLException e){
			e.printStackTrace();
		}
	

		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addPizza(Pizza p) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts and toppings associated with the pizza,
		 * there are other methods below that may help with that process.
		 * 
		 */
		String query = "INSERT INTO pizza(PizzaID,PizzaBaseID, PizzaStatus,PizzaPriceToBiz,PizzaPriceToCust) values (?,?,?,?,?)";
		String query2 = "SELECT BaseID from base where BaseSize = ? and BaseType= ?";

		try{
			PreparedStatement statement = conn.prepareStatement(query);
			PreparedStatement statement1 = conn.prepareStatement(query2);
			statement1.setString(1,p.getSize());
			System.out.println(p.getSize());
			statement1.setString(2,p.getCrustType());
			System.out.println(p.getCrustType());
			ResultSet rs = statement1.executeQuery();
			Integer baseid=null;
			while(rs.next()){
				baseid = rs.getInt("BaseID");
			}

			statement.setInt(1,p.getPizzaID());
			statement.setInt(2,baseid);
			statement.setString(3,p.getPizzaState());
			statement.setDouble(4,p.getBusPrice());
			statement.setDouble(5,p.getCustPrice());
			statement.executeUpdate();


		}catch (SQLException e){
			e.printStackTrace();
		}
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	
	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this method will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This method should do 2 two things.
		 * - update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * - connect the topping to the pizza
		 *   What that means will be specific to your yimplementatinon.
		 * 
		 * Ideally, you should't let toppings go negative....but this should be dealt with BEFORE calling this method.
		 * 
		 */
		String query = "INSERT INTO toppings values (?,?,?)";
		String query1 = "SELECT PizzaPriceToBiz,PizzaPriceToCust from pizza where PizzaID = ?";
		String query2 = "UPDATE pizza set PizzaPriceToBiz = ? , PizzaPriceToCust = ? WHERE PizzaID = ?";
		Double price_to_biz=0.0;
		Double price_to_cust=0.0;
		//System.out.println(t);
		boolean canadd=false;
		try{

			if(p.getSize()=="Small"){
				Double ToppingQTYSmall=0.0;
				Double ToppingCurrInvLevel=0.0;
				Double ToppingMinInvLevel=0.0;
				Statement getSmall = conn.createStatement();
				ResultSet rs1 = getSmall.executeQuery("Select ToppingQTYSmall, ToppingCurrInvLevel, ToppingMinInvLevel from topping where ToppingID="+t.getTopID());
				while(rs1.next()){
					ToppingQTYSmall=rs1.getDouble("ToppingQTYSmall");
					ToppingCurrInvLevel=rs1.getDouble("ToppingCurrInvLevel");
					ToppingMinInvLevel=rs1.getDouble("ToppingMinInvLevel");

				}
				Statement updateInventory = conn.createStatement();
				Double curr_inv = ToppingCurrInvLevel-ToppingQTYSmall;
				if(curr_inv>ToppingMinInvLevel){
					canadd=true;
					updateInventory.execute("UPDATE topping set ToppingCurrInvLevel="+curr_inv+" where ToppingID="+t.getTopID());
				}
				else{
					System.out.println("Not enough to add to pizza");
				}


			}else if(p.getSize()=="Medium"){
				Double ToppingQTYSMedium=0.0;
				Double ToppingCurrInvLevel=0.0;
				Double ToppingMinInvLevel=0.0;
				Statement getSmall = conn.createStatement();
				ResultSet rs1 = getSmall.executeQuery("Select ToppingQTYMedium, ToppingCurrInvLevel, ToppingMinInvLevel from topping where ToppingID="+t.getTopID());
				while(rs1.next()){
					ToppingQTYSMedium=rs1.getDouble("ToppingQTYMedium");
					ToppingCurrInvLevel=rs1.getDouble("ToppingCurrInvLevel");
					ToppingMinInvLevel=rs1.getDouble("ToppingMinInvLevel");

				}
				Statement updateInventory = conn.createStatement();
				Double curr_inv = ToppingCurrInvLevel-ToppingQTYSMedium;
				if(curr_inv>ToppingMinInvLevel){
					canadd=true;
					updateInventory.execute("UPDATE topping set ToppingCurrInvLevel="+curr_inv+" where ToppingID="+t.getTopID());
				}
				else{
					System.out.println("Not enough to add to pizza");
				}

			}else if(p.getSize()=="Large"){

				Double ToppingQTYLarge=0.0;
				Double ToppingCurrInvLevel=0.0;
				Double ToppingMinInvLevel=0.0;
				Statement getSmall = conn.createStatement();
				ResultSet rs1 = getSmall.executeQuery("Select ToppingQTYLarge, ToppingCurrInvLevel, ToppingMinInvLevel from topping where ToppingID="+t.getTopID());
				while(rs1.next()){
					ToppingQTYLarge=rs1.getDouble("ToppingQTYLarge");
					ToppingCurrInvLevel=rs1.getDouble("ToppingCurrInvLevel");
					ToppingMinInvLevel=rs1.getDouble("ToppingMinInvLevel");

				}
				Statement updateInventory = conn.createStatement();
				Double curr_inv = ToppingCurrInvLevel-ToppingQTYLarge;
				if(curr_inv>ToppingMinInvLevel){
					canadd=true;
					updateInventory.execute("UPDATE topping set ToppingCurrInvLevel="+curr_inv+" where ToppingID="+t.getTopID());
				}
				else{
					System.out.println("Not enough to add to pizza");
				}

			}else if(p.getSize()=="XLarge"){


				Double ToppingQTYXLarge=0.0;
				Double ToppingCurrInvLevel=0.0;
				Double ToppingMinInvLevel=0.0;
				Statement getSmall = conn.createStatement();
				ResultSet rs1 = getSmall.executeQuery("Select ToppingQTYXLarge, ToppingCurrInvLevel, ToppingMinInvLevel from topping where ToppingID="+t.getTopID());
				while(rs1.next()){
					ToppingQTYXLarge=rs1.getDouble("ToppingQTYXLarge");
					ToppingCurrInvLevel=rs1.getDouble("ToppingCurrInvLevel");
					ToppingMinInvLevel=rs1.getDouble("ToppingMinInvLevel");

				}
				Statement updateInventory = conn.createStatement();
				Double curr_inv = ToppingCurrInvLevel-ToppingQTYXLarge;
				if(curr_inv>ToppingMinInvLevel){
					canadd=true;
					updateInventory.execute("UPDATE topping set ToppingCurrInvLevel="+curr_inv+" where ToppingID="+t.getTopID());
				}
				else{
					System.out.println("Not enough to add to pizza");
				}

			}







			if(canadd){
				PreparedStatement statement = conn.prepareStatement(query);
				statement.setInt(1,t.getTopID());
				statement.setInt(2,p.getPizzaID());
				statement.setBoolean(3,isDoubled);

				statement.executeUpdate();
				System.out.println("Insert Done");

				PreparedStatement statement1 = conn.prepareStatement(query1);
				statement1.setInt(1,p.getPizzaID());
				ResultSet rs = statement1.executeQuery();
				while(rs.next()){
					price_to_biz=rs.getDouble("PizzaPriceToBiz");
					price_to_cust = rs.getDouble("PizzaPriceToCust");
					//System.out.println("Rs:Biz Price:"+price_to_biz+"Cust Price:"+price_to_cust);
				}
				//System.out.println("Biz Price:"+price_to_biz+"Cust Price:"+price_to_cust);
				price_to_biz = price_to_biz + t.getBusPrice();
				price_to_cust = price_to_cust + t.getCustPrice();


				PreparedStatement statement2 = conn.prepareStatement(query2);
				statement2.setDouble(1,price_to_biz);
				statement2.setDouble(2,price_to_cust);
				statement2.setInt(3,p.getPizzaID());
				statement2.executeUpdate();}
			else {
				System.out.println("Not enough to add to pizza");
			}




		}catch ( SQLException e){
			e.printStackTrace();
		}

		

		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	
	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * This method connects a discount with a Pizza in the database.
		 * 
		 * What that means will be specific to your implementatinon.
		 */
		String query = "INSERT INTO discpizza values (?,?)";
		String query1 = "SELECT PizzaPriceToCust FROM pizza where PizzaID = ?";
		String query2 = "UPDATE pizza set PizzaDiscountPrice = ? WHERE PizzaID = ?";
		Double pizza_cust_price = 0.0;

		try{
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1,p.getPizzaID());
			statement.setInt(2,d.getDiscountID());
			//statement.setBoolean(3,isDoubled);

			statement.executeUpdate();
			System.out.println("Insert Done");

			PreparedStatement statement1 = conn.prepareStatement(query1);
			statement1.setInt(1,p.getPizzaID());
			ResultSet rs = statement1.executeQuery();
			while(rs.next()){
				pizza_cust_price = rs.getDouble("PizzaPriceToCust");
			}

			if(d.isPercent()){
				pizza_cust_price = pizza_cust_price * (1 - (d.getAmount()/100));
			}
			else{
				pizza_cust_price = pizza_cust_price - d.getAmount();
			}

			PreparedStatement statement2 = conn.prepareStatement(query2);
			statement2.setDouble(1,pizza_cust_price);
			statement2.setInt(2,p.getPizzaID());
			statement2.executeUpdate();


		}catch ( SQLException e){
			e.printStackTrace();
		}


		//conn.close();

		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * This method connects a discount with an order in the database
		 * 
		 * You might use this, you might not depending on where / how to want to update
		 * this information in the dabast
		 */
		String query = "INSERT INTO discorder values (?,?)";
		String query1 = "SELECT OrderCostToCust FROM orders where OrderNum = ?";
		String query2 = "UPDATE orders set OrderDiscount = ? WHERE OrderNum = ?";
		Double order_cust_price = 0.0;

		try{
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1,o.getOrderID());
			statement.setInt(2,d.getDiscountID());
			//statement.setBoolean(3,isDoubled);

			statement.executeUpdate();
			System.out.println("Insert Done");

			PreparedStatement statement1 = conn.prepareStatement(query1);
			statement1.setInt(1,o.getOrderID());
			ResultSet rs = statement1.executeQuery();
			while(rs.next()){
				order_cust_price = rs.getDouble("OrderCostToCust");
			}

			if(d.isPercent()){
				order_cust_price = order_cust_price * (1 - (d.getAmount()/100));
			}
			else{
				order_cust_price = order_cust_price - d.getAmount();
			}

			PreparedStatement statement2 = conn.prepareStatement(query2);
			statement2.setDouble(1,order_cust_price);
			statement2.setInt(2,o.getOrderID());
			statement2.executeUpdate();


		}catch ( SQLException e){
			e.printStackTrace();
		}
		conn.close();
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addCustomer(Customer c) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This method adds a new customer to the database.
		 * 
		 */
		String insertCustomer = "INSERT INTO customer (CustomerID,CustomerFname, CustomerLname,CustomerPhone,CustomerAddress)"+" values (?,?,?,?,?)";

		//System.out.println(c.toString());
		//System.out.println(c.getAddress());

		try{
			PreparedStatement statement = conn.prepareStatement(insertCustomer);
			Statement statement2 = conn.createStatement();
			Integer last_cust_num = null;
			ResultSet rs = statement2.executeQuery("select max(CustomerID) from customer");
			while(rs.next()){
				last_cust_num = rs.getInt("max(CustomerID)");
			}
			//System.out.println(last_cust_num);
			c.setCustID(last_cust_num+1);
			statement.setInt(1,c.getCustID());
			//System.out.println(c.getCustID());
			statement.setString(2,c.getFName());
			//System.out.println(c.getFName());
			statement.setString(3,c.getLName());
			//System.out.println(c.getLName());
			statement.setString(4,c.getPhone());
			//System.out.println(c.getPhone());
			statement.setString(5,c.getAddress());
			//System.out.println(c.getAddress());
			//System.out.println("SQL Query: " + insertCustomer);
			statement.executeUpdate();

			//System.out.println("Added Customer");
		} catch (SQLException e) {
			e.printStackTrace();
		}
			//return false;


			//conn.close();


			//DO NOT FORGET TO CLOSE YOUR CONNECTION
		}

	public static void completeOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Find the specifed order in the database and mark that order as complete in the database.
		 * 
		 */
		try {
			connect_to_db();

			if (o instanceof PickupOrder) {
				/* REVISIT */
				((PickupOrder) o).setIsPickedUp(1);
				String updatePizzaStatement = "update orders set OrdersStatus = true where OrderNum = " + o.getOrderID() + ";";
				PreparedStatement pizzaPreparedStatement = conn.prepareStatement(updatePizzaStatement);
				pizzaPreparedStatement.executeUpdate();
			}
			String updatePizzaStatement = "update orders set OrdersStatus = true where OrderNum = " + o.getOrderID() + ";";

			PreparedStatement pizzaPreparedStatement = conn.prepareStatement(updatePizzaStatement);

			pizzaPreparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}










		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static ArrayList<Order> getOrders(boolean openOnly) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */

		ArrayList<Order> orders = new ArrayList<Order>();

		if (openOnly) {
			try (Statement statement = conn.createStatement();

				 ResultSet resultSet = statement.executeQuery("select OrderNum, OrderCustID, OrderDate, OrderCostToCust, OrderCostToBiz, OrderType, OrdersStatus " +
						 "from orders join customer on orders.OrderCustID = customer.CustomerID where OrdersStatus = 0")) {

				while (resultSet.next()) {
					Integer OrderID = resultSet.getInt("OrderNum");
					Integer CustID = resultSet.getInt("OrderCustID");
					String OrderType = resultSet.getString("OrderType");
					String date = resultSet.getString("OrderDate");
					Double CustPrice =resultSet.getDouble("OrderCostToCust");
					Double BusPrice =resultSet.getDouble("OrderCostToBiz");
					Integer isComplete =resultSet.getInt("OrdersStatus");



					orders.add(new Order(OrderID, CustID, OrderType,date,CustPrice,BusPrice,isComplete));

				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			try (Statement statement = conn.createStatement();

				 ResultSet resultSet = statement.executeQuery("select OrderNum, OrderCustID, OrderDate, OrderCostToCust, OrderCostToBiz, OrderType, OrdersStatus " +
						 "from orders join customer on orders.OrderCustID = customer.CustomerID")) {

				while (resultSet.next()) {
					Integer OrderID = resultSet.getInt("OrderNum");
					Integer CustID = resultSet.getInt("OrderCustID");
					String OrderType = resultSet.getString("OrderType");
					String date = resultSet.getString("OrderDate");
					Double CustPrice =resultSet.getDouble("OrderCostToCust");
					Double BusPrice =resultSet.getDouble("OrderCostToBiz");
					Integer isComplete =resultSet.getInt("OrdersStatus");



					orders.add(new Order(OrderID, CustID, OrderType,date,CustPrice,BusPrice,isComplete));

				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return orders;

		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		//return null;
	}
	
	public static Order getLastOrder(){
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there should ALWAYS be a "last order"!
		 */

		Order lastOrder = null;
		try {
			connect_to_db();
			Statement statement = conn.createStatement();
			String query = "SELECT * FROM orders ORDER BY OrderNum DESC LIMIT 1";
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				//System.out.println("In if");
				Integer OrderID = resultSet.getInt("OrderNum");
				Integer Order_pizzaid = resultSet.getInt("OrderPizzaID");
				Integer CustID = resultSet.getInt("OrderCustID");
				String OrderType = resultSet.getString("OrderType");
				String date = resultSet.getString("OrderDate");
				String time = resultSet.getString("OrderTime");
				Double CustPrice =resultSet.getDouble("OrderCostToCust");
				Double BusPrice =resultSet.getDouble("OrderCostToBiz");
				Double order_disc = resultSet.getDouble("OrderDiscount");
				Integer isComplete =resultSet.getInt("OrdersStatus");
				//System.out.println(OrderID+":"+Order_pizzaid+":"+CustID+":"+OrderType+":"+date+":"+time+":"+CustPrice+":"+BusPrice+":"+order_disc+":"+isComplete);
				lastOrder = new Order(OrderID, CustID, OrderType,date,CustPrice,BusPrice,isComplete);
				//System.out.println("ending if");
			}
		}catch (SQLException e) {
			e.printStackTrace();

		}catch (IOException e){
			e.printStackTrace();
		}
		return lastOrder;
	}

	public static ArrayList<Order> getOrdersByDate(String date){
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *  
		 */
		ArrayList<Order> orders = new ArrayList<Order>();

		try {
			connect_to_db();

			String selectQuery = "select * from orders where (OrderDate = '" + date + " 00:00:00')" +
					" order by OrderNum desc";

			Statement statement = conn.createStatement();

			ResultSet resultSet = statement.executeQuery(selectQuery);

			while (resultSet.next()) {
				Integer OrderID = resultSet.getInt("OrderNum");
				Integer CustID = resultSet.getInt("OrderCustID");
				String OrderType = resultSet.getString("OrderType");
				String OrderTime = resultSet.getString("OrderDate");
				Double CustPrice =resultSet.getDouble("OrderCostToCust");
				Double BusPrice =resultSet.getDouble("OrderCostToBiz");
				Integer isComplete =resultSet.getInt("OrdersStatus");
				orders.add(new Order(OrderID, CustID, OrderType, OrderTime, CustPrice, BusPrice, isComplete));

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		finally {
//			conn.close();
//		}

		return orders;




		// return null;
	}
		
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database for all the available discounts and 
		 * return them in an arrayList of discounts.
		 * 
		*/
		ArrayList<Discount> discountList = new ArrayList<Discount>();

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT DiscountID, DiscountName,DiscountAmount, DiscountIsPercentage FROM discount;");

			while (rs.next()) {
				int disc_id = rs.getInt("DiscountID"); // Assuming "customer_id" is a column in your customer table
				String disc_name = rs.getString("DiscountName");
				Double disc_amt = rs.getDouble("DiscountAmount");

				Boolean dic_is_perc = rs.getBoolean("DiscountIsPercentage"); // Assuming "customer_name" is a column in your customer table
				// Add more columns as needed
				Discount disc = new Discount(disc_id,disc_name,disc_amt,dic_is_perc);
				discountList.add(disc);

				//Customer customer = new Customer(cust_id,cust_fname,cust_lname,cust_phone);
				//discountList.add(customer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} //finally {
			//if (conn != null) {
				//conn.close(); // Close the connection in the finally block
			//}
		//}

		return discountList;
		
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		//return null;
		//return null;
	}

	public static Discount findDiscountByName(String name){
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *  
		 */

		try{
			connect_to_db();

			Discount discount =null;
			String query = "select * from discount where DiscountName =" +name+";";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while(rs.next()) {
				int disc_id = rs.getInt("DiscountID");
				String disc_name = rs.getString("DiscountName");
				Double disc_amt = rs.getDouble("DiscountAmount");
				Boolean dic_is_perc = rs.getBoolean("DiscountIsPercentage");

				discount = new Discount(disc_id,disc_name,disc_amt,dic_is_perc);
				return discount;
			}
			//conn.close();
//
		}catch (SQLException | IOException e ){
			e.printStackTrace();
		}

		return null;




		// return null;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the data for all the customers and return an arrayList of all the customers. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		*/
		ArrayList<Customer> customerList = new ArrayList<>();

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM customer where CustomerFname is not null");

			while (rs.next()) {
				int cust_id = rs.getInt("CustomerID"); // Assuming "customer_id" is a column in your customer table
				String cust_fname = rs.getString("CustomerFname");
				String cust_lname = rs.getString("CustomerLname");

				String cust_phone = rs.getString("CustomerPhone"); // Assuming "customer_name" is a column in your customer table
				// Add more columns as needed

				Customer customer = new Customer(cust_id,cust_fname,cust_lname,cust_phone);
				customerList.add(customer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} /*finally {
			if (conn != null) {
				conn.close(); // Close the connection in the finally block
			}
		}*/

		return customerList;
	}

	public static Customer findCustomerByPhone(String phoneNumber){
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *  
		 */
		try{
			connect_to_db();
			Customer cust =null;
			String query = "select * from customer where CustomerPhone =" +phoneNumber +";";
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while(resultSet.next()) {
				int cust_id = resultSet.getInt("CustomerID"); // Assuming "customer_id" is a column in your customer table
				String cust_fname = resultSet.getString("CustomerFname");
				String cust_lname = resultSet.getString("CustomerLname");
				String cust_phone = resultSet.getString("CustomerPhone");

				cust = new Customer(cust_id, cust_fname, cust_lname, cust_phone);

			}
			if(cust != null) {
				return cust;

			}
			//conn.close();

		}catch (SQLException | IOException e ) {
			e.printStackTrace();
		}
		return null;




		 //return null;
	}


	public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database for the aviable toppings and 
		 * return an arrayList of all the available toppings. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */

		ArrayList<Topping> toppings = new ArrayList<Topping>();

		try {
			Statement statement = conn.createStatement();

			ResultSet rs  = statement.executeQuery( "SELECT * FROM topping");
			//System.out.printf("%-5s %-25s %-10s%n", "ID", "Topping", "CurrINVT");
			while(rs.next()){
				int topping_id = rs.getInt("ToppingID");
				String topping_name = rs.getString("ToppingName");
				Double perAMT = rs.getDouble("ToppingQTYSmall");
				Double medAMT = rs.getDouble("ToppingQTYMedium");
				Double largeAMT = rs.getDouble("ToppingQTYLarge");
				Double xlargeAMT = rs.getDouble("ToppingQTYXlarge");
				Double cost_toCust = rs.getDouble("ToppingCostToCust");
				Double cost_toBiz = rs.getDouble("ToppingCostToBiz");
				Integer min_inv = rs.getInt("ToppingMinInvLevel");
				Integer curr_inv = rs.getInt("ToppingCurrInvLevel");


				//int topping_inv = rs.getInt("ToppingCurrInvLevel");
				Topping t = new Topping(topping_id,topping_name,perAMT,medAMT,largeAMT,xlargeAMT,cost_toCust,cost_toBiz,min_inv,curr_inv);

				toppings.add(t);



			}

		}catch (SQLException e){
			e.printStackTrace();
		}



		//conn.close();



		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return toppings;
	}

	public static Topping findToppingByName(String name){
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *  
		 */
		try {
			connect_to_db();
			Topping top =null;
			String query = "select * from topping where ToppingName =" +name+";";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);

			while(rs.next()) {
				int topping_id = rs.getInt("ToppingID");
				String topping_name = rs.getString("ToppingName");
				Double perAMT = rs.getDouble("ToppingQTYSmall");
				Double medAMT = rs.getDouble("ToppingQTYMedium");
				Double largeAMT = rs.getDouble("ToppingQTYLarge");
				Double xlargeAMT = rs.getDouble("ToppingQTYXlarge");
				Double cost_toCust = rs.getDouble("ToppingCostToCust");
				Double cost_toBiz = rs.getDouble("ToppingCostToBiz");
				Integer min_inv = rs.getInt("ToppingMinInvLevel");
				Integer curr_inv = rs.getInt("ToppingCurrInvLevel");

				top = new Topping(topping_id, topping_name, perAMT, medAMT, largeAMT, xlargeAMT, cost_toCust, cost_toBiz, min_inv, curr_inv);

				return top;
			}
//
			//conn.close();
		}catch (IOException| SQLException e) {
			e.printStackTrace();
		}

		return null;




		 //return null;
	}


	public static void addToInventory(Topping t, double quantity) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 * 
		 * */
		String addInv = "update topping set ToppingCurrInvLevel= ? where ToppingID= ?";
		try{
			PreparedStatement statement = conn.prepareStatement(addInv);
			Statement statement1 = conn.createStatement();
			int topping_id = t.getTopID();

			ResultSet rs = statement1.executeQuery("SELECT ToppingCurrInvLevel from topping where ToppingID="+topping_id);
			while(rs.next()){
				int curInv = rs.getInt("ToppingCurrInvLevel");
				t.setCurINVT(curInv);
			}

			//ResultSet rs = statement.executeQuery("update topping set ToppingCurrInvLevel=100 where ToppingID="+ topping_id);
			//System.out.println(rs);
			int total = t.getCurINVT() + (int) quantity;
			statement.setInt(1,total);
			statement.setInt(2,t.getTopID());
			statement.executeUpdate();

		}catch (SQLException e){
				e.printStackTrace();
		}
		
		
		
		//conn.close();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database from the base customer price for that size and crust pizza.
		 * 
		*/
		Double baseBusPrice=null;
		try {
			PreparedStatement statement = conn.prepareStatement("select BasePrice from base where BaseSize = ? and BaseType= ?");
			statement.setString(1, size);
			statement.setString(2, crust);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				baseBusPrice = rs.getDouble("BasePrice");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return baseBusPrice;


		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		//return 0.0;
	}

	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database from the base business price for that size and crust pizza.
		 * 
		*/
		Double baseBusPrice=null;
		try {
			PreparedStatement statement = conn.prepareStatement("select BaseCost from base where BaseSize = ? and BaseType= ?");
			statement.setString(1, size);
			statement.setString(2, crust);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				baseBusPrice = rs.getDouble("BaseCost");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return baseBusPrice;
	}

	public static void printInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Queries the database and prints the current topping list with quantities.
		 *  
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */


		try {
			Statement statement = conn.createStatement();
			ResultSet rs  = statement.executeQuery( "SELECT ToppingID, ToppingName, ToppingCurrInvLevel FROM topping");
			System.out.printf("%-5s %-25s %-10s%n", "ID", "Topping", "CurrINVT");
			while(rs.next()){
				int topping_id = rs.getInt("ToppingID");
				String topping_name = rs.getString("ToppingName");
				int topping_inv = rs.getInt("ToppingCurrInvLevel");

				System.out.printf("%-5d %-25s %-10d%n", topping_id, topping_name, topping_inv);



			}

			}catch (SQLException e){
			e.printStackTrace();
		}

		
		
		//conn.close();
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION


	}
	
	public static void printToppingPopReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */


		try {
			connect_to_db();

			Statement statement = conn.createStatement();
			String query = "select * from ToppingPopularity;";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.printf("%-20s | %-4s |%n", "Topping", "ToppingCount");
			System.out.printf("-------------------------------------\n");
			while (resultSet.next()) {

				Integer count = resultSet.getInt("ToppingCount");
				String topping = resultSet.getString("Topping");
				System.out.printf("%-20s | %-4s |%n", topping, count);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}






		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */

		try {

			Statement statement = conn.createStatement();
			String query = "select * from ProfitByPizza;";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.printf("%-15s | %-15s | %-10s| %-30s%n", "Size", " Crust", "Profit", "Order Month");
			System.out.printf("-----------------------------------------------------------------\n");
			while (resultSet.next()) {

				String size = resultSet.getString("Size");
				String crust = resultSet.getString("Crust");
				Double profit = resultSet.getDouble("Profit");
				String orderMonth = resultSet.getString("Order Month");
				System.out.printf("%-15s | %-15s | %-10s| %-30s%n", size, crust, profit, orderMonth);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByOrderType() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */

		try {
			connect_to_db();

			Statement statement = conn.createStatement();
			String query = "select * from ProfitByOrderType";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.printf("%-15s | %-15s | %-18s| %-18s| %-8s%n", "Customer Type", "Order Month", "Total Order Price",
					"Total Order Cost", "Profit");
			System.out.printf("-----------------------------------------------------------------------------------\n");
			while (resultSet.next()) {

				String customerType = resultSet.getString("CustomerType");
				String orderMonth = resultSet.getString("OrderDate");
				Double totalOrderPrice = resultSet.getDouble("TotalOrderPrice");
				Double totalOrderCost = resultSet.getDouble("TotalOrderCost");
				Double profit = resultSet.getDouble("Profit");
				System.out.printf("%-15s | %-15s | %-18s| %-18s| %-8s%n", customerType, orderMonth, totalOrderPrice,
						totalOrderCost, profit);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION	
	}
	
	
	
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
	/*
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with 
		 * your database from Java.  It's used in the model solution for this project...so the code works!
		 * 
		 * OF COURSE....this code would only work in your application if the table & field names match!
		 *
		 */

		 connect_to_db();

		/* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
		String cname1 = "";
		String query = "Select CustomerFName, CustomerLName From customer WHERE CustomerID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2); 
		}

		/* 
		* an example of the same query using a prepared statement...
		* 
		*/
		String cname2 = "";
		PreparedStatement os;
		ResultSet rset2;
		String query2;
		query2 = "Select CustomerFName, CustomerLName From customer WHERE CustomerID=?;";
		os = conn.prepareStatement(query2);
		os.setInt(1, CustID);
		rset2 = os.executeQuery();
		while(rset2.next())
		{
			cname2 = rset2.getString("CustomerFName") + " " + rset2.getString("CustomerLName"); // note the use of field names in the getSting methods
		}

		//conn.close();
		return cname1; // OR cname2
	}

	/*
	 * The next 3 private methods help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}