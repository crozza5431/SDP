package controller;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

public class PasswordHelper
{
	private static final Random RANDOM = new SecureRandom();

	public static byte[] getNewSalt()
	{
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);

		return salt;
	}

	private static byte[] getHash(char[] password, byte[] salt)
	{
		PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
		Arrays.fill(password, Character.MIN_VALUE);
		try
		{
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] encoded = factory.generateSecret(spec).getEncoded();
			return encoded;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e)
		{
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		}
		finally
		{
			spec.clearPassword();
		}
	}

	public static boolean passwordMatches(char[] password, byte[] salt, byte[] dbHash)
	{
		byte[] passwordHash = getHash(password, salt);
		Arrays.fill(password, Character.MIN_VALUE);
		if (passwordHash.length != dbHash.length) return false;
		for (int i = 0; i < passwordHash.length; i++)
		{
			if (passwordHash[i] != dbHash[i]) return false;
		}
		return true;
	}
}
