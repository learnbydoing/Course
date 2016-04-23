

import java.util.HashMap;

public class UserHashMap {
	public static HashMap<String, User> customer = new HashMap<String, User>();
	public static HashMap<String, User> retailer = new HashMap<String, User>();
	public static HashMap<String, User> manager = new HashMap<String, User>();
	
	public UserHashMap(){
		if(customer.isEmpty()){
			User user = new User("customer","customer","customer");
			customer.put("customer",user);
		}
		if(retailer.isEmpty()){
			User user = new User("retailer","retailer","retailer");
			retailer.put("retailer",user);		
		}
		if(manager.isEmpty()){
			User user = new User("manager","manager","manager");
			manager.put("manager",user);
		}
	}

}
