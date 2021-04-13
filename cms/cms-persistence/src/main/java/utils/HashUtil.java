package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class HashUtil {
	private static final String HASH_STRING_ERROR = "Hash String Error";
	private static final String SHA_256_ALGORITHM = "SHA-256";
	private static final String ZERO = "0";

	private static Logger logger = Logger.getLogger(HashUtil.class);

	private HashUtil() {
		/* static class */
	}

	public static String hashString(String password) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance(SHA_256_ALGORITHM);
			sha256.update(password.getBytes());
			return convertByteArrayToHexString(sha256.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error(HASH_STRING_ERROR, e);

			throw new UtilException(e);
		}
	}

	private static String convertByteArrayToHexString(byte[] byteArray) {
		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < byteArray.length; i++) {
			hexString.append(byteToHexCode(byteArray[i]));
		}

		return hexString.toString();
	}

	private static String byteToHexCode(byte value) {
		String hexCode = Integer.toString(Byte.toUnsignedInt(value), 16);

		if (hexCode.length() == 1) {
			hexCode = ZERO + hexCode;
		}

		return hexCode;
	}
}
