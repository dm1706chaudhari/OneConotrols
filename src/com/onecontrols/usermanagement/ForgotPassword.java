package com.onecontrols.usermanagement;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForgotPassword
 */

public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ForgotPassword() {
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
		System.out.println("in get method of forgot password12334534");
		String userName = request.getParameter("userName");
		String userPassword = null;
		String email = null;
		int userId = 0;
	     try {
	    	 ResultSet rs = null;
		     PreparedStatement statement = ConnectionProvider.getInstance().connection.prepareStatement("select * from users where userName = ?");    
		     statement.setString(1, userName);
		      rs = statement.executeQuery();
				while(rs.next()){
					userPassword = rs.getString("userPassword");
					userId = rs.getInt("userId");
				  }
			     statement = ConnectionProvider.getInstance().connection.prepareStatement("select * from users_data where userId = ?");    
			     statement.setInt(1, userId);
			     rs = statement.executeQuery();
				while(rs.next()){
					email = rs.getString("email");
				}
				
	     } catch(Exception e) {
	    	 
	     }
	     String to= email;//change accordingly  
	     
	     //Get the session object  
	     Properties props = new Properties();  
	     props.put("mail.smtp.host", "smtp.gmail.com");  
	     props.put("mail.smtp.socketFactory.port", "465");  
	     props.put("mail.smtp.socketFactory.class",  
	               "javax.net.ssl.SSLSocketFactory");  
	     props.put("mail.smtp.auth", "true");  
	     props.put("mail.smtp.port", "465");  
	      
	     Session session = Session.getDefaultInstance(props,  
	      new javax.mail.Authenticator() {  
	      protected PasswordAuthentication getPasswordAuthentication() {  
	      return new PasswordAuthentication("sachin@onecontrols.com","Jaimatadi1");//change accordingly  
	      }  
	     });  
	      
	     //compose message  
	     try {  
	      MimeMessage message = new MimeMessage(session);  
	      message.setFrom(new InternetAddress("sachin@onecontrols.com"));//change accordingly  
	      message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	      message.setSubject("Password for you application");  
	      message.setText("Hi password for your account " + userName + " is " + userPassword);  
	        
	      //send message  
	      Transport.send(message);  
	     
	      System.out.println("message sent successfully");  
	      
	     } catch (MessagingException e) {
	    	 request.getSession().setAttribute("SendEmailFailed","SendEmailFailed") ;
	    	 throw new RuntimeException(e);}  
	     
	     request.getSession().setAttribute("SendEmailSuccess","SendEmailSuccess") ;
	      RequestDispatcher view =  request.getRequestDispatcher("index.jsp");
	      view.forward(request, response);	
	      
	    }  
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
