import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ConsoleList")
public class ConsoleList extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String name = null;
		String makerName = request.getParameter("maker");
		makerName = makerName == null ? "" : makerName.toLowerCase();
		HashMap<String, Console> hm = new HashMap<String, Console>();
		if(makerName==null || makerName.isEmpty()){
			hm.putAll(ConsoleHashMap.microsoft);
			hm.putAll(ConsoleHashMap.sony);
			hm.putAll(ConsoleHashMap.nintendo);
			name = "";
		}else{
			if(makerName.equals("microsoft")){
				hm.putAll(ConsoleHashMap.microsoft);
				name = ConsoleHashMap.string_microsoft;
			}
			else if(makerName.equals("sony")){
				hm.putAll(ConsoleHashMap.sony);
				name = ConsoleHashMap.string_sony;
			}
			else if(makerName.equals("nintendo")){
				hm.putAll(ConsoleHashMap.nintendo);
				name = ConsoleHashMap.string_nintendo;
			}
		}

		Helper helper = new Helper(request,pw);
		helper.prepareLayout();
		helper.prepareHeader();
		helper.prepareMenu();
		String itemtemp = helper.getTemplate("shopping_item.html");
		String content = "";
		content += "<section id='content'>";
		content += "	<h3>"+name+" Consoles</h3>";
		int i = 1; int size= hm.size();
		for(Map.Entry<String, Console> entry : hm.entrySet()){
			Console console = entry.getValue();
			if(i%3==1) {
				content += "<div class='special_grid_row'>";
			}
			String item = itemtemp;
			item = item.replace("$itemname$", console.getName())
								.replace("$image$", console.getImage())
								.replace("$oldprice$", String.valueOf(console.getPrice()))
								.replace("$newprice$", String.valueOf(console.getPrice()))
								.replace("$name$", entry.getKey())
								.replace("$type$", "consoles")
								.replace("$maker$", console.getRetailer());
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
