package com.onecontrols.usermanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionProvider {
	   public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   public static final String DB_URL = "jdbc:mysql://localhost:3306/one_controls";
	   public static final String USER = "root";
	   public static final String PASS = "Lifeis5*";
	   public Connection connection;
	   public static Statement statement;
       private static ConnectionProvider CONNECTION_INSTANCE = new ConnectionProvider();
	    public ConnectionProvider() {
	    	try {
	    		try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connection = DriverManager.getConnection(DB_URL,USER, PASS);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    public static ConnectionProvider getInstance () {
	    	return CONNECTION_INSTANCE;
	    }

}
