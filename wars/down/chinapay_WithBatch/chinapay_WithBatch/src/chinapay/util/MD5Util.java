package chinapay.util;

import chinapay.DesRsa;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static MessageDigest alg;
	private static int length;
	private static int maxBytesReadPerTime = 8192;

	static {
		try {
			alg = MessageDigest.getInstance("MD5");

			length = alg.getDigestLength();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String getMD5Content(byte[] fileContent) throws Exception {
		byte[] tmpByte = getMD5(fileContent, fileContent.length);

		String tmpRetMd5 = DesRsa.Hex2Asc(tmpByte.length, tmpByte);
		return tmpRetMd5;
	}

	public static byte[] getMD5(byte[] is, long fileLength) {
		byte[] bytes = new byte[maxBytesReadPerTime];
		int bytesRead = 0;
		int maxReadTimesBeforeRedigest = 100;
		byte[] digestOfPart = new byte[length];
		byte[] digest = new byte[length * maxReadTimesBeforeRedigest];
		int timesRead = 0;
		int readTimes = 0;
		byte[] digestToReturn = new byte[length];

		while (fileLength > 0L) {
			if (fileLength >= maxBytesReadPerTime) {
				System.arraycopy(is, readTimes * maxBytesReadPerTime, bytes, 0,
						maxBytesReadPerTime);
				bytesRead = maxBytesReadPerTime;
			} else {
				System.arraycopy(is, readTimes * maxBytesReadPerTime, bytes, 0,
						(int) fileLength);
				bytesRead = (int) fileLength;
			}
			digestOfPart = getMD5(bytes, 0, bytesRead);
			System.arraycopy(digestOfPart, 0, digest, timesRead * length,
					length);
			timesRead++;
			readTimes++;
			if (timesRead == maxReadTimesBeforeRedigest) {
				digestOfPart = getMD5(digest, 0, digest.length);
				System.arraycopy(digestOfPart, 0, digest, 0, length);
				timesRead = 1;
			}
			fileLength -= bytesRead;
		}

		if (timesRead > 1) {
			digestOfPart = getMD5(digest, 0, timesRead * length);
			System.arraycopy(digestOfPart, 0, digestToReturn, 0, length);
		} else {
			System.arraycopy(digest, 0, digestToReturn, 0, length);
		}
		return digestToReturn;
	}

	public static byte[] getMD5(InputStream is, long fileLength)
			throws Exception {
		byte[] bytes = new byte[maxBytesReadPerTime];
		int bytesRead = 0;
		int maxReadTimesBeforeRedigest = 100;
		byte[] digestOfPart = new byte[length];
		byte[] digest = new byte[length * maxReadTimesBeforeRedigest];
		int timesRead = 0;
		byte[] digestToReturn = new byte[length];
		try {
			while (fileLength > 0L) {
				bytesRead = is.read(bytes);
				digestOfPart = getMD5(bytes, 0, bytesRead);
				System.arraycopy(digestOfPart, 0, digest, timesRead * length,
						length);
				timesRead++;
				if (timesRead == maxReadTimesBeforeRedigest) {
					digestOfPart = getMD5(digest, 0, digest.length);
					System.arraycopy(digestOfPart, 0, digest, 0, length);
					timesRead = 1;
				}
				fileLength -= bytesRead;
			}

			if (timesRead > 1) {
				digestOfPart = getMD5(digest, 0, timesRead * length);
				System.arraycopy(digestOfPart, 0, digestToReturn, 0, length);
			} else {
				System.arraycopy(digest, 0, digestToReturn, 0, length);
			}

			if (is != null)
				is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return digestToReturn;
	}

	public static byte[] getMD5(byte[] in) {
		alg.update(in);
		return alg.digest();
	}

	public static byte[] getMD5(byte[] in, int from, int len) {
		alg.update(in, from, len);
		return alg.digest();
	}

	public static int getDigestLength() {
		return length;
	}

	public static byte[] getMd5(byte[] tmpValue) {
		alg.update(tmpValue);
		byte[] digest = alg.digest();
		return digest;
	}
}