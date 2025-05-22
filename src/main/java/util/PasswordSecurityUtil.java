package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password encryption and validation using BCrypt.
 */
public final class PasswordSecurityUtil {

	// BCrypt workload (log rounds) to control hashing complexity
	private static final int WORKLOAD_FACTOR = 12;

	// Private constructor to prevent instantiation
	private PasswordSecurityUtil() {
	}

	/**
	 * Encrypts a raw password using the BCrypt hashing algorithm.
	 */
	public static String encryptPassword(String rawPassword) {
		if (rawPassword == null || rawPassword.isEmpty()) {
			throw new IllegalArgumentException("Raw password cannot be null or empty");
		}

		String salt = BCrypt.gensalt(WORKLOAD_FACTOR);
		return BCrypt.hashpw(rawPassword, salt);
	}

	/**
	 * Verifies a raw password against a hashed password.
	 */
	public static boolean validatePassword(String rawPassword, String encryptedPassword) {
		if (rawPassword == null || rawPassword.isEmpty() || encryptedPassword == null || encryptedPassword.isEmpty()) {
			return false;
		}

		try {
			return BCrypt.checkpw(rawPassword, encryptedPassword);
		} catch (IllegalArgumentException e) {
			// Occurs if encryptedPassword is not a valid BCrypt hash
			return false;
		}
	}
}
