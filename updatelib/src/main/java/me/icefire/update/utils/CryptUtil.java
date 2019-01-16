package me.icefire.update.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 加密
 *
 * @author yangchj
 * email yangchj@icefire.me
 * date 2018/6/4
 */
public class CryptUtil {

    /**
     * 默认字符集（UTF-8）
     */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * 加密方法：MD5
     */
    public static final String MD5 = "MD5";
    /**
     * 加密方法：SHA
     */
    public static final String SHA = "SHA";

    /**
     * byte[] -> 十六进制字符串（小写）
     * @param bytes
     * @return
     */
    public final static String bytes2HexStr(byte[] bytes) {
        return bytes2HexStr(bytes,false);
    }

    /**
     * byte[] -> 十六进制进制字符串
     * @param bytes
     * @param capital
     * @return
     */
    public final static String bytes2HexStr(byte[] bytes, boolean capital) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(byte2Hex(b,capital));
        }
        return sb.toString();
    }

    /**
     * byte-> 十六进制双子符（小写）
     * @param b
     * @return
     */
    public final static char[] byte2Hex(byte b) {
        return byte2Hex(b, false);
    }

    /**
     * byte -> 十六进制双字符
     * @param b
     * @param capital
     * @return
     */
    public final static char[] byte2Hex(byte b, boolean capital) {
        byte bh = (byte) (b >>> 4 & 0xF);
        byte bl = (byte) (b & 0xF);
        return new char[]{halfByte2Hex(bh, capital), halfByte2Hex(bl, capital)};
    }

    /**
     * 半 byte -> 十六进制单字符 (小写)
     * @param b
     * @return
     */
    public final static char halfByte2Hex(byte b) {
        return halfByte2Hex(b, false);
    }

    /**
     * 半 byte -> 十六进制单字符
     * @param b
     * @param capital
     * @return
     */
    public final static char halfByte2Hex(byte b, boolean capital) {
        return (char) (b <= 9 ? b + '0' : (capital ? b + 'A' - 0xA
                : b + 'a' - 0xA));
    }

    /**
     * 十六进制字符串 -> byte[]
     * @param str
     * @return
     */
    public final static byte[] hexStr2Bytes(String str) {
        int length = str.length();

        if (length % 2 != 0) {
            str = "0" + str;
            length = str.length();
        }

        byte[] bytes = new byte[length / 2];

        for (int i = 0; i < bytes.length; i++)
            bytes[i] = hex2Byte(str.charAt(2 * i), str.charAt(2 * i + 1));

        return bytes;
    }

    /**
     * 十六进制双字符 -> byte
     * @param ch
     * @param cl
     * @return
     */
    public final static byte hex2Byte(char ch, char cl) {
        byte bh = hex2HalfByte(ch);
        byte bl = hex2HalfByte(cl);

        return (byte) ((bh << 4) + bl);
    }

    /**
     * 十六进制单字符 -> 半 byte
     * @param c
     * @return
     */
    public final static byte hex2HalfByte(char c) {
        return (byte) (c <= '9' ? c - '0' : (c <= 'F' ? c - 'A' + 0xA
                : c - 'a' + 0xA));
    }

    /**
     * 使用默认字符集对字符串编码后再进行 MD5 加密
     * @param input
     * @return
     */
    public final static String md5(String input) {
        return md5(input, null);
    }

    /**
     * 使用指定字符集对字符串编码后再进行 MD5 加密
     * @param input
     * @param charset
     * @return
     */
    public final static String md5(String input, String charset) {
        return encode(getMd5Digest(), input, charset);
    }

    /**
     * MD5加密
     * @param input
     * @return
     */
    public final static byte[] md5(byte[] input) {
        MessageDigest algorithm = getMd5Digest();
        return encode(algorithm, input);
    }

    /**
     * 获取 MD5 加密摘要对象
     * @return
     */
    public final static MessageDigest getMd5Digest() {
        return getDigest(MD5);
    }

    /**
     * 获取 SHA 加密摘要对象
     * @return
     */
    public final static MessageDigest getShaDigest() {
        return getDigest(SHA);
    }

    /**
     * 获取 SHA-{X} 加密摘要对象，其中 {X} 由 version 参数指定
     * @param version
     * @return
     */
    public final static MessageDigest getShaDigest(int version) {
        String algorithm = String.format("%s-%d", SHA, version);
        return getDigest(algorithm);
    }

    /**
     * 根据加密方法名称获取加密摘要对象
     * @param algorithm
     * @return
     */
    public final static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据加密方法名称和提供者获取加密摘要对象
     * @param algorithm
     * @param provider
     * @return
     */
    public final static MessageDigest getDigest(String algorithm, String provider) {
        try {
            return MessageDigest.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用指定算法对字符串加密
     * @param algorithm
     * @param input
     * @return
     */
    public final static String encode(MessageDigest algorithm, String input) {
        return encode(algorithm, input, null);
    }

    /**
     * 使用指定字符集对字符串编码后再进行 SHA-{X} 加密，字符串的编码由 charset 参数指定
     * @param algorithm
     * @param input
     * @param charset
     * @return
     */
    public final static String encode(MessageDigest algorithm, String input,
                                      String charset) {
        try {
            byte[] bytes = input.getBytes(safeCharset(charset));
            byte[] output = encode(algorithm, bytes);

            return bytes2HexStr(output);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用指定算法对 byte[] 加密
     * @param algorithm
     * @param input
     * @return
     */
    public final static byte[] encode(MessageDigest algorithm, byte[] input) {
        return algorithm.digest(input);
    }

    /**
     * 安全的字符集
     * @param charset
     * @return
     */
    private final static String safeCharset(String charset) {
        if (isStrEmpty(charset))
            charset = DEFAULT_ENCODING;
        return charset;
    }

    /**
     * 字符串not null
     * @param str
     * @return
     */
    public final static String safeString(String str) {
        return (str == null) ? "" : str;
    }

    /**
     * 字符串判空
     * @param str
     * @return
     */
    public final static boolean isStrEmpty(String str) {
        return !(str != null && str.length() != 0);
    }

    /**
     * RC4加密
     * @param aInput
     * @param aKey
     * @return
     */
    public static String encodeRC4(String aInput, String aKey) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++)
            iS[i] = i;
        int j = 1;
        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) aKey.charAt((i % aKey.length()));
        }
        j = 0;
        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }
        int i = 0;
        j = 0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }
        return new String(iOutputChar);
    }

    /**
     * RC4解密
     * @param aInput
     * @param aKey
     * @return
     */
    public static String dencodeRC4(String aInput, String aKey) {
        return encodeRC4(aInput, aKey);
    }

}
