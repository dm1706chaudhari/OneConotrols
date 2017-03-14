package com.onecontrols.usermanagement;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ResetPassword
 */
public class ResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResetPassword() {
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
		System.out.println("in reset password123");
		String userName = null;
		String userPassword = null;
		String userPasswordDb = null;
		String newPassword = null;
		String reEntredNewPassword = null;
		userName = request.getParameter("userName");
		userPassword = request.getParameter("currentPassword");
		newPassword = request.getParameter("newPassword");
		reEntredNewPassword = request.getParameter("reEnteredPassword");

		boolean arePasswordEqulas = false;
		if (newPassword.equals(reEntredNewPassword)) {
			arePasswordEqulas = true;
		}
		if (arePasswordEqulas) {

			try {
				ResultSet rs = null;
				PreparedStatement statement = ConnectionProvider.getInstance().connection
						.prepareStatement("select * from users where userName = ?");
				statement.setString(1, userName);
				rs = statement.executeQuery();
				while (rs.next()) {
					userPasswordDb = rs.getString("userPassword");
				}
				if (userPasswordDb.equals(userPassword)) {
					statement = ConnectionProvider.getInstance().connection
							.prepareStatement("update users set userPassword = ? where userName = ?");
					statement.setString(1, newPassword);
					statement.setString(2, userName);
					statement.executeUpdate();
					request.getSession().setAttribute("PassResetSuccessfuly", "PassResetSuccessfuly");
					RequestDispatcher view = request.getRequestDispatcher("index.jsp");
					view.forward(request, response);
				} else {
					request.getSession().setAttribute("WrongCurrentPassword", "WrongCurrentPassword");
				}
			} catch (Exception e) {
				request.getSession().setAttribute("PassResetFailed", "PassResetFailed");
			}
		} else {
			request.getSession().setAttribute("ReenterPasswordDoesNotMatch", "ReenterPasswordDoesNotMatch");
		}
		RequestDispatcher view = request.getRequestDispatcher("ResetPassword.jsp");
		view.forward(request, response);
		

	}

}
