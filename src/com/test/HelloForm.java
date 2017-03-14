package com.test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class HelloForm extends HttpServlet {
	
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost:3306/one_controls";
	     static final String USER = "root";
	      static final String PASS = "Lifeis5*";
 
	      
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException {
		  
		   Connection conn = null;
		   Statement stmt = null;
		   try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
		      System.out.println("Connecting to database...");
		      try {
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      String FirstName = request.getParameter("first_name");
		      System.out.println("first name is123" + FirstName);
		      
		      String LastName = request.getParameter("last_name");
		      System.out.println("second name is123 "+ LastName);
		      try {
				PreparedStatement pst = (PreparedStatement) conn.prepareStatement("insert into users (first_name,last_name) value(?,?)");
			    pst.setString(1, FirstName);
			    pst.setString(2, LastName);
			    int i = pst.executeUpdate();
			    System.out.println("update status is" + i);
		       } catch (SQLException e) {
				System.out.println("we are in exception" + e);
				e.printStackTrace();
			}
		      PrintWriter out = response.getWriter();
		      out.println(" succss");
		      response.sendRedirect("/Hello.htm");
			  }
	  
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type
	  System.out.println("inside get");
	   Connection conn = null;
	   Statement stmt = null;
	   try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   
	      System.out.println("Connecting to database...");
	      try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      //STEP 4: Execute a query
	      System.out.println("Creating statement...");
	      try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      ResultSet rs = null;
	     String sql = "SELECT * FROM users";
	      try {
			 rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      String result = null;
	      try {
			while(rs.next()){
			     	String first = rs.getString("first_name");
			         String last = rs.getString("last_name");
			        result =result +  first + ","+last +",";
			         

			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      
      PrintWriter out = response.getWriter();

	    String text = "Update successfull get"; //message you will recieve 

	    String name = request.getParameter("txtUname");

	    out = response.getWriter();

	    out.println(result);

	    
	    
  }
}
