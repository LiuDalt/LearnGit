package com.example.administrator.myapplication;

import java.security.MessageDigest;



public class DigestUtils {
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String md5Hex(String data, boolean lowercase) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes());
            return new String(encodeHex(array, lowercase ? DIGITS_LOWER : DIGITS_UPPER));
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static final String md5Hex(String data) {
        return md5Hex(data, true);
    }


    private static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }


}
