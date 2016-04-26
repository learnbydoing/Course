
import java.util.Calendar;
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
public class ProductList {    
    private static ProductItem[] items = new ProductItem[] {
        new ProductItem("tv1","Basic - 50 Channels", 1, 48.99, new Date()),
        new ProductItem("tv2","BasicPlus - 100 Channels", 1, 98.99, new Date()),
        new ProductItem("tv3","Ultimate - 200 Channels", 1, 138.99, new Date()),
        new ProductItem("net1","SpeedLane - 20/5 Mbps", 2, 56.49, new Date()),
        new ProductItem("net2","LightLane - 50/10 MBPS", 2, 79.99, new Date()),
        new ProductItem("phone1","Basic - 1000 calls", 3, 35.99, new Date()),
        new ProductItem("phone2","Freedom - unlimited calls", 3, 69.09, new Date()),
        new ProductItem("sports1","NBA", 4, 112.49, getDate(0)),
        new ProductItem("sports2","NFL", 4, 149.99, getDate(1)),
        new ProductItem("sports3","MLB", 4, 139.99, getDate(2)),
        new ProductItem("movie1","Star Wars", 5, 30.99, getDate(-1)),
        new ProductItem("movie2","Spotlight", 5, 23.99, getDate(3)),
        new ProductItem("movie3","The Martian", 5, 33.99, getDate(6))
    };
    
    public static ProductItem getItem(String id) {
        ProductItem item;
        if (id == null || id.isEmpty()) {
            return null;
        }
        for(int i = 0; i < items.length; i++) {
            item = items[i];
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }
    
    public static Date getDate(int days) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, days);
        return c1.getTime();
    }
}
