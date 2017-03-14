package com.onecontrols.quotation.management;

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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.onecontrols.usermanagement.ConnectionProvider;

/**
 * Servlet implementation class UpdateQuotation
 */
public class UpdateQuotation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateQuotation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	private static int QuatationCounter = 0;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in post method of update quatation");
		String id = null;
		 if (request.getParameterMap().containsKey("ID")) {
			 id = request.getParameter("ID");
		 }
		 int status = 0;
		 if (request.getParameterMap().containsKey("status")) {
		 status = Integer.parseInt(request.getParameter("status"));
		 }
		 int prise =0;
		 if (request.getParameterMap().containsKey("priseAdded")) {
		 prise = Integer.parseInt(request.getParameter("priseAdded"));
		 }
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement statement = null;
		try {
			 statement = ConnectionProvider.getInstance().connection
					.prepareStatement("update quotation_data set status = ?, prise = ? where purchaseId = ?");
			statement.setInt(1, status);
			statement.setInt(2, prise);
			statement.setString(3, id);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		int doNotProcess = 1;
		int customerId = 0;
		String CustomerName = null; 
		String AddressOfCustomer = null;
		String ContactNumbers = null;
		String EmailAddress = null;
		if(status == 2){
			try {
				statement = ConnectionProvider.getInstance().connection.prepareStatement(
						"select * from quotation_data where purchaseId = ?");
				statement.setString(1, id);
				rs = statement.executeQuery();
				while (rs.next()) {
					customerId = rs.getInt("customerId");
				}
				
				statement = ConnectionProvider.getInstance().connection.prepareStatement(
						"select * from customer_data where customerId = ?");
				statement.setInt(1, customerId);
				rs = statement.executeQuery();
				while (rs.next()) {
					CustomerName = rs.getString("CustomerName");
					AddressOfCustomer = rs.getString("AddressOfCustomer");
					ContactNumbers = rs.getString("ContactNumbers");
					EmailAddress = rs.getString("EmailAddress");
				}
				rs1 = null;
				statement = ConnectionProvider.getInstance().connection.prepareStatement(
						"select * from quotation_data where customerId = ?");
				statement.setInt(1, customerId);
				rs1 = statement.executeQuery();
				while (rs1.next()) {
					status = rs1.getInt("status");
					if(status < 2){
						doNotProcess = 1;
						break;
					} else{
						doNotProcess = 0;
					}
				}
				if(doNotProcess == 0){
					
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
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(EmailAddress));
						message.addRecipient(Message.RecipientType.CC, new InternetAddress("sachin@onecontrols.com"));
						message.addRecipient(Message.RecipientType.CC, new InternetAddress("dm1706chaudhari@gmail.com"));
						// message.addRecipient(Message.RecipientType.CC,new
						// InternetAddress("sales@onecontrols.com"));
						message.setSubject("Quotation information at onecontrols");
						// message.setContent();
						String messagetest = "Hi, \nThanks for choosing one controls, your qoutation information is as below attached with file\n"
								+ "\n" + "Please contact onecontrols team in case of discrepancy" + "\nThanks & Regards"
								+ "\n Sachin Patil";
						messageBodyPart.setText(messagetest);

						Multipart multipart = new MimeMultipart();

						// Set text message part
						multipart.addBodyPart(messageBodyPart);

						FileInputStream fis = new FileInputStream(new File("/Users/DineshChaudhari/Downloads/quotationToCustomer.xls"));
						HSSFWorkbook wb = new HSSFWorkbook(fis);
						HSSFSheet worksheet = wb.getSheetAt(0);
						Cell cell1 = null;
						String[] arr = AddressOfCustomer.split(",");

						int row =12;
						for (String add : arr) {
						cell1 = worksheet.getRow(row++).getCell(0);
						cell1.setCellValue(add);
						}

						String customer = CustomerName + " - " + ContactNumbers;

						cell1 = worksheet.getRow(18).getCell(1);
						cell1.setCellValue(customer);

						SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
						Date now = new Date();
						String strDate = sdfDate.format(now);

						cell1 = worksheet.getRow(7).getCell(6);
						cell1.setCellValue(strDate);

						cell1 = worksheet.getRow(8).getCell(6);
						cell1.setCellValue(QuatationCounter++);

						int row1 = 23;
						rs1 = statement.executeQuery();
						while (rs1.next()) {
							
							String product = rs1.getString("product");
							String perticulars = rs1.getString("perticulars");
							int quantity = rs1.getInt("quantity");
							int prise1 = rs1.getInt("prise");
							int purchaseId = rs1.getInt("purchaseId");
							cell1 = worksheet.getRow(row1).getCell(0);
							cell1.setCellValue(product);
							cell1 = worksheet.getRow(row1).getCell(2);
							cell1.setCellValue(perticulars);
							cell1 = worksheet.getRow(row1).getCell(5);
							cell1.setCellValue(quantity);
							cell1 = worksheet.getRow(row1).getCell(6);
							cell1.setCellValue(prise1);
							
							try {
								 statement = ConnectionProvider.getInstance().connection
										.prepareStatement("update quotation_data set status = ? where purchaseId = ?");
								 status = 3;
								statement.setInt(1, status);
								statement.setInt(2, purchaseId);
								statement.executeUpdate();
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							row1++;
						}

						statement.close();
						fis.close();
						FileOutputStream output_file = new FileOutputStream(
								new File("/Users/DineshChaudhari/Downloads/quotationToCustomerOutput.xls"));
						wb.write(output_file);
						output_file.close();
						messageBodyPart = new MimeBodyPart();
						String filename = "/Users/DineshChaudhari/Downloads/quotationToCustomerOutput.xls";
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

				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
