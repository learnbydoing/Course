
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Johnny
 */
public class Order {
    private String id;
    private String user;
    private String confirmation;
    private ArrayList<OrderItem> items;
    public Order(String id, String user, String confirmation) {
        this.id = id;
        this.user = user;
        this.confirmation = confirmation;
        items = new ArrayList();
    }
    
    public String getId() {
        return id;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getConfirmation() {
        return confirmation;
    }
    
    public void setUser(String confirmation) {
        this.confirmation = confirmation;
    }
   
    public List getItems() {
      return items;
    }
    
    public synchronized void addItem(OrderItem item) {
        items.add(item);
    }
}
