/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Dao;

import Johnny.Beans.Accessory;
import Johnny.Beans.Console;
import Johnny.Common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RZHUANG
 */
public class ConsoleDao {
    private List<Console> consoles = new ArrayList<Console>();
    public List<Console> getConsoleList() {
        if (consoles!=null && consoles.size() > 0) {
            return consoles;
        }
        Accessory xboxone_wc = new Accessory("xboxone_wc", "xboxone", "Controller", 40.00, "accessories/XBOX controller.jpg", "Microsoft","New",10);
        Accessory xboxone_sh = new Accessory("xboxone_sh", "xboxone", "Turtle Beach Headset", 50.00, "accessories/Turtle Beach Headset.jpg", "Microsoft","New",10);
        List<Accessory> accessories = new ArrayList<Accessory>();
        accessories.add(xboxone_wc);
        accessories.add(xboxone_sh);
        Console xboxone = new Console("xboxone", Constants.CONST_MICROSOFT, "XBox One",399.00,"consoles/xbox1.jpg",Constants.CONST_MICROSOFT,"New",10,accessories);
        consoles.add(xboxone);
        
        Accessory xbox360_mr = new Accessory("xbox360_mr", "xbox360", "Speeding Wheel", 40.00, "accessories/XBOX360-SpeedWheel.jpg", "Microsoft","New",10);
        Accessory xbox360_wa = new Accessory("xbox360_wa", "xbox360", "Wireless Adapter", 50.00, "accessories/xbox360_wa.png", "Microsoft","New",10);
        accessories = new ArrayList<Accessory>();
        accessories.add(xbox360_mr);
        accessories.add(xbox360_wa);
        Console xbox360 = new Console("xbox360", Constants.CONST_MICROSOFT, "XBox 360",299.00,"consoles/xbox360.jpg", Constants.CONST_MICROSOFT,"New",10,accessories);
        consoles.add(xbox360);
       
        Accessory ps3_wc = new Accessory("ps3_wc", "ps3", "Wireless Controller", 19.99, "accessories/ps3_controller.jpg", Constants.CONST_SONY,"New",10);
        Accessory ps3_dc = new Accessory("ps3_dc", "ps3", "Disc Remote Control", 24.99, "accessories/ps3_diskcontroller.jpg", Constants.CONST_SONY,"New",10);
        accessories = new ArrayList<Accessory>();
        accessories.add(ps3_wc);
        accessories.add(ps3_dc);
        Console ps3 = new Console("ps3", Constants.CONST_SONY, "PS3",219.00,"consoles/ps3-console.jpg",Constants.CONST_SONY,"New",10,accessories);
        consoles.add(ps3);

        Accessory ps4_cb = new Accessory("ps4_cb", "ps4", "Chartboost - Black", 19.99, "accessories/chartboost.jpg", Constants.CONST_SONY,"New",10);
        Accessory ps4_cc = new Accessory("ps4_cc", "ps4", "Dual Controller Charger", 24.99, "accessories/ps4_controllercharger.jpg", Constants.CONST_SONY,"New",10);
        accessories = new ArrayList<Accessory>();
        accessories.add(ps4_cb);
        accessories.add(ps4_cc);
        Console ps4 = new Console("ps4", Constants.CONST_SONY, "PS4",349.00,"consoles/PS4-console-bundle.jpg",Constants.CONST_SONY,"New",10,accessories);
        consoles.add(ps4);
       
        Accessory wii_cs = new Accessory("wii_cs", "wii", "Charging System - Black", 21.99, "accessories/wii_chargingsystem.jpg", Constants.CONST_NINTENDO,"New",10);
        Accessory wii_rp = new Accessory("wii_rp", "wii", "Wii Remote Plus", 39.99, "accessories/wii_remoteplus.jpg", Constants.CONST_NINTENDO,"New",10);            
        accessories = new ArrayList<Accessory>();
        accessories.add(wii_cs);
        accessories.add(wii_rp);
        Console wii = new Console("wii", Constants.CONST_NINTENDO, "Wii",269.00,"consoles/wii.jpg",Constants.CONST_NINTENDO,"New",10,accessories);
        consoles.add(wii);

        Accessory wiiu_fp = new Accessory("wiiu_fp", "wiiu", "Fight Pad", 16.99, "accessories/wiiu_fightingpad.jpg", Constants.CONST_NINTENDO,"New",10);
        Accessory wiiu_gc = new Accessory("wiiu_gc", "wiiu", "GameCube Controller", 29.99, "accessories/wiiu_gamecube.jpg", Constants.CONST_NINTENDO,"New",10);            
        accessories = new ArrayList<Accessory>();
        accessories.add(wiiu_fp);
        accessories.add(wiiu_gc);
        Console wiiu = new Console("wiiu", Constants.CONST_NINTENDO, "WiiU",299.99,"consoles/wiiu.jpg",Constants.CONST_NINTENDO,"New",10,accessories);
        consoles.add(wiiu);
            
        return consoles;
    }    
    
    public List<Console> getConsoleList(String maker) {
        if (maker==null || maker.isEmpty()) {
            return getConsoleList();
        }
        if (consoles.size() == 0) {
            getConsoleList();
        }
        List<Console> res = new ArrayList<Console>();
        for(Console console : consoles) {
            if (console.getRetailer().toLowerCase().equals(maker.toLowerCase())) {
                res.add(console);
            }
        }
        return res;
    }
}