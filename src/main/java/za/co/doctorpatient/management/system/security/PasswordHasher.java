package za.co.doctorpatient.management.system.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password hashing utility using SHA-256 with a random salt.
 * 
 * For production use, consider replacing this with BCrypt by adding
 * the jBCrypt library (org.mindrot:jbcrypt:0.4) to WEB-INF/lib.
 * 
 * Stored format: base64(salt):base64(hash)
 */
public class PasswordHasher {

	private static final int SALT_LENGTH = 16;

	/**
	 * Hashes a plain-text password with a random salt.
	 * 
	 * @param plainPassword the raw password to hash
	 * @return the stored hash in format "salt:hash" (both Base64-encoded)
	 */
	public static String hashPassword(String plainPassword) {
		byte[] salt = generateSalt();
		byte[] hash = hash(plainPassword, salt);

		String saltBase64 = Base64.getEncoder().encodeToString(salt);
		String hashBase64 = Base64.getEncoder().encodeToString(hash);

		return saltBase64 + ":" + hashBase64;
	}

	/**
	 * Verifies a plain-text password against a stored hash.
	 * 
	 * @param plainPassword the raw password to check
	 * @param storedHash    the stored hash in "salt:hash" format
	 * @return true if the password matches
	 */
	public static boolean verifyPassword(String plainPassword, String storedHash) {
		if (storedHash == null || !storedHash.contains(":")) {
			return false;
		}

		String[] parts = storedHash.split(":", 2);
		byte[] salt = Base64.getDecoder().decode(parts[0]);
		byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
		byte[] actualHash = hash(plainPassword, salt);

		return MessageDigest.isEqual(expectedHash, actualHash);
	}

	private static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		return salt;
	}

	private static byte[] hash(String password, byte[] salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(salt);
			return digest.digest(password.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not available", e);
		}
	}
}
