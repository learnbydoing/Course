
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Johnny
 */
public class CartItem {
    private ProductItem item;
    private int quantity;

    public CartItem(ProductItem item) {
        setItem(item);
        setQuantity(1);
    }

    public ProductItem getItem() {
        return item;
    }

    protected void setItem(ProductItem item) {
        this.item = item;
    }

    public String getItemId() {
        return getItem().getId();
    }
    
    public String getItemName() {
        return getItem().getName();
    }

    public int getItemType() {
        return getItem().getType();
    }

    public Date getItemEventDate() {
        return getItem().getEventDate();
    }

    public double getUnitPrice() {
        return getItem().getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementItemQuantity() {
        setQuantity(getQuantity() + 1);
    }

    public void cancelOrder() {
        setQuantity(0);
    }

    public double getTotalCost() {
        return(getQuantity() * getUnitPrice());
    }
}
