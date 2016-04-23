

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AccessoryList")
public class AccessoryList extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		
		String CategoryName = request.getParameter("maker");
		String ConsoleName = request.getParameter("console");
		HashMap<String, Console> hm = new HashMap<String, Console>();
		if(CategoryName.equals("microsoft")){
			hm.putAll(ConsoleHashMap.microsoft);
		}
		else if(CategoryName.equals("sony")){
			hm.putAll(ConsoleHashMap.sony);
		}
		else if(CategoryName.equals("nintendo")){
			hm.putAll(ConsoleHashMap.nintendo);
		}
		
		Console console = hm.get(ConsoleName);
		
		Helper helper = new Helper(request,pw);
		helper.printHtml("site_header.html");
		helper.printHtml("site_sidebar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>"+ console.getName() +": Accessories</a>");
		pw.print("</h2><div class='entry'><table id='bestseller'>");
		int i = 1; int size= hm.size();
		for(Map.Entry<String, Accessory> entry : console.getAccessories().entrySet()){
			Accessory accessory = entry.getValue();
			if(i%3==1) pw.print("<tr>");
			pw.print("<td><div id='shop_item'>");
			pw.print("<h3>"+accessory.getName()+"</h3>");
			pw.print("<strong>"+accessory.getPrice()+"$</strong><ul>");
			pw.print("<li id='item'><img src='images/accessories/"+accessory.getImage()+"' alt='' /></li>");
			pw.print("<li><form method='post' action='Cart'>" +
					"<input type='hidden' name='name' value='"+entry.getKey()+"'>"+
					"<input type='hidden' name='type' value='accessories'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='access' value='"+ConsoleName+"'>"+
					"<input type='submit' class='btnbuy' value='Buy Now' href='#'></input></form></li>");
			pw.print("<li><a class='btnreview' href='Review?name="+entry.getKey()+"&type=accessories&maker="+CategoryName+"&access="+ConsoleName+"'>Reviews</a></li>");
			
			pw.print("</ul></div></td>");
			if(i%3==0 || i == size) pw.print("</tr>");
			i++;
		}		
		pw.print("</table></div></div></div>");		
		helper.printHtml("site_footer.html");
	}
}
