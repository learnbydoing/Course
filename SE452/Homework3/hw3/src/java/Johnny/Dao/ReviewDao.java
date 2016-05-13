/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Dao;

import Johnny.Beans.Review;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Johnny
 */
public class ReviewDao {
    public static HashMap<String, ArrayList<Review>> reviews = new HashMap<String, ArrayList<Review>>();
    public ReviewDao() {
        ArrayList<Review> list = new ArrayList<Review>();
        Review review = new Review("1", "customer", 5, new Date(), "Easy to use, funny!");
        list.add(review);
        Review review2 = new Review("2", "storemanager", 4, new Date(), "Like it!");
        list.add(review2);
        reviews.put("xbox360", list);
        ArrayList<Review> list2 = new ArrayList<Review>();
        Review review21 = new Review("1", "customer", 3, new Date(), "Too expensive, doesn't worth");
        list2.add(review21);
        reviews.put("xbox360_wa", list2);
        ArrayList<Review> list3 = new ArrayList<Review>();
        Review review31 = new Review("1", "customer", 5, new Date(), "Great game, I spent all weekend playing with it.");
        list3.add(review31);
        reviews.put("ea_nfs", list3);
    }
}
