
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
public class ShoppingCart {
    private ArrayList<CartItem> items;
    public ShoppingCart() {
        items = new ArrayList();
    }
    public List getItems() {
      return items;
    }
    
    public synchronized void addItem(String id) {
        CartItem order;
        for(int i = 0; i < items.size(); i++) {
            order = items.get(i);
            if (order.getItemId().equals(id)) {
                order.incrementItemQuantity();
                return;
            }
        }
        CartItem newOrder = new CartItem(ProductList.getItem(id));
        items.add(newOrder);
    }
    
    public synchronized void setItemQuantity(String id, int quantity) {
        CartItem order;
        for(int i = 0; i < items.size(); i++) {
            order = items.get(i);
            if (order.getItemId().equals(id)) {
                if (quantity <= 0) {
                    items.remove(i);
                } else {
                    order.setQuantity(quantity);
                }
                return;
            }
        }
        CartItem newOrder = new CartItem(ProductList.getItem(id));
        items.add(newOrder);
    }
}
