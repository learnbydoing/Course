import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Checkout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request, pw);
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String template = helper.getTemplate("checkout.html");
        template = template.replace("$username$", helper.username());
        helper.prepareContent(template);
        helper.prepareSideBar();
        helper.prepareFooter();
        helper.printHtml();        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
