import java.util.HashMap;

public class ConsoleHashMap{
	public static HashMap<String, Console> microsoft = new HashMap<String, Console>();
	public static HashMap<String, Console> sony = new HashMap<String, Console>();
	public static HashMap<String, Console> nintendo = new HashMap<String, Console>();

	public static String string_microsoft = "Microsoft";
	public static String string_sony = "Sony";
	public static String string_nintendo = "Nintendo";

	public ConsoleHashMap() {
		HashMap<String, Accessory> accessories;
		if(microsoft.isEmpty()){
			Accessory xboxone_wc = new Accessory("Controller", 40.00, "accessories/XBOX controller.jpg", "Microsoft","New",10);
			Accessory xboxone_sh = new Accessory("Turtle Beach Headset", 50.00, "accessories/Turtle Beach Headset.jpg", "Microsoft","New",10);
			accessories = new HashMap<String, Accessory>();
			accessories.put("xboxone_wc", xboxone_wc);
			accessories.put("xboxone_sh", xboxone_sh);
			Console xboxone = new Console("XBox One",399.00,"consoles/xbox1.jpg","Microsoft","New",10,accessories);
			microsoft.put("xboxone", xboxone);

			Accessory xbox360_mr = new Accessory("Speeding Wheel", 40.00, "accessories/XBOX360-SpeedWheel.jpg", "Microsoft","New",10);
			Accessory xbox360_wa = new Accessory("Wireless Adapter", 50.00, "accessories/xbox360_wa.png", "Microsoft","New",10);
			accessories = new HashMap<String, Accessory>();
			accessories.put("xbox360_mr", xbox360_mr);
			accessories.put("xbox360_wa", xbox360_wa);
			Console xbox360 = new Console("XBox 360",299.00,"consoles/xbox360.jpg","Microsoft","New",10,accessories);
			microsoft.put("xbox360", xbox360);
		}
		if(sony.isEmpty()){
			accessories = new HashMap<String, Accessory>();
			Console ps4 = new Console("PS4",349.00,"consoles/PS4-console-bundle.jpg","Sony","New",10,accessories);
			sony.put("ps4", ps4);
		}
		if(nintendo.isEmpty()){

		}
	}
}
