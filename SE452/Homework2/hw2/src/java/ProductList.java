
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductList {    
    public static ArrayList<ProductItem> getItems(int type) {
        ArrayList<ProductItem> items = new ArrayList();
        switch(type) {
            case 1:
                HashMap<String, Console> hmc = new HashMap<String, Console>();
                hmc.putAll(ConsoleHashMap.Microsoft);
                hmc.putAll(ConsoleHashMap.Sony);
                hmc.putAll(ConsoleHashMap.Nintendo);
                for(Map.Entry<String, Console> entry : hmc.entrySet()){
                    Console console = entry.getValue();
                    for (Map.Entry<String, Accessory> entry2 : console.getAccessories().entrySet()) {
                        Accessory ac = entry2.getValue();
                        items.add(new ProductItem(ac.getKey(),ac.getName(), 1, ac.getPrice()));
                    }
                }
                break;
            case 2:
                HashMap<String, Game> hmg = new HashMap<String, Game>();
                hmg.putAll(GameHashMap.ElectronicArts);
                hmg.putAll(GameHashMap.Activision);
                hmg.putAll(GameHashMap.TakeTwoInteractive);
                for(Map.Entry<String, Game> entry : hmg.entrySet()){
                    Game gm = entry.getValue();
                    items.add(new ProductItem(gm.getKey(),gm.getName(), 2, gm.getPrice()));
                }
                break;
            case 3:
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
}
