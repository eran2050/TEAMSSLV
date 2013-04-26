package util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Util {

	private static String	forUTF8;

	public static String getSha(String s, String k) {

		String hash = "";

		try {
			Mac mac = Mac.getInstance("HmacSha256");
			SecretKeySpec secret = new SecretKeySpec(k.getBytes(), "HmacSha256");
			mac.init(secret);
			byte[] shaDigest = mac.doFinal(s.getBytes());
			for (byte b : shaDigest) {
				hash += String.format("%02x", b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hash;
	}

	public static String toUTF8(String s1) {
		forUTF8 = s1.replace("\u0026", "&");
		return forUTF8;
	}

}
