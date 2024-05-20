import com.sun.source.tree.DoWhileLoopTree;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the methods for each of the menu options.
 * 
 * This file should not need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove methods as you see necessary. But you MUST have all of the menu methods (including exit!)
 * 
 * Simply removing menu methods because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 */

public class Menu {

	public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws SQLException, IOException {

		System.out.println("Welcome to Pizzas-R-Us!");
		
		int menu_option = 0;

		// present a menu of options and take their selection
		
		PrintMenu();
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order

				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{

		/*
		 * EnterOrder should do the following:
		 * 
		 * Ask if the order is delivery, pickup, or dinein
		 *   if dine in....ask for table number
		 *   if pickup...
		 *   if delivery...
		 * 
		 * Then, build the pizza(s) for the order (there's a method for this)
		 *  until there are no more pizzas for the order
		 *  add the pizzas to the order
		 *
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * return to menu
		 * 
		 * make sure you use the prompts below in the correct order!
		 */

		 // User Input Prompts...
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Is this order for: \n1.) Dine-in\n2.) Pick-up\n3.) Delivery\nEnter the number of your choice:");
		int choice = Integer.parseInt(reader.readLine());
		Order o1 = null;
		Order o2 = null;
		if (choice == 1) {

			System.out.println("What is the table number for this order?");
			int table = Integer.parseInt(reader.readLine());
			String choice_pizza=null;
			do{
				//System.out.println("Let's build a pizza!");
				//System.out.println("Now order object");
				Order o = new Order(DBNinja.getLastOrder().getOrderID(),DBNinja.getLastOrder().getCustID(),DBNinja.getLastOrder().getOrderType(),DBNinja.getLastOrder().getDate(),DBNinja.getLastOrder().getCustPrice(),DBNinja.getLastOrder().getBusPrice(),DBNinja.getLastOrder().getIsComplete()) ;
				//System.out.println("After order object");
				//System.out.println(o.toSimplePrint());
				//System.out.println(o.getOrderID());
				Pizza p = buildPizza(o.getOrderID());
				o1 = new Order(p.getOrderID()+1, 0,"dinein",java.time.LocalDate.now().toString(),0.0,0.0,0);
				DBNinja.addOrder(o1);
				System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
				 choice_pizza= reader.readLine();
			}while(!choice_pizza.equals("-1"));



		}
		if(choice==2){
			Customer cust =null;
			Integer cust_id = null;
			Integer input_cust_id = null;
			String choice_1=null;

			System.out.println("Is this order for an existing customer? Answer y/n: ");
			String existing = reader.readLine();
			if(existing.equals("y")) {
				System.out.println("Here's a list of the current customers: ");
				viewCustomers();
				ArrayList<Customer> cust_list = DBNinja.getCustomerList();
				System.out.println("Which customer is this order for? Enter ID Number:");
				input_cust_id = Integer.parseInt(reader.readLine());
				for (Customer c : cust_list) {
					if (input_cust_id == c.getCustID()) {
						cust_id = c.getCustID();
					}
					/*
					else{
						System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");

					}*/

				}
			}
			else if(existing.equals("n")){
				//System.out.println("Add new Customer");
				EnterCustomer();
				ArrayList<Customer> cust_list = DBNinja.getCustomerList();
				Customer c1 = cust_list.getLast();
				cust_id = c1.getCustID();


			}
			do {
					Pizza p = buildPizza(DBNinja.getLastOrder().getOrderID() + 1);
					//System.out.println(p);
					o1 = new PickupOrder(p.getOrderID(), cust_id,java.time.LocalDate.now().toString(), 0.0, 0.0, 0,0);
					o2 = new Order(p.getOrderID(), cust_id, "pickup", java.time.LocalDate.now().toString(), 0.0, 0.0, 0);
					DBNinja.addOrder(o1);
					System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
					choice_1 = reader.readLine();
				} while(!choice_1.equals("-1"));

		}
		if (choice == 3){
			Customer cust =null;
			Integer cust_id = null;
			Integer input_cust_id = null;
			String choice_1=null;
			Customer c1 = null;
			System.out.println("Is this order for an existing customer? Answer y/n: ");
			String existing = reader.readLine();
			if(existing.equals("y")) {
				System.out.println("Here's a list of the current customers: ");
				viewCustomers();
				ArrayList<Customer> cust_list = DBNinja.getCustomerList();
				System.out.println("Which customer is this order for? Enter ID Number:");
				input_cust_id = Integer.parseInt(reader.readLine());
				for (Customer c : cust_list) {
					if (input_cust_id == c.getCustID()) {
						cust_id = c.getCustID();
						c1=c;
					}
					/*
					else{
						System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");

					}*/

				}
			}
			else if(existing.equals("n")){

				//System.out.println("Add new Customer");
				EnterCustomer();
				ArrayList<Customer> cust_list = DBNinja.getCustomerList();
				c1 = cust_list.getLast();
				cust_id = c1.getCustID();


			}
			do {
				Pizza p = buildPizza(DBNinja.getLastOrder().getOrderID() + 1);
				//System.out.println(p);
				//System.out.println(cust_id);
				o1 = new DeliveryOrder(p.getOrderID(), c1.getCustID(),java.time.LocalDate.now().toString(), 0.0, 0.0, 0,c1.getAddress());
				o2 = new Order(p.getOrderID(), cust_id, "delivery", java.time.LocalDate.now().toString(), 0.0, 0.0, 0);
				DBNinja.addOrder(o1);
				System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
				choice_1 = reader.readLine();
			} while(!choice_1.equals("-1"));



			}

		System.out.println("Do you want to add discounts to this order? Enter y/n?");
		String order_dic = reader.readLine();
		ArrayList<Discount> discount_list= null;
		String choice3=null;
		ArrayList<Discount> discount_list_all = new ArrayList<Discount>();
		if(order_dic.equals("y")){



			do {
				discount_list = DBNinja.getDiscountList();
				for (Discount disc : discount_list) {
					System.out.println(disc);

				}
				System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
				choice3= reader.readLine();
				for (Discount d : discount_list){
					if(d.getDiscountID() == Integer.parseInt(choice3)){
						//System.out.println(o1);
						DBNinja.useOrderDiscount(o1,d);
						discount_list_all.add(d);
					}
				}
				if(!choice3.equals("-1")){
					System.out.println("Do you want to add more discounts to this Order? Enter y/n?");
					choice3 = reader.readLine();}
			}while(choice3.equals("y")) ;
		}





		/*
		System.out.println("Is this order for an existing customer? Answer y/n: ");
		System.out.println("Here's a list of the current customers: ");
		System.out.println("Which customer is this order for? Enter ID Number:");
		System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");
		System.out.println("What is the table number for this order?");
		System.out.println("Let's build a pizza!");

		System.out.println("Do you want to add discounts to this order? Enter y/n?");
		System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
		System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
		System.out.println("What is the Street for this order? (e.g., Smile Street)");
		System.out.println("What is the City for this order? (e.g., Greenville)");
		System.out.println("What is the State for this order? (e.g., SC)");
		System.out.println("What is the Zip Code for this order? (e.g., 20605)");
		
		
		System.out.println("Finished adding order...Returning to menu...");
		 */
	}
	
	
	public static void viewCustomers() throws SQLException, IOException 
	{
		/*
		 * Simply print out all of the customers from the database. 
		 */


		try {
			ArrayList<Customer> customerList = DBNinja.getCustomerList();
			//System.out.println(customerList);
			for (Customer customer : customerList) {
				System.out.println(customer);
			}
		} catch (SQLException | IOException e) {
			System.out.println(e);
		}

		
		
	}
	

	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException 
	{
		/*
		 * Ask for the name of the customer:
		 *   First Name <space> Last Name
		 * 
		 * Ask for the  phone number.
		 *   (##########) (No dash/space)
		 * 
		 * Once you get the name and phone number, add it to the DB
		 */
		
		// User Input Prompts...
		int last_cust_id=7;
		 //System.out.println("What is this customer's name (first <space> last");

		 //System.out.println("What is this customer's phone number (##########) (No dash/space)");

		//BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
		System.out.println("What is this customer's name (first <space> last");
		String name = reader.readLine();
		String[] name1=name.split(" ");
		System.out.println(Arrays.toString(name1));
		System.out.println("What is this customer's phone number (##########) (No dash/space)");
		String phone = reader.readLine();
		Customer cust = new Customer(last_cust_id,name1[0],name1[1],phone);
		last_cust_id = last_cust_id+1;

		System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
		String house = reader.readLine();
		System.out.println("What is the Street for this order? (e.g., Smile Street)");
		String street = reader.readLine();
		System.out.println("What is the City for this order? (e.g., Greenville)");
		String city = reader.readLine();
		System.out.println("What is the State for this order? (e.g., SC)");
		String state = reader.readLine();
		System.out.println("What is the Zip Code for this order? (e.g., 20605)");
		String pin = reader.readLine();
		cust.setAddress(street,city,state,pin);
		DBNinja.addCustomer(cust);

	}

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
		/*  
		* This method allows the user to select between three different views of the Order history:
		* The program must display:
		* a.	all open orders
		* b.	all completed orders 
		* c.	all the orders (open and completed) since a specific date (inclusive)
		* 
		* After displaying the list of orders (in a condensed format) must allow the user to select a specific order for viewing its details.  
		* The details include the full order type information, the pizza information (including pizza discounts), and the order discounts.
		* 
		*/


		ArrayList<Order> orders;
		ArrayList<Order> OrderDetails;

		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Would you like to:\n" +
					"(a) display all orders [open or closed]\n" +
					"(b) display all open orders\n" +
					"(c) display all completed [closed] orders\n" +
					"(d) display orders since a specific date");

			char EnterOptions= sc.next().charAt(0);
			if(EnterOptions == 'a')
			{
				orders = DBNinja.getOrders(false);

				for (Order order : orders) {
					System.out.println(order.toSimplePrint());
				}
			} else if (EnterOptions == 'b') {

				orders = DBNinja.getOrders(true);

				for (Order order : orders) {
					System.out.println(order.toSimplePrint());
				}
			} else if (EnterOptions == 'c') {

				orders = DBNinja.getOrders(false);

				for (Order order : orders) {
					if(order.getIsComplete() == 1)
					{System.out.println(order.toSimplePrint());}
				}
			} else {

				System.out.println("What is the date you want to restrict by? (FORMAT= YYYY-MM-DD)");
				String date = sc.next();
				orders = DBNinja.getOrdersByDate(date);
				for (Order order : orders) {
					System.out.println(order.toSimplePrint());
				}
			}
			System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): "); {
