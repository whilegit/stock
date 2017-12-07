package chinapay.util;

import chinapay.PrivateKey;
import chinapay.SecureLink;

public class DigestMD5 {
	public static String MD5Sign(String MerId, String FileName, byte[] FileContent, String MerKeyPath) throws Exception {
		String MD5 = MD5Util.getMD5Content(FileContent);

		String PlainData = MerId + FileName + MD5;

		int KeyUsage = 0;
		PrivateKey key = new PrivateKey();
		key.buildKey(MerId, KeyUsage, MerKeyPath);

		SecureLink s = new SecureLink(key);
		String chkValue = s.Sign(PlainData);

		return chkValue;
	}

	public static String MD5Sign(String MerId, byte[] Plain, String MerKeyPath)
			throws Exception {
		String MD5 = MD5Util.getMD5Content(Plain);

		int KeyUsage = 0;
		PrivateKey key = new PrivateKey();
		key.buildKey(MerId, KeyUsage, MerKeyPath);

		SecureLink s = new SecureLink(key);
		String chkValue = s.Sign(MD5);

		return chkValue;
	}

	public static boolean MD5Verify(byte[] Plain, String CheckValue,
			String PubKeyPath) throws Exception {
		String MD5 = MD5Util.getMD5Content(Plain);

		boolean res = false;

		PrivateKey key = new PrivateKey();
		key.buildKey("999999999999999", 0, PubKeyPath);
		SecureLink s = new SecureLink(key);

		res = s.verifyAuthToken(MD5, CheckValue);
		return res;
	}
}