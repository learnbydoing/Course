
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
public class OrderList {
    private ArrayList<Order> orders;
    public OrderList() {        
        orders = new ArrayList();
    }    
  
    public List getOrders() {
      return orders;
    }
    
    public List getOrders(String user) {
        if (user == null || user.isEmpty()) {
            return orders;
        }
        ArrayList<Order> res = new ArrayList<Order>();
        for (Order order: orders) {
            if (order.getUser().equals(user)) {
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
