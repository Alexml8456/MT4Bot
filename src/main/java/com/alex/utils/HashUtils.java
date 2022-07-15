package com.alex.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;

public class HashUtils {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String HMAC_SHA512 = "HmacSHA512";
    private static final String HMAC_SHA384 = "HmacSHA384";
    public static final String HMAC_SHA256 = "HmacSHA256";

    public static String hmacSha512(String value, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), HMAC_SHA512);

            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(keySpec);
            return toHexString(mac.doFinal(value.getBytes(DEFAULT_ENCODING)));

        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacSha384(String value, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), HMAC_SHA384);

            Mac mac = Mac.getInstance(HMAC_SHA384);
            mac.init(keySpec);
            mac.update(Base64.getEncoder().encode(value.getBytes()));

            return String.format("%096x", new BigInteger(1, mac.doFinal()));

        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hmacSha256(String value, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(keySpec);

            return byteArrayToHex(mac.doFinal(value.getBytes(DEFAULT_ENCODING)));

        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
