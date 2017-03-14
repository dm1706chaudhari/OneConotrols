package com.onecontrols.quotation.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.onecontrols.usermanagement.ConnectionProvider;

/**
 * Servlet implementation class AddQuotation
 */

public class AddQuotation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static int QuatationCounter = 0;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddQuotation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("inside the getting all quotation");
		PrintWriter out = response.getWriter();
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		String dropdown = "<select id = %ID%> <option value=\"select\">select</option>";
		
		try {
			PreparedStatement statement = ConnectionProvider.getInstance().connection
					.prepareStatement("select * from purchase_data");
			rs = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			while (rs.next()) {
				dropdown = dropdown + "<option id=\"" +rs.getInt("purchaserId") + "\"" +" value=\"" +rs.getInt("purchaserId")+"\">"+rs.getString("organizationName")+"</option>";
			}
			dropdown = dropdown + "</select>";
			System.out.println("dropdown value is" +dropdown );
		} catch (SQLException e) {
			System.out.println("error we get is" + e);
			e.printStackTrace();
		}
		

		try {
			PreparedStatement statement = ConnectionProvider.getInstance().connection
					.prepareStatement("select * from quotation_data");
			rs = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = null;
		PreparedStatement statement = null;
		try {
			String dropdown1 = null;
			int customerId = 0;
			String CustomerName = null;
			List<ObjectNode> nodes = new ArrayList<ObjectNode>();
			while (rs.next()) {
				ObjectNode node = new ObjectMapper().createObjectNode();
				customerId = rs.getInt("customerId");
				statement = ConnectionProvider.getInstance().connection.prepareStatement(
						"select * from customer_data where customerId = ?");
				statement.setInt(1, customerId);
				rs1 = statement.executeQuery();
				while (rs1.next()) {
					CustomerName = rs1.getString("CustomerName");
				}
				node.put("CustomerName", CustomerName);
				node.put("product", rs.getString("product"));
				node.put("perticulars", rs.getString("perticulars"));
				node.put("quantity", rs.getString("quantity"));
				node.put("status", rs.getString("status"));
				node.put("purchaseName", rs.getString("purchaserName"));
				node.put("prise", rs.getString("prise"));
				dropdown1 = dropdown.replace("%ID%", rs.getString("purchaseId"));
				node.put("dropdown", dropdown1);
				node.put("ID", rs.getString("purchaseId"));
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("we are processing quotation now");
		String CustomerName = request.getParameter("CustomerName");
		String product = request.getParameter("product");
		String perticulars = request.getParameter("perticulars");
		String AddressOfCustomer = request.getParameter("AddressOfCustomer");
		String ContactNumbers = request.getParameter("ContactNumbers");
		String EmailAddress = request.getParameter("EmailAddress");
		String quantity = request.getParameter("quantity");
		String product1 = request.getParameter("txtbox1");
		String perticulars1 = request.getParameter("txtbox2");
		String quantity1 = request.getParameter("txtbox3");
		int i = 0;
		int[] i1 = null;

		try {
			PreparedStatement pst = (PreparedStatement) ConnectionProvider.getInstance().connection.prepareStatement(
					"insert into customer_data (CustomerName,AddressOfCustomer,ContactNumbers,EmailAddress) value(?,?,?,?)");
			pst.setString(1, CustomerName);
			pst.setString(2, AddressOfCustomer);
			pst.setString(3, ContactNumbers);
			pst.setString(4, EmailAddress);
			pst.addBatch();
			i1 = pst.executeBatch();
		} catch (SQLException e) {
			System.out.println("we are in exception" + e);
			e.printStackTrace();
		}
		if (i1.length > 0) {
			request.getSession().setAttribute("QuotationUpdate", "Success");
		} else {
			request.getSession().setAttribute("QuotationUpdate", "Failed");
		}
		String customerId = null;
		PreparedStatement statement;
		try {
			statement = ConnectionProvider.getInstance().connection.prepareStatement(
					"select * from customer_data where CustomerName = ? and ContactNumbers = ? and EmailAddress = ?");

			statement.setString(1, CustomerName);
			statement.setString(2, ContactNumbers);
			statement.setString(3, EmailAddress);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				customerId = rs.getString("customerId");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			PreparedStatement pst = (PreparedStatement) ConnectionProvider.getInstance().connection.prepareStatement(
					"insert into quotation_data (customerId,product,perticulars, quantity,status) value(?,?,?,?,?)");
			pst.setString(1, customerId);
			pst.setString(2, product);
			pst.setString(3, perticulars);
			pst.setString(4, quantity);
			pst.setInt(5, 0);
			pst.addBatch();
			String product2 = null;
			for (i = 0; i <= 30;) {
				product2 = request.getParameter("txtbox" + i++);
				if (product2 == null || product2.isEmpty()) {
					i = i + 3;
					continue;
				}
				perticulars = request.getParameter("txtbox" + i++);
				quantity = request.getParameter("txtbox" + i++);
				pst.setString(2, product2);
				pst.setString(3, perticulars);
				pst.setString(4, quantity);
				pst.addBatch();
			}

			i1 = pst.executeBatch();
			System.out.println("update status is" + i1);
		} catch (SQLException e) {
			System.out.println("we are in exception" + e);
			e.printStackTrace();
		}
		if (i1.length > 0) {
			request.getSession().setAttribute("QuotationUpdate", "Success");
		} else {
			request.getSession().setAttribute("QuotationUpdate", "Failed");
		}





		RequestDispatcher view = request.getRequestDispatcher("next.jsp");
		view.forward(request, response);

	}

}
