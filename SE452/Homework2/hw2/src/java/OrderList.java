
import java.util.ArrayList;
import java.util.List;

public class OrderList {
    private ArrayList<Order> orders;
    public OrderList() {        
        orders = new ArrayList();
    }    
  
    public List getOrders() {
      return orders;
    }
    
    public List getOrders(String username) {
        if (username == null || username.isEmpty()) {
            return orders;
        }
        ArrayList<Order> res = new ArrayList<Order>();
        for (Order order: orders) {
            if (order.getUserName().equals(username)) {
                res.add(order);
            }
        }
        return res;
    }
    
    public synchronized void addOrder(Order order) {
        orders.add(order);
    }
    
    public synchronized Order getOrder(String id) {
        if (orders==null || orders.isEmpty()) {
            return null;
        } 
        for (Order order: orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }
    
    public synchronized void removeOrder(String id) {
        if (orders==null || orders.isEmpty()) {
            return;
        } 
        
        Order order = getOrder(id);
        if (order==null) {
            return;
        } else {
            orders.remove(order);
        }
    }
}
