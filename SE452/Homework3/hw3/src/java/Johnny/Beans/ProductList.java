/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Beans;

import Johnny.Dao.ConsoleDao;
import Johnny.Dao.GameDao;
import Johnny.Dao.TabletDao;
import java.util.ArrayList;

/**
 *
 * @author Johnny
 */
public class ProductList {
    public static ArrayList<ProductItem> getItems() {
        ArrayList<ProductItem> res = new ArrayList();
        for (int i = 1; i <= 4; i++) {
            ArrayList<ProductItem> items = getItems(i);
            res.addAll(items);
        }
        return res;
    }
    public static ArrayList<ProductItem> getItems(int type) {
        ArrayList<ProductItem> items = new ArrayList();
        switch(type) {
            case 1:
                ConsoleDao dao = new ConsoleDao();
                for(Console cs : dao.getConsoleList()){
                    items.add(new ProductItem(cs.getKey(),cs.getName(), 1, cs.getPrice(), cs.getImage(), cs.getMaker(), cs.getDiscount()));
                    for (Accessory ac : cs.getAccessories()) {
                        items.add(new ProductItem(ac.getKey(),ac.getName(), 2, ac.getPrice(), ac.getImage(), ac.getRetailer(), ac.getDiscount()));
                    }
                }
                break;
            case 2:                
                break;
            case 3:
                GameDao gmDao = new GameDao();
                for(Game gm : gmDao.getGameList()){
                    items.add(new ProductItem(gm.getKey(),gm.getName(), 3, gm.getPrice(), gm.getImage(), gm.getMaker(), gm.getDiscount()));
                }                
                break;
            case 4:
                TabletDao tbDao = new TabletDao();
                for(Tablet tb : tbDao.getTabletList()){
                    items.add(new ProductItem(tb.getKey(),tb.getName(), 4, tb.getPrice(), tb.getImage(), tb.getMaker(), tb.getDiscount()));
                }
                break;
        }
        
        return items;
    }    
    public static ProductItem getItem(String id, int type) {
        
        if (id == null || id.isEmpty()) {
            return null;
        }
        ArrayList<ProductItem> items = getItems(type);
        ProductItem item;
        for(int i = 0; i < items.size(); i++) {
            item = items.get(i);
            if (id.equals(item.getId())&& type == item.getType()) {
                return item;
            }
        }
        return null;
    }
    
    public static ArrayList<ProductItem> searchProduct(String keyword) {
        ArrayList<ProductItem> res = new ArrayList();
        ArrayList<ProductItem> list = getItems();
        if (keyword == null || keyword.isEmpty()) {
            return list;
        }
        ProductItem item;
        for(int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                res.add(item);
            }
        }
        return res;
    }    
    
    public static ArrayList<String> autoCompleteProducts(String keyword) {
        ArrayList<String> res = new ArrayList();
        ArrayList<ProductItem> list = getItems();
        if (keyword == null || keyword.isEmpty()) {
            return res;
        }
        ProductItem item;
        for(int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())) {
                res.add(item.getName());
            }
        }
        return res;
    }
    
    public static ProductItem getProduct(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        
        ArrayList<ProductItem> list = getItems();
        
        ProductItem item;
        for(int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.getId().equals(id)) {
                 return item;
            }
        }
        return null;
    }
}

