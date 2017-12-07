package chinapay.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class MsgUtil {
	static final int BUFFER = 128;

	public static byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		byte[] b = new byte[4096];

		byte[] tmpResult = (byte[]) null;
		byte[] base64Result = (byte[]) null;
		int n;
		while ((n = in.read(b)) != -1) {
			if (tmpResult == null)
				tmpResult = getSumByte(null, 0, b, n);
			else {
				tmpResult = getSumByte(tmpResult, tmpResult.length, b, n);
			}
		}

		base64Result = deflateEncode(tmpResult);
		in.close();
		return base64Result;
	}

	public static byte[] getSumByte(byte[] baseValue, int orLength,
			byte[] streamByte, int length) {
		byte[] result = new byte[orLength + length];
		for (int i = 0; i < orLength; i++) {
			result[i] = baseValue[i];
		}
		for (int i = 0; i < length; i++) {
			result[(orLength + i)] = streamByte[i];
		}
		return result;
	}

	public static byte[] deflateEncode(byte[] inputByte) throws IOException {
		if ((inputByte == null) || (inputByte.length == 0)) {
			System.out.println("压缩编码异常:输入不能为空指针!");
			throw new IOException("压缩编码异常:输入不能为空指针!");
		}
		byte[] tmpByte = deflater(inputByte);
		return encode(tmpByte);
	}

	public static byte[] decodeInflate(byte[] inputByte) throws IOException {
		if ((inputByte == null) || (inputByte.length == 0)) {
			throw new IOException("解码解压缩异常:输入不能为空!");
		}
		byte[] tmpByte = decode(inputByte);
		return inflater(tmpByte);

	}

	public static byte[] deflater(byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.deflate(result);
				o.write(result, 0, compressedDataLength);
			}
		} finally {
			o.close();
		}
		return o.toByteArray();
	}

	public static byte[] inflater(byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (Exception ex) {
			System.err.println("Data format error!\n");
			ex.printStackTrace();
		} finally {
			o.close();
		}
		return o.toByteArray();
	}

	public static byte[] decode(byte[] inputByte) throws IOException {
		return BASE64Util.decode(inputByte);
	}

	public static byte[] encode(byte[] inputByte) throws IOException {
		return BASE64Util.encode(inputByte);
	}
}