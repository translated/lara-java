package com.translated.lara.net;

class HexFormat {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String format(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_ARRAY[(b & 0xF0) >>> 4]);
            sb.append(HEX_ARRAY[b & 0x0F]);
        }
        return sb.toString();
    }

}
