import java.util.HashMap;

public class UserHashMap {
    public static HashMap<String, User> Users = new HashMap<String, User>();

    public static final String CONST_TYPE_CUSTOMER = "customer";
    public static final String CONST_TYPE_STOREMANAGER = "storemanager";
    public static final String CONST_TYPE_SALESMAN = "salesman";

    public UserHashMap(){
        if(Users.isEmpty()){
            User user = new User("customer","customer", CONST_TYPE_CUSTOMER);
            Users.put("customer",user);
            user = new User("storemanager","storemanager", CONST_TYPE_STOREMANAGER);
            Users.put("storemanager",user);
            user = new User("salesman","salesman", CONST_TYPE_SALESMAN);
            Users.put("salesman",user);
        }
    }
}
