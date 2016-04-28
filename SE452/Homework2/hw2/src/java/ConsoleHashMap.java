import java.util.HashMap;

public class ConsoleHashMap{
    public static HashMap<String, Console> Microsoft = new HashMap<String, Console>();
    public static HashMap<String, Console> Sony = new HashMap<String, Console>();
    public static HashMap<String, Console> Nintendo = new HashMap<String, Console>();

    public static final String CONST_MICROSOFT = "Microsoft";
    public static final String CONST_SONY = "Sony";
    public static final String CONST_NINTENDO = "Nintendo";
    public static final String CONST_MICROSOFT_LOWER = "microsoft";
    public static final String CONST_SONY_LOWER = "sony";
    public static final String CONST_NINTENDO_LOWER = "nintendo";

    public ConsoleHashMap() {
        HashMap<String, Accessory> accessories;
        if(Microsoft.isEmpty()){
            Accessory xboxone_wc = new Accessory("xboxone_wc", "xboxone", "Controller", 40.00, "accessories/XBOX controller.jpg", "Microsoft","New",10);
            Accessory xboxone_sh = new Accessory("xboxone_sh", "xboxone", "Turtle Beach Headset", 50.00, "accessories/Turtle Beach Headset.jpg", "Microsoft","New",10);
            accessories = new HashMap<String, Accessory>();
            accessories.put("xboxone_wc", xboxone_wc);
            accessories.put("xboxone_sh", xboxone_sh);
            Console xboxone = new Console("xboxone", CONST_MICROSOFT, "XBox One",399.00,"consoles/xbox1.jpg",CONST_MICROSOFT,"New",10,accessories);
            Microsoft.put("xboxone", xboxone);

            Accessory xbox360_mr = new Accessory("xbox360_mr", "xbox360", "Speeding Wheel", 40.00, "accessories/XBOX360-SpeedWheel.jpg", "Microsoft","New",10);
            Accessory xbox360_wa = new Accessory("xbox360_wa", "xbox360", "Wireless Adapter", 50.00, "accessories/xbox360_wa.png", "Microsoft","New",10);
            accessories = new HashMap<String, Accessory>();
            accessories.put("xbox360_mr", xbox360_mr);
            accessories.put("xbox360_wa", xbox360_wa);
            Console xbox360 = new Console("xbox360", CONST_MICROSOFT, "XBox 360",299.00,"consoles/xbox360.jpg",CONST_MICROSOFT,"New",10,accessories);
            Microsoft.put("xbox360", xbox360);
        }
        if(Sony.isEmpty()){
            //accessories = new HashMap<String, Accessory>();
            //Console ps3 = new Console("ps3", CONST_SONY, "PS3",349.00,"consoles/PS4-console-bundle.jpg",CONST_SONY,"New",10,accessories);
            //Sony.put("ps3", ps3);
            accessories = new HashMap<String, Accessory>();
            Console ps4 = new Console("ps4", CONST_SONY, "PS4",349.00,"consoles/PS4-console-bundle.jpg",CONST_SONY,"New",10,accessories);
            Sony.put("ps4", ps4);
        }
        if(Nintendo.isEmpty()){

        }
    }
}
