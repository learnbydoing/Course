/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author RZHUANG
 */
public class Search extends HttpServlet {
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String error_msg = "";
        
        HashMap<String, Student> hm = new HashMap<String, Student>();
        Student std1 = new Student("111", "Johnny", "Computer Science", "2241 S Archer Ave","3124786579","Miller",3.9);
        Student std2 = new Student("222", "Tony", "Art", "1st Jackson Ave","31200022254","Jack",3.1);
        Student std3 = new Student("333", "Jane", "Computer Science", "123 State Street","3124786579","Miller",3.4);
        hm.put("111", std1);
        hm.put("222", std2);
        hm.put("333", std3);
            
        String ssn = request.getParameter("ssn");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if(!hm.containsKey(ssn)) {
            error_msg = "No such student [" +ssn+ "]";
            String docType = "<!DOCTYPE html>\n";
            out.println(docType +
                        "<HTML>\n" +
                        "<HEAD><TITLE>Search Result</TITLE></HEAD>\n" +
                        "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                        "<H1>" + error_msg + "</H1>");
            out.println("</BODY></HTML>");
        } else {
            Student std = hm.get(ssn);
            String docType = "<!DOCTYPE html>\n";
            out.println(docType +
                        "<HTML>\n" +
                        "<HEAD><TITLE>Search Result</TITLE></HEAD>\n" +
                        "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                        "<H1>Student: " + ssn + "</H1>");             
            out.println("<h3>Name:"+std.getName()+"</h3>"); 
            out.println("<h3>Major:"+std.getMajor()+"</h3>"); 
            out.println("<h3>Address:"+std.getAddress()+"</h3>"); 
            out.println("<h3>Phone:"+std.getPhone()+"</h3>"); 
            out.println("<h3>Advisor:"+std.getAdvisor()+"</h3>"); 
            out.println("<h3>Advisor:"+std.getGpa()+"</h3>"); 
            out.println("</BODY></HTML>");
        }       
    }
    
    class Student {
        private String ssn;
	private String name;
	private String major;
	private String address;
	private String phone;
	private String advisor;
	private double  gpa;

	public Student(String ssn, String name, String major, String address, String phone, String advisor,double gpa){
		this.ssn = ssn;
		this.name = name;
		this.major = major;
		this.address = address;
		this.phone = phone;
		this.advisor = advisor;
		this.gpa = gpa;
	}

	public Student(){

	}

	public String getSsn() {
		return ssn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAdvisor() {
		return advisor;
	}
	public void setAdvisor(String advisor) {
		this.advisor = advisor;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}
    }


}
