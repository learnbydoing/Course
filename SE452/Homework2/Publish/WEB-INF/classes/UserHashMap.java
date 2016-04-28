import java.util.HashMap;

public class UserHashMap {
	public static HashMap<String, User> Customer = new HashMap<String, User>();
	public static HashMap<String, User> Storemanager = new HashMap<String, User>();
	public static HashMap<String, User> Salesman = new HashMap<String, User>();

	public static final String CONST_TYPE_CUSTOMER = "customer";
	public static final String CONST_TYPE_STOREMANAGER = "storemanager";
	public static final String CONST_TYPE_SALESMAN = "salesman";

	public UserHashMap(){
		if(Customer.isEmpty()){
			User user = new User("customer","customer", CONST_TYPE_CUSTOMER);
			Customer.put("customer",user);
		}
		if(Storemanager.isEmpty()){
			User user = new User("storemanager","storemanager", CONST_TYPE_STOREMANAGER);
			Storemanager.put("storemanager",user);
		}
		if(Salesman.isEmpty()){
			User user = new User("salesman","salesman", CONST_TYPE_SALESMAN);
			Salesman.put("salesman",user);
		}
	}
}
