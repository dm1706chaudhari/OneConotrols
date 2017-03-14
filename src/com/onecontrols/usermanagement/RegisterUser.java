package com.onecontrols.usermanagement;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterUser
 */
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
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

          String userName = request.getParameter("userName");
          String userPassword = request.getParameter("userPassword");
          String reEnteredUserPassword = request.getParameter("reEnteredUserPassword");
          String userEmail = request.getParameter("userEmail");
          String reEnteredUserEmail = request.getParameter("reEnteredUserEmail");
          String userAddress = request.getParameter("userAddress");
          String userGender = request.getParameter("userGender");
          String userPhonenumber = request.getParameter("userNumber");
          Statement stmt = null;
          int userId = 0 ;
          String isAdmin = null;		      
		      try {
				PreparedStatement pst = (PreparedStatement) ConnectionProvider.getInstance().connection.prepareStatement("insert into users (userName,userPassword,isAdmin) value(?,?,?)");
			    pst.setString(1, userName);
			    pst.setString(2, userPassword);
			    pst.setInt(3, 0);
			    int i = pst.executeUpdate();
			    System.out.println("update status is" + i);
		       } catch (SQLException e) {
				System.out.println("we are in exception" + e);
				e.printStackTrace();
			}
		      try {
				stmt = ConnectionProvider.getInstance().connection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		      if(!userEmail.equals(reEnteredUserEmail)) {
		    	  request.getSession().setAttribute("reEnteredUserEmaildWrong","reEnteredUserEmaildWrong") ;

		      }
		      if(!userPassword.equals(reEnteredUserPassword)) {
		    	  request.getSession().setAttribute("reEnteredUserPasswordWrong","reEnteredUserPasswordWrong") ;
		      }
		      if (userPassword.equals(reEnteredUserPassword) && userEmail.equals(reEnteredUserEmail)) {
		      ResultSet rs = null;
			     //String sql = "SELECT * FROM users where userName="+userName+" and userPassword="+userPassword;
			     try {
			     PreparedStatement statement = ConnectionProvider.getInstance().connection.prepareStatement("select * from users where userName = ? and userPassword = ?");    
			     statement.setString(1, userName);
			     statement.setString(2, userPassword);
			      rs = statement.executeQuery();
					while(rs.next()){
					       isAdmin = rs.getString("isAdmin");
					          userId = rs.getInt("userId");	         

					  }
					PreparedStatement pst = (PreparedStatement) ConnectionProvider.getInstance().connection.prepareStatement("insert into users_data (userId,email,phoneNumber,address,gender) value(?,?,?,?,?)");
				    pst.setInt(1, userId);
				    pst.setString(2, userEmail);
				    pst.setString(3, userPhonenumber);
				    pst.setString(4, userAddress);
				    pst.setString(5, userGender);
				    int i = pst.executeUpdate();
				    System.out.println("update status is" + i);
				    request.getSession().setAttribute("SUCCESSFULL","success") ;
				} catch (SQLException e) {
					request.getSession().setAttribute("Failed","Failed") ;
					e.printStackTrace();
				}
		      }
			      RequestDispatcher view =  request.getRequestDispatcher("index.jsp");
			      view.forward(request, response);	      
			  	}

}
