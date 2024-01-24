package com.rental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static Connection connection;

	private DBConnection() {
		// Private constructor to prevent instantiation
	}

	public static Connection getConnection() {
		try {
			String connectionString = PropertyUtil.getPropertyString();
			return DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			throw new RuntimeException("Error establishing a new database connection.", e);
		}
	}
}

//	public static Connection getConnection() {
//		if (connection == null) {
//			try {
//				String connectionString = PropertyUtil.getPropertyString();
//				connection = DriverManager.getConnection(connectionString);
//			} catch (SQLException e) {
//				throw new RuntimeException("Error establishing the database connection.", e);
//			}
//		}
//		return connection;
//	}
//
//	public static void closeConnection() {
//		if (connection != null) {
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				throw new RuntimeException("Error closing the database connection.", e);
//			} finally {
//				connection = null; // Set to null to allow reconnection if needed
//			}
//		}
//	}
