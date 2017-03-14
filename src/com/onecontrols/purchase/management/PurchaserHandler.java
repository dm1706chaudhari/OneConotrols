package com.onecontrols.purchase.management;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.onecontrols.usermanagement.ConnectionProvider;

/**
 * Servlet implementation class PurchaserHandler
 */
public class PurchaserHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchaserHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("inside the getting all quotation");
		 PrintWriter out = response.getWriter();

		      ResultSet rs = null;
		      try {
		    	  PreparedStatement statement = ConnectionProvider.getInstance().connection.prepareStatement("select * from purchase_data");  
			      rs = statement.executeQuery();
			      } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      String result = null;
		      try {
		    	        List<ObjectNode> nodes = new ArrayList<ObjectNode>(); 
		    	        while (rs.next()){
				        ObjectNode node = new ObjectMapper().createObjectNode();
				        node.put("organizationName", rs.getString("organizationName"));
				        node.put("purchaserEmail", rs.getString("purchaserEmail"));
				        node.put("purchaserContactNumber", rs.getString("purchaserContactNumber"));
				        node.put("PurchaserAddress", rs.getString("PurchaserAddress"));
				        nodes.add(node);
		    	        }
		    	        
				        result = nodes.toString();			  
			} catch (SQLException e) {
				System.out.println("error we get is" + e);
				e.printStackTrace();
			}

		      response.setContentType("application/json");
	    out = response.getWriter();

	    out.println(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("we are adding purchaser now");
        String organizationName = request.getParameter("organizationName");
        String purchaserEmail = request.getParameter("purchaserEmail");
        String purchaserContactNumber = request.getParameter("purchaserContactNumber");
        String PurchaserAddress = request.getParameter("PurchaserAddress");
        int i=0;
		      try {
				PreparedStatement pst = (PreparedStatement) ConnectionProvider.getInstance().connection.prepareStatement("insert into purchase_data (organizationName,purchaserEmail,purchaserContactNumber, PurchaserAddress) value(?,?,?,?)");
			    pst.setString(1, organizationName);
			    pst.setString(2, purchaserEmail);
			    pst.setString(3, purchaserContactNumber);
			    pst.setString(4, PurchaserAddress);
			     i = pst.executeUpdate();
			    System.out.println("update status is" + i);
		       } catch (SQLException e) {
				System.out.println("we are in exception" + e);
				e.printStackTrace();
			}
		    if(i==1){
		    	request.getSession().setAttribute("PurchaserUpdate", "Success");
		    }else {
		    	request.getSession().setAttribute("PurchaserUpdate", "Failed");
		    }
		      RequestDispatcher view =  request.getRequestDispatcher("next.jsp");
		      view.forward(request, response);	 
	}

}
