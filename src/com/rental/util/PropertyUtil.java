package com.rental.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private static final String PROPERTY_FILE = "db.properties";

	public static String getPropertyString() {
		Properties properties = new Properties();
		try (InputStream input = PropertyUtil.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
			if (input == null) {
				throw new RuntimeException("Unable to find " + PROPERTY_FILE);
			}
			properties.load(input);

			// Debugging: Print loaded properties and values
//			System.out.println("Loaded properties:");
//			properties.forEach((key, value) -> System.out.println(key + ": " + value));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String hostname = properties.getProperty("hostname");
		String port = properties.getProperty("port");
		String dbname = properties.getProperty("db.url");
		String username = properties.getProperty("db.user");
		String password = properties.getProperty("db.password");

		// Debugging: Print values after loading properties
//		System.out.println("Hostname: " + hostname);
//		System.out.println("Port: " + port);
//		System.out.println("DB URL: " + dbname);
//		System.out.println("Username: " + username);
//		System.out.println("Password: " + password);

		return "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password;

//		return "jdbc:mysql://" + dbname + "?user=" + username + "&password=" + password;
	}
}
