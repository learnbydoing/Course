

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TabletList")
public class TabletList extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		String name = null;
		String CategoryName = request.getParameter("maker");
		HashMap<String, Tablet> hm = new HashMap<String, Tablet>();

		if (CategoryName == null) {
			CategoryName = "";
			hm.putAll(TabletHashMap.apple);
			hm.putAll(TabletHashMap.microsoft);
			hm.putAll(TabletHashMap.samsung);
			name = "";
		} else {
			if (CategoryName.equals("apple")) {
				hm.putAll(TabletHashMap.apple);
				name = TabletHashMap.string_apple;
			} else if (CategoryName.equals("microsoft")) {
				hm.putAll(TabletHashMap.microsoft);
				name = TabletHashMap.string_microsoft;
			} else if (CategoryName.equals("samsung")) {
				hm.putAll(TabletHashMap.samsung);
				name = TabletHashMap.string_samsung;
			}
		}

		Helper helper = new Helper(request,pw);
		helper.prepareLayout();
		helper.prepareHeader();
		helper.prepareMenu();
		String itemtemp = helper.getTemplate("shopping_item2.html");
		String content = "";
		content += "<section id='content'>";
		content += "	<h3>"+name+" Tablets</h3>";
		int i = 1; int size= hm.size();
		for(Map.Entry<String, Tablet> entry : hm.entrySet()){
			Tablet tablet = entry.getValue();
			if(i%3==1) {
				content += "<div class='special_grid_row'>";
			}
			String item = itemtemp;
			item = item.replace("$itemname$", tablet.getName())
								.replace("$image$", tablet.getImage())
								.replace("$oldprice$", String.valueOf(tablet.getPrice()))
								.replace("$newprice$", String.valueOf(tablet.getPrice()))
								.replace("$name$", entry.getKey())
								.replace("$type$", "tablets")
								.replace("$maker$", CategoryName);
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
