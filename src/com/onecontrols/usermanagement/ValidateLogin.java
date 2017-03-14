package com.onecontrols.usermanagement;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ValidateLogin
 */

public class ValidateLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ValidateLogin() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");
		String dbpassword = null;
		ResultSet rs = null;
		boolean valid = false;
		try {
			PreparedStatement statement = ConnectionProvider.getInstance().connection
					.prepareStatement("select * from users where userName = ?");
			statement.setString(1, userName);
			rs = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (rs.next()) {
				dbpassword = rs.getString("userPassword");
				if (dbpassword.equalsIgnoreCase(userPassword)) {
					valid = true;
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("db passsword is" + dbpassword + "userpassword is" + userPassword + "user name is" + userName
				+ "is Valid is" + valid);
		if (valid) {
			request.getSession().setAttribute("LoginSuccess", "Login");
			RequestDispatcher view = request.getRequestDispatcher("next.jsp");
            Cookie ck=new Cookie("name",userName);
            response.addCookie(ck);  
			view.forward(request, response);
		} else {
			request.getSession().setAttribute("LoginFailed", "Failed");
			RequestDispatcher view = request.getRequestDispatcher("index.jsp");
			view.forward(request, response);
		}

	}

}
