package chinapay.util;

public class BASE64Util {
	private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };

	private static final byte[] pem_convert_array = new byte[256];

	static {
		for (int i = 0; i < 255; i++)
			pem_convert_array[i] = -1;
		for (int j = 0; j < pem_array.length; j++)
			pem_convert_array[pem_array[j]] = (byte) j;
	}

	public static byte[] decode(byte[] paramArrayOfByte) {
		int i = paramArrayOfByte.length / 4 * 3;
		if (i == 0) {
			return paramArrayOfByte;
		}
		if (paramArrayOfByte[(paramArrayOfByte.length - 1)] == 61) {
			i--;
			if (paramArrayOfByte[(paramArrayOfByte.length - 2)] == 61)
				i--;
		}
		byte[] arrayOfByte = new byte[i];

		int j = 0;
		int k = 0;
		i = paramArrayOfByte.length;
		while (i > 0) {
			int l = pem_convert_array[(paramArrayOfByte[(j++)] & 0xFF)];
			int i1 = pem_convert_array[(paramArrayOfByte[(j++)] & 0xFF)];

			arrayOfByte[(k++)] = (byte) (l << 2 & 0xFC | i1 >>> 4 & 0x3);

			if (paramArrayOfByte[j] == 61)
				return arrayOfByte;
			l = i1;
			i1 = pem_convert_array[(paramArrayOfByte[(j++)] & 0xFF)];

			arrayOfByte[(k++)] = (byte) (l << 4 & 0xF0 | i1 >>> 2 & 0xF);

			if (paramArrayOfByte[j] == 61)
				return arrayOfByte;
			l = i1;
			i1 = pem_convert_array[(paramArrayOfByte[(j++)] & 0xFF)];

			arrayOfByte[(k++)] = (byte) (l << 6 & 0xC0 | i1 & 0x3F);
			i -= 4;
		}
		return arrayOfByte;
	}

	public static byte[] encode(byte[] paramArrayOfByte) {
		if (paramArrayOfByte.length == 0)
			return paramArrayOfByte;
		byte[] arrayOfByte = new byte[(paramArrayOfByte.length + 2) / 3 * 4];
		int i = 0;
		int j = 0;
		int k = paramArrayOfByte.length;
		while (k > 0) {
			if (k == 1) {
				int l = paramArrayOfByte[(i++)];
				int i1 = 0;
				arrayOfByte[(j++)] = (byte) pem_array[(l >>> 2 & 0x3F)];
				arrayOfByte[(j++)] = (byte) pem_array[((l << 4 & 0x30) + (i1 >>> 4 & 0xF))];
				arrayOfByte[(j++)] = 61;
				arrayOfByte[(j++)] = 61;
			} else if (k == 2) {
				int l = paramArrayOfByte[(i++)];
				int i1 = paramArrayOfByte[(i++)];
				int i2 = 0;
				arrayOfByte[(j++)] = (byte) pem_array[(l >>> 2 & 0x3F)];
				arrayOfByte[(j++)] = (byte) pem_array[((l << 4 & 0x30) + (i1 >>> 4 & 0xF))];
				arrayOfByte[(j++)] = (byte) pem_array[((i1 << 2 & 0x3C) + (i2 >>> 6 & 0x3))];
				arrayOfByte[(j++)] = 61;
			} else {
				int l = paramArrayOfByte[(i++)];
				int i1 = paramArrayOfByte[(i++)];
				int i2 = paramArrayOfByte[(i++)];
				arrayOfByte[(j++)] = (byte) pem_array[(l >>> 2 & 0x3F)];
				arrayOfByte[(j++)] = (byte) pem_array[((l << 4 & 0x30) + (i1 >>> 4 & 0xF))];
				arrayOfByte[(j++)] = (byte) pem_array[((i1 << 2 & 0x3C) + (i2 >>> 6 & 0x3))];
				arrayOfByte[(j++)] = (byte) pem_array[(i2 & 0x3F)];
			}
			k -= 3;
		}
		return arrayOfByte;
	}
}