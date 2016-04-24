import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GamesList")
public class GamesList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		String name = null;
		String makerName = request.getParameter("maker");
		makerName = makerName == null ? "" : makerName.toLowerCase();
		HashMap<String, Game> hm = new HashMap<String, Game>();

		if(makerName==null || makerName.isEmpty()){
			hm.putAll(GameHashMap.electronicArts);
			hm.putAll(GameHashMap.activision);
			hm.putAll(GameHashMap.takeTwoInteractive);
			name = "";
		}else{
			if(makerName.equals("electronicarts")){
				hm.putAll(GameHashMap.electronicArts);
				name = GameHashMap.string_electronicArts;
			}
			else if(makerName.equals("activision")){
				hm.putAll(GameHashMap.activision);
				name = GameHashMap.string_activision;
			}
			else if(makerName.equals("taketwointeractive")){
				hm.putAll(GameHashMap.takeTwoInteractive);
				name = GameHashMap.string_takeTwoInteractive;
			}
		}

		Helper helper = new Helper(request,pw);
		helper.prepareLayout();
		helper.prepareHeader();
		helper.prepareMenu();
		String itemtemp = helper.getTemplate("shopping_item2.html");
		String content = "";
		content += "<section id='content'>";
		content += "	<h3>"+name+" Games</h3>";
		int i = 1; int size= hm.size();
		for(Map.Entry<String, Game> entry : hm.entrySet()){
			Game game = entry.getValue();
			if(i%3==1) {
				content += "<div class='special_grid_row'>";
			}
			String item = itemtemp;
			item = item.replace("$itemname$", game.getName())
								.replace("$image$", game.getImage())
								.replace("$oldprice$", String.valueOf(game.getPrice()))
								.replace("$newprice$", String.valueOf(game.getPrice()))
								.replace("$name$", entry.getKey())
								.replace("$type$", "games")
								.replace("$maker$", makerName);
			content += item;
			if(i%3==0 || i == size) {
				content += "</div>";
			}
			i++;
		}
		content += "	<div class='clear'></div>";
		content += "</section>";
		helper.prepareContent(content);
		helper.prepareSideBar();
		helper.prepareFooter();
		helper.printHtml();
	}
}