//				int OrderNum = sc.nextInt();
				Integer orderId = Integer.parseInt(reader.readLine());
				Order order = orders.stream().filter(o -> o.getOrderID()==orderId)
						.findFirst().orElse(null);
				if(orderId!=null){
                    assert order != null;
                    System.out.println(order.toSimplePrint());

				}
				else{
					System.out.println("No orders to display, returning to menu.");
				}

			}

		}catch(Exception e)
		{
			System.out.println("There was error in displaying the Orders " + e);
			e.printStackTrace();

		}

		// User Input Prompts...


		//System.out.println("I don't understand that input, returning to menu");

		//System.out.println("Incorrect entry, returning to menu.");
		//System.out.println("No orders to display, returning to menu.");





	}

	
	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException 
	{
		/*
		 * All orders that are created through java (part 3, not the orders from part 2) should start as incomplete
		 * 
		 * When this method is called, you should print all of the "opoen" orders marked
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */
		
		
		
		// User Input Prompts...
		ArrayList<Order> orders = DBNinja.getOrders(false);

		if (orders.isEmpty()){
			System.out.println("There are no open orders currently... returning to menu...");
		} else if (!orders.isEmpty()) {
			for (Order order : orders) {
				System.out.println(order.toSimplePrint());
			}
			System.out.println("Which order would you like mark as complete? Enter the OrderID: ");
			//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			Integer orderId = Integer.parseInt(reader.readLine());
			DBNinja.completeOrder(orders.stream().filter(o->o.getOrderID()==orderId).findFirst().get());
		}else {
			System.out.println("Incorrect entry, not an option");
		}

		
		

	}

	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		/*
		 * Print the inventory. Display the topping ID, name, and current inventory
		*/
		
		DBNinja.printInventory();
		//ArrayList<Topping> toppings
		//System.out.printf("%-5s %-25s %-10s%n", "ID", "Topping", "CurrINVT");
		
		
	}


	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping (by ID) they want to add more to and how much to add
		 */
		
		
		// User Input Prompts...
		//BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
		ViewInventoryLevels();
		System.out.println("Which topping do you want to add inventory to? Enter the number: ");
		int topping_id = Integer.parseInt(reader.readLine());

		if(topping_id <=17 && topping_id>=1){
			System.out.println("How many units would you like to add? ");
			int units = Integer.parseInt(reader.readLine());
			Topping top = new Topping(topping_id," ",0,0,0,0,0,0,0,0);
			DBNinja.addToInventory(top,units);
		}
		else{
			System.out.println("Incorrect entry, not an option");
		}

	
		
		
		
	}

	// A method that builds a pizza. Used in our add new order method
	public static Pizza buildPizza(int orderID) throws SQLException, IOException 
	{
		
		/*
		 * This is a helper method for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */

		Pizza ret = null;
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean isextra = false;
		boolean repeat = false;
		Pizza p = null;
		Topping selected_topping = null;
		//String option = reader.readLine();
		// User Input Prompts...

		ArrayList<Discount> discount_list_all = new ArrayList<Discount>();

		System.out.println("What size is the pizza?");
		System.out.println("1."+DBNinja.size_s);
		System.out.println("2."+DBNinja.size_m);
		System.out.println("3."+DBNinja.size_l);
		System.out.println("4."+DBNinja.size_xl);
		String pizza_size = reader.readLine();
		System.out.println("Enter the corresponding number: ");
		System.out.println("What crust for this pizza?");
		System.out.println("1."+DBNinja.crust_thin);
		System.out.println("2."+DBNinja.crust_orig);
		System.out.println("3."+DBNinja.crust_pan);
		System.out.println("4."+DBNinja.crust_gf);

		System.out.println("Enter the corresponding number: ");
		String crust_type = reader.readLine();
		String choice3=null;
		String choice2=null;
		String choice1 = null;
		Map<Integer, Object> dict_crust = new HashMap<>();
		dict_crust.put(1, DBNinja.crust_thin);
		dict_crust.put(2, DBNinja.crust_orig);
		dict_crust.put(3, DBNinja.crust_pan);
		dict_crust.put(4, DBNinja.crust_gf);

		Map<Integer, Object> dict_base = new HashMap<>();
		dict_base.put(1, DBNinja.size_s);
		dict_base.put(2, DBNinja.size_m);
		dict_base.put(3, DBNinja.size_l);
		dict_base.put(4, DBNinja.size_xl);
		ArrayList<Integer> topping_ids = new ArrayList<>();
		Double total_cust_price=0.0;
		Double total_biz_cost  = 0.0;

		do {
			System.out.println("Available Toppings:");
			DBNinja.printInventory();

			System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");
			Integer topping_id = Integer.parseInt(reader.readLine());
			ArrayList<Topping> top = DBNinja.getToppingList();
			for (Topping topping1 : top){
				if (topping1.getTopID()==topping_id){
					System.out.println("Build Pizza If");
					if(topping_ids.contains(topping_id)){
						isextra=true;
					}
					topping_ids.add(topping1.getTopID());
					selected_topping = new Topping(topping1.getTopID(),topping1.getTopName(),topping1.getPerAMT(),topping1.getMedAMT(),topping1.getLgAMT(),topping1.getXLAMT(),topping1.getCustPrice(),topping1.getBusPrice(),topping1.getMinINVT(),topping1.getCurINVT());
					Order order_pizza = DBNinja.getLastOrder();
					Double base_price_cust = DBNinja.getBaseCustPrice(dict_base.get(Integer.parseInt(pizza_size)).toString(),dict_crust.get(Integer.parseInt(crust_type)).toString());
					Double base_cost_bus = DBNinja.getBaseBusPrice(dict_base.get(Integer.parseInt(pizza_size)).toString(),dict_crust.get(Integer.parseInt(crust_type)).toString());
					total_cust_price = base_price_cust ;
					total_biz_cost = base_cost_bus ;

					p = new Pizza(order_pizza.getOrderID()+1,dict_base.get(Integer.parseInt(pizza_size)).toString(),dict_crust.get(Integer.parseInt(crust_type)).toString(),orderID,"Inprogress",java.time.LocalDate.now().toString(),total_cust_price,total_biz_cost);
					if(!repeat){
						DBNinja.addPizza(p);
						repeat=true;
					}
					else{
						total_cust_price = total_cust_price + topping1.getCustPrice();
						total_biz_cost = total_biz_cost + topping1.getBusPrice();

					}

					//p.toString();

					//selected_topping.toString();


				}
			}

			System.out.println("Do you want to add extra topping? Enter y/n");
			choice1 = reader.readLine();
			//isextra=true;
			//System.out.println("Pizza:"+p);
			//System.out.println("Topping:"+selected_topping);
			//System.out.println(isextra);
			//if(choice1.equals("y")){

				//isextra=true;
				//System.out.println("Pizza:"+p);
				//System.out.println("Topping:"+selected_topping);
				//System.out.println(isextra);

				//DBNinja.useTopping(p,selected_topping,isextra);
				//DBNinja.usePizzaDiscount(p,d);

			//}
			//else{
				//isextra = false;

			//}

			DBNinja.useTopping(p,selected_topping,isextra);

			//System.out.println(choice1);
		} while(choice1.equals("y"));

		System.out.println("Do you want to add discounts to this Pizza? Enter y/n?");
		choice2 = reader.readLine();
		ArrayList<Discount> discount_list= null;

			if(choice2.equals("y")){
				//System.out.println("In y if loop/");
				do {
					discount_list = DBNinja.getDiscountList();
					for (Discount disc : discount_list) {
						System.out.println(disc);

					}
					System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
					choice3= reader.readLine();
					for (Discount d : discount_list){
						if(d.getDiscountID() == Integer.parseInt(choice3)){
							DBNinja.usePizzaDiscount(p,d);
							discount_list_all.add(d);
						}
					}
					if(!choice3.equals("-1")){
						System.out.println("Do you want to add more discounts to this Pizza? Enter y/n?");
						choice3 = reader.readLine();}
				}while(choice3.equals("y")) ;

				//System.out.println("Need to implement DiscountList function");


			}
			//System.out.println(java.time.LocalDate.now().toString());
			//ret = new Pizza(pizza_size,crust_type,orderID,"inprogress",java.time.LocalDate.now(),cut_price,bus_price)

		//System.out.println("We don't have enough of that topping to add it...");
		//System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");

		//System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");
		//System.out.println("Do you want to add more discounts to this Pizza? Enter y/n?");
	
		
		
		ret = p;
		
		return ret;
	}
	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		/*
		 * This method asks the use which report they want to see and calls the DBNinja method to print the appropriate report.
		 * 
		 */

		// User Input Prompts...
		System.out.println("Which report do you wish to print? Enter\n(a) ToppingPopularity\n(b) ProfitByPizza\n(c) ProfitByOrderType:");
		char userInput = reader.readLine().charAt(0);
		switch(userInput){
			case 'a':
				DBNinja.printToppingPopReport();
				break;
			case 'b':
				DBNinja.printProfitByPizzaReport();
				break;
			case 'c':
				DBNinja.printProfitByOrderType();
				break;
		}
		//System.out.println("I don't understand that input... returning to menu...");

	}

	//Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
	// DO NOT EDIT ANYTHING BELOW HERE, THIS IS NEEDED TESTING.
	// IF YOU EDIT SOMETHING BELOW, IT BREAKS THE AUTOGRADER WHICH MEANS YOUR GRADE WILL BE A 0 (zero)!!

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	/*
	 * autograder controls....do not modiify!
	 */

	public final static String autograder_seed = "6f1b7ea9aac470402d48f7916ea6a010";

	
	private static void autograder_compilation_check() {

		try {
			Order o = null;
			Pizza p = null;
			Topping t = null;
			Discount d = null;
			Customer c = null;
			ArrayList<Order> alo = null;
			ArrayList<Discount> ald = null;
			ArrayList<Customer> alc = null;
			ArrayList<Topping> alt = null;
			double v = 0.0;
			String s = "";

			DBNinja.addOrder(o);
			DBNinja.addPizza(p);
			DBNinja.useTopping(p, t, false);
			DBNinja.usePizzaDiscount(p, d);
			DBNinja.useOrderDiscount(o, d);
			DBNinja.addCustomer(c);
			DBNinja.completeOrder(o);
			alo = DBNinja.getOrders(false);
			o = DBNinja.getLastOrder();
			alo = DBNinja.getOrdersByDate("01/01/1999");
			ald = DBNinja.getDiscountList();
			d = DBNinja.findDiscountByName("Discount");
			alc = DBNinja.getCustomerList();
			c = DBNinja.findCustomerByPhone("0000000000");
			alt = DBNinja.getToppingList();
			t = DBNinja.findToppingByName("Topping");
			DBNinja.addToInventory(t, 1000.0);
			v = DBNinja.getBaseCustPrice("size", "crust");
			v = DBNinja.getBaseBusPrice("size", "crust");
			DBNinja.printInventory();
			DBNinja.printToppingPopReport();
			DBNinja.printProfitByPizzaReport();
			DBNinja.printProfitByOrderType();
			s = DBNinja.getCustomerName(0);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}


}


