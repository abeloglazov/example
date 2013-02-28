package training.mysimpleblog.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class, that contains String related methods. Contains method, that
 * allows to hash given string.
 * 
 * @author Aleksejs Beloglazovs
 */
public class StringUtil {

    public static String sha256(String str) {
        try {
            // Create MessageDigest and encoding for input String
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes("UTF-8"));

            // Hash the Input String
            StringBuffer sb = new StringBuffer();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
