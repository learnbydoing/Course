import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Helper {
	HttpServletRequest req;
	PrintWriter pw;
	String url;
	HttpSession session;
	String _layout;
	String _header;
	String _menu;
	String _content;
	String _sidebar;
	String _footer;

	public Helper(HttpServletRequest req, PrintWriter pw) {
		this.req = req;
		this.pw = pw;
		this.url = this.getFullURL();
		this.session = req.getSession(true);
	}

	public void prepareLayout() {
		_layout = HtmlToString("_layout.html");
		_header = "";
		_menu = "";
		_content = "";
		_sidebar = "";
		_footer = "";
	}
	public void prepareHeader() {
		_header = HtmlToString("site_header.html");
	}
	public void prepareMenu() {
		_menu = HtmlToString("site_menu.html");
		String menuitem = "";
		if (session.getAttribute("username")!=null){
			String username = session.getAttribute("username").toString();
			username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
			menuitem = menuitem
					+ "<ul>"
					+ "	<li><a>Hello, "+username+"</a></li>"
					+ "	<li><a href='Account'>Account</a></li>"
					+ "	<li><a href='Logout'>Logout</a></li>"
					+ "	<li><a href='Cart'>Cart("+CartCount()+")</a></li>"
					+ "</ul>";
		}
		else {
			menuitem = menuitem
					+ "<ul>"
					+ "	<li><a href='Login'>Login</a></li>"
					+ "	<li><a href='Cart'>Cart(0)</a></li>"
					+ "</ul>";
		}
		_menu = _menu.replace("$usermenu$", menuitem);
	}
	public void prepareContent(String content) {
		_content = content;
	}
	public void prepareSideBar() {
		_sidebar = HtmlToString("site_sidebar.html");
	}
	public void prepareFooter() {
		_footer = HtmlToString("site_footer.html");
	}
	public void printHtml() {
		_layout =  _layout.replace("$header$", _header)
											.replace("$menu$", _menu)
											.replace("$content$", _content)
											.replace("$sidebar$", _sidebar)
											.replace("$footer$", _footer);
		pw.print(_layout);
	}
	public String getTemplate(String file) {
		return HtmlToString(file);
	}
	public void printHtml(String file) {

		String result = HtmlToString(file);
		if (file == "site_header.html") {
			if (session.getAttribute("username")!=null){
				String username = session.getAttribute("username").toString();
				username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
				result = result
						+ "<li><a>Hello, "+username+"</a></li>"
						+ "<li><a href='Account'>Account</a></li>"
						+ "<li><a href='Logout'>Logout</a></li>";
			}
			else
				result = result + "<li><a href='Login'>Login</a></li>";
			result = result
					+ "<li><a href='Cart'>Cart("+CartCount()+")</a></li></ul></div></div><div id='page'>";
			pw.print(result);
		} else
			pw.print(result);
	}


	public String getFullURL() {
		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		url.append("/");
		return url.toString();
	}


	public String HtmlToString(String file) {
		String result = null;
		try {
			String webPage = url + file;
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (Exception e) {
		}
		return result;
	}


	public void logout(){
		session.removeAttribute("username");
		session.removeAttribute("usertype");
	}

	public boolean isLoggedin(){
		if (session.getAttribute("username")==null)
			return false;
		return true;
	}

	public String username(){
		if (session.getAttribute("username")!=null)
			return session.getAttribute("username").toString();
		return null;
	}

	public String usertype(){
		if (session.getAttribute("usertype")!=null)
			return session.getAttribute("usertype").toString();
		return null;
	}

	public User getUser(){
		String usertype = usertype();
		HashMap<String, User> hm = new HashMap<String, User>();
		if (usertype.equals("customer")) {
			hm.putAll(UserHashMap.customer);
		} else if (usertype.equals("retailer")) {
			hm.putAll(UserHashMap.retailer);
		} else if (usertype.equals("manager")) {
			hm.putAll(UserHashMap.manager);
		}
		User user = hm.get(username());
		return user;
	}

	public ArrayList<OrderItem> getCustomerOrders(){
		ArrayList<OrderItem> order = new ArrayList<OrderItem>();
		if(OrdersHashMap.orders.containsKey(username()))
			order= OrdersHashMap.orders.get(username());
		return order;
	}



	public int CartCount(){
		if(isLoggedin())
		return getCustomerOrders().size();
		return 0;
	}




	public void storeProduct(String name,String type,String maker, String acc){
		if(!OrdersHashMap.orders.containsKey(username())){
			ArrayList<OrderItem> arr = new ArrayList<OrderItem>();
			OrdersHashMap.orders.put(username(), arr);
		}

		ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());

		if(type.equals("consoles")){
			Console console = null;
			if(maker.equals("microsoft")){
				console = ConsoleHashMap.microsoft.get(name);
			}
			else if(maker.equals("sony")){
				console = ConsoleHashMap.sony.get(name);
			}
			else if(maker.equals("nintendo")){
				console = ConsoleHashMap.nintendo.get(name);
			}else{
				HashMap<String, Console> hm = new HashMap<String, Console>();
				hm.putAll(ConsoleHashMap.microsoft);
				hm.putAll(ConsoleHashMap.sony);
				hm.putAll(ConsoleHashMap.nintendo);
				console = hm.get(name);

			}
			OrderItem orderitem = new OrderItem(console.getName(), console.getPrice(), console.getImage(), console.getRetailer());
			orderItems.add(orderitem);
		}
		if(type.equals("games")){
			Game game = null;
			if(maker.equals("electronicArts")){
				game = GameHashMap.electronicArts.get(name);
			}
			else if(maker.equals("activision")){
				game = GameHashMap.activision.get(name);
			}
			else if(maker.equals("takeTwoInteractive")){
				game = GameHashMap.takeTwoInteractive.get(name);
			}else{
				HashMap<String, Game> hm = new HashMap<String, Game>();
				hm.putAll(GameHashMap.electronicArts);
				hm.putAll(GameHashMap.activision);
				hm.putAll(GameHashMap.takeTwoInteractive);
				game = hm.get(name);
			}
			OrderItem orderitem = new OrderItem(game.getName(), game.getPrice(), game.getImage(), game.getRetailer());
			orderItems.add(orderitem);
		}

		if(type.equals("tablets")){
			Tablet tablet = null;
			if (maker.equals("apple")) {
				tablet = TabletHashMap.apple.get(name);
			} else if (maker.equals("microsoft")) {
				tablet = TabletHashMap.microsoft.get(name);
			} else if (maker.equals("samsung")) {
				tablet = TabletHashMap.samsung.get(name);
			}else{
				HashMap<String, Tablet> hm = new HashMap<String, Tablet>();
				hm.putAll(TabletHashMap.apple);
				hm.putAll(TabletHashMap.microsoft);
				hm.putAll(TabletHashMap.samsung);
				tablet = hm.get(name);
			}
			OrderItem orderitem = new OrderItem(tablet.getName(), tablet.getPrice(), tablet.getImage(), tablet.getRetailer());
			orderItems.add(orderitem);
		}

		if(type.equals("accessories")){
			Console console = null;
			if(maker.equals("microsoft")){
				console = ConsoleHashMap.microsoft.get(acc);
			}
			else if(maker.equals("sony")){
				console = ConsoleHashMap.sony.get(acc);
			}
			else if(maker.equals("nintendo")){
				console = ConsoleHashMap.nintendo.get(acc);
			}

			Accessory accessory = console.getAccessories().get(name);
			OrderItem orderitem = new OrderItem(accessory.getName(), accessory.getPrice(), accessory.getImage(), accessory.getRetailer());
			orderItems.add(orderitem);
		}

	}

	public String currentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}

	public HashMap<String, Console> getConsoles(){
			HashMap<String, Console> hm = new HashMap<String, Console>();
			hm.putAll(ConsoleHashMap.microsoft);
			hm.putAll(ConsoleHashMap.sony);
			hm.putAll(ConsoleHashMap.nintendo);
			return hm;
	}

	public HashMap<String, Game> getGames(){
		HashMap<String, Game> hm = new HashMap<String, Game>();
			hm.putAll(GameHashMap.electronicArts);
			hm.putAll(GameHashMap.activision);
			hm.putAll(GameHashMap.takeTwoInteractive);
			return hm;
	}

	public HashMap<String, Tablet> getTablets(){
			HashMap<String, Tablet> hm = new HashMap<String, Tablet>();
			hm.putAll(TabletHashMap.apple);
			hm.putAll(TabletHashMap.microsoft);
			hm.putAll(TabletHashMap.samsung);
			return hm;
	}

	public ArrayList<String> getProducts(){
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, Console> entry : getConsoles().entrySet()){
			ar.add(entry.getValue().getName());
		}

		return ar;
	}

	public ArrayList<String> getProductsGame(){
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, Game> entry : getGames().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}

	public ArrayList<String> getProductsTablets(){
		ArrayList<String> ar = new ArrayList<String>();
		for(Map.Entry<String, Tablet> entry : getTablets().entrySet()){
			ar.add(entry.getValue().getName());
		}
		return ar;
	}



}
