package utilities;

import exceptions.UtilityException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by u624 on 3/24/17.
 */
public class HashingUtility {
    private static final String SHA_256_HASH_FUNCTION = "SHA-256";
    private static final String ZERO_PAD = "0";

    private HashingUtility() {
        /* static class */
    }

    public static String hashString(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance(SHA_256_HASH_FUNCTION);
            sha256.update(password.getBytes());
            return convertByteArrayToHexString(sha256.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new UtilityException(e);
        }
    }

    private static String convertByteArrayToHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            hexString.append(getHexCode(byteArray[i]));
        }
        return hexString.toString();
    }

    private static String getHexCode(byte value) {
        String hexCode = Integer.toString(Byte.toUnsignedInt(value), 16);
        if (hexCode.length() == 1) {
            hexCode = ZERO_PAD + hexCode;
        }
        return hexCode;
    }
}
