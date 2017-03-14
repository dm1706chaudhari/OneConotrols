package com.onecontrols.purchase.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.mail.Transport;
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

import com.onecontrols.usermanagement.ConnectionProvider;

/**
 * Servlet implementation class PurchaserSendEmail1
 */

public class PurchaserSendEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchaserSendEmail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String perchaserId = request.getParameter("purchaserID");
		String productId = request.getParameter("ID");
		System.out.println("purchase id and product id " + perchaserId + " " +productId );
		PreparedStatement statement;
		String purchaserEmail = null;
		String purchaserAddress =null;
		String purchaserContactNumber = null;
		String purchaserOrgName =null;
		try {
			statement = ConnectionProvider.getInstance().connection.prepareStatement(
					"select * from purchase_data where purchaserId = ?");

			statement.setString(1, perchaserId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				purchaserEmail = rs.getString("purchaserEmail");
				purchaserAddress = rs.getString("PurchaserAddress");
				purchaserContactNumber = rs.getString("purchaserContactNumber");
				purchaserOrgName = rs.getString("organizationName");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String productname =null;
		String perticulars =null;
		String quantity =null;
		try {
			statement = ConnectionProvider.getInstance().connection.prepareStatement(
					"select * from quotation_data where purchaseId = ?");

			statement.setString(1, productId);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				productname = rs.getString("product");
				perticulars = rs.getString("perticulars");
				quantity = rs.getString("quantity");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			 statement = ConnectionProvider.getInstance().connection
					.prepareStatement("update quotation_data set purchaserName = ? where purchaseId = ?");
			statement.setString(1, purchaserOrgName);
			statement.setString(2, productId);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("sachin@onecontrols.com", "Jaimatadi1");// change
																							// accordingly
			}
		});

		// compose message
		try {
			MimeMessage message = new MimeMessage(session);

			BodyPart messageBodyPart = new MimeBodyPart();

			message.setFrom(new InternetAddress("sachin@onecontrols.com"));// change
																			// accordingly
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(purchaserEmail));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("sachin@onecontrols.com"));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress("dm1706chaudhari@gmail.com"));
			// message.addRecipient(Message.RecipientType.CC,new
			// InternetAddress("sales@onecontrols.com"));
			message.setSubject("Quotation for below product required");
			// message.setContent();
			String messagetest = "Hi, \nWe are in requirement of the product below with attachement\n"
					+ "\n" + "It will be great if could provide as the details of prices for  mentioned product." + "\nThanks & Regards"
					+ "\n Sachin Patil";
			messageBodyPart.setText(messagetest);

			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			FileInputStream fis = new FileInputStream(new File("/Users/DineshChaudhari/Downloads/purchaserQuotation.xls"));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet worksheet = wb.getSheetAt(0);
			Cell cell1 = null;
			String[] arr = purchaserAddress.split(",");

			int row =12;
			for (String add : arr) {
			cell1 = worksheet.getRow(row++).getCell(0);
			cell1.setCellValue(add);
			}

			String customer = purchaserOrgName + " - " + purchaserContactNumber;

			cell1 = worksheet.getRow(18).getCell(1);
			cell1.setCellValue(customer);

			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
			Date now = new Date();
			String strDate = sdfDate.format(now);

			cell1 = worksheet.getRow(7).getCell(6);
			cell1.setCellValue(strDate);

			cell1 = worksheet.getRow(23).getCell(0);
			cell1.setCellValue(productname);

			cell1 = worksheet.getRow(23).getCell(2);
			cell1.setCellValue(perticulars);

			cell1 = worksheet.getRow(23).getCell(5);
			cell1.setCellValue(quantity);

			fis.close();
			FileOutputStream output_file = new FileOutputStream(
					new File("/Users/DineshChaudhari/Downloads/purchaserXls.xls"));
			wb.write(output_file);
			output_file.close();
			messageBodyPart = new MimeBodyPart();
			String filename = "/Users/DineshChaudhari/Downloads/purchaserXls.xls";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// send message

			Transport.send(message);

			System.out.println("message sent successfully");

		} catch (MessagingException e) {
			request.getSession().setAttribute("SendEmailFailed", "SendEmailFailed");
			throw new RuntimeException(e);
		}
		
		RequestDispatcher view = request.getRequestDispatcher("next.jsp");
		view.forward(request, response);
		
		
	}

}
