package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for database connection management. Handles loading database
 * configuration from properties file and provides connections to the database.
 */
public class DbConnectionUtil {

	// Database connection properties
	private static String url;
	private static String user;
	private static String password;

	// Property file location
	private static final String PROPERTIES_FILE = "db.properties";

	// Property keys
	private static final String DRIVER_KEY = "db.driver";
	private static final String URL_KEY = "db.url";
	private static final String USERNAME_KEY = "db.username";
	private static final String PASSWORD_KEY = "db.password";

	/**
	 * Static initialization block. Loads database connection properties from the
	 * properties file when the class is first loaded.
	 */
	static {
		Properties prop = new Properties();

		try (InputStream inputStream = DbConnectionUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {

			// Check if properties file exists
			if (inputStream == null) {
				System.err.println("Unable to find " + PROPERTIES_FILE);
			} else {
				// Load properties
				prop.load(inputStream);

				// Load JDBC driver
				String driverClass = prop.getProperty(DRIVER_KEY);
				Class.forName(driverClass);

				// Set connection properties
				url = prop.getProperty(URL_KEY);
				user = prop.getProperty(USERNAME_KEY);
				password = prop.getProperty(PASSWORD_KEY);

				System.out.println("Database connection properties loaded successfully");
			}

		} catch (ClassNotFoundException e) {
			System.err.println("Database driver not found: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error loading database properties: " + e.getMessage());
		}
	}

	/**
	 * Private constructor to prevent instantiation of utility class.
	 */
	private DbConnectionUtil() {
		// Utility class should not be instantiated
	}

	/**
	 * Provides a connection to the database using the loaded configuration.
	 *
	 * @return A new Connection to the database
	 * @throws SQLException if a database access error occurs
	 */
	public static Connection getConnection() throws SQLException {
		if (url == null || user == null || password == null) {
			throw new SQLException("Database connection properties not properly initialized");
		}
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * Safely closes a database connection.
	 *
	 * @param connection The connection to close
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error closing database connection: " + e.getMessage());
			}
		}
	}
}