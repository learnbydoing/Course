/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Dao;

import Johnny.Beans.User;
import Johnny.Common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Johnny
 */
public class UserDao {
    private static List<User> users = new ArrayList<User>();
    public UserDao() {
        getUserList();
    }
    public List<User> getUserList() {
        if (users!=null && users.size() > 0) {
            return users;
        }
        User user = new User("customer","customer", Constants.CONST_TYPE_CUSTOMER_LOWER);
        users.add(user);
        user = new User("storemanager","storemanager", Constants.CONST_TYPE_STOREMANAGER_LOWER);
        users.add(user);
        user = new User("salesman","salesman", Constants.CONST_TYPE_SALESMAN_LOWER);
        users.add(user);          
        
        return users;        
    }
    
    public User getUser(String username) {
        for (User user: users) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public boolean isExisted(String username) {
        return getUser(username) == null ? false : true;
    }
    
    public void addUser(User user) {        
        users.add(user);
    }
    
    public void deleteUser(String username) {
        if (users==null || users.isEmpty()) {
            return;
        } 
        
        User user = getUser(username);
        if (user==null) {
            return;
        } else {
            users.remove(user);
        }
    }
}
