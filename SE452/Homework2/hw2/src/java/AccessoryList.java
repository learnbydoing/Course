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

        String makerName = request.getParameter("maker");
        makerName = makerName == null ? "" : makerName;
        String consoleName = request.getParameter("console");

        Helper helper = new Helper(request,pw);
        HashMap<String, Console> hm = helper.getConsoles(makerName);
        Console console = hm.get(consoleName);
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String itemtemp = helper.getTemplate("shopping_item2.html");
        String content = "";
        content += "<section id='content'>";
        content += "  <h3>"+console.getName()+": Accessories</h3>";
        int i = 1; int size= console.getAccessories().size();
        for(Map.Entry<String, Accessory> entry : console.getAccessories().entrySet()){
            Accessory accessory = entry.getValue();
            if(i%3==1) {
                content += "<div class='special_grid_row'>";
            }
            String item = itemtemp;
            item = item.replace("$itemname$", accessory.getName())
                       .replace("$image$", accessory.getImage())
                       .replace("$oldprice$", String.valueOf(accessory.getPrice()))
                       .replace("$newprice$", String.valueOf(accessory.getPrice()))
                       .replace("$id$", accessory.getKey())
                       .replace("$name$", accessory.getName())
                       .replace("$type$", "2")
                       .replace("$maker$", makerName)
                       .replace("$access$", consoleName);
            content += item;
            if(i%3==0 || i == size) {
                content += "</div>";
            }
            i++;
        }
        content += "  <div class='clear'></div>";
        content += "</section>";
        helper.prepareContent(content);
        helper.prepareSideBar();
        helper.prepareFooter();
        helper.printHtml();
    }
}
