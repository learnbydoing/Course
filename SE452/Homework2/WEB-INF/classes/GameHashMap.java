

import java.util.HashMap;

public class GameHashMap {

	public static HashMap<String, Game> electronicArts = new HashMap<String, Game>();
	public static HashMap<String, Game> activision = new HashMap<String, Game>();
	public static HashMap<String, Game> takeTwoInteractive = new HashMap<String, Game>();

	public static String string_electronicArts = "Electronic Arts";
	public static String string_activision = "Activision";
	public static String string_takeTwoInteractive = "Take-Two Interactive";

	public GameHashMap() {
		if(electronicArts.isEmpty()){
			Game ea_fifa = new Game("FIFA 2016",59.99,"games/ea_fifa.jpg","Electronic Arts","New",10);
			Game ea_nfs = new Game("Need for Speed",59.99,"games/ea_nfs.jpg","Electronic Arts","New",10);
			electronicArts.put("ea_nfs", ea_nfs);
			electronicArts.put("ea_fifa", ea_fifa);
		}
		if(activision.isEmpty()){
			Game activision_cod = new Game("Call Of Duty",54.99,"games/activision_cod.jpg","Activision","New",10);
			activision.put("activision_cod", activision_cod);
		}
		if(takeTwoInteractive.isEmpty()){
			Game tti_evolve = new Game("Evolve",49.99,"games/tti_evolve.jpg","Take-Two Interactive","New",10);			
			takeTwoInteractive.put("tti_evolve", tti_evolve);
		}
	}
}
