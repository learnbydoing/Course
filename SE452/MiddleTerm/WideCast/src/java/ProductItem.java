
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
public class ProductItem {
    private String id;
    private String name;
    private int type; //  1.TV, 2.Internet, 3.Phone 4.Live sports, 5.Movies
    private double price;
    private Date eventdate;

    public ProductItem(String id, String name, int type, double price, Date eventdate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.eventdate = eventdate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public Date getEventDate() {
        return eventdate;
    }

    public void setEventDate(Date eventdate) {
        this.eventdate = eventdate;
    }
}
