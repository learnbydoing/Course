/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Beans;

/**
 *
 * @author RZHUANG
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Console {
    private String key;
    private String manufacturer;
    private String name;
    private double price;
    private String image;
    private String retailer;
    private String condition;
    private double discount;
    private List<Accessory> accessories = new ArrayList<Accessory>();

    public Console(String key, String manufacturer, String name, double price, String image, String retailer,String condition,double discount, List<Accessory> accessories){
        this.key = key;
        this.manufacturer = manufacturer;
        this.name = name;
        this.price = price;
        this.image = image;
        this.retailer = retailer;
        this.condition = condition;
        this.discount = discount;
        this.setAccessories(accessories);
    }

    public Console(){

    }
    public String getKey() {
        return key;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getRetailer() {
        return retailer;
    }
    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public double getDiscountedPrice() {
        return price * (100 - discount) / 100;
    }
    public List<Accessory> getAccessories() {
        return accessories;
    }
    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }
}
