package finley.gmair.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ByteUtil {
    public static byte[] concat(byte[]... partial) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            for (byte[] item : partial) {
                output.write(item);
            }
        } catch (Exception e) {
            return null;
        }
        return output.toByteArray();
    }

    public static byte[] int2byte(int value, int size) {
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[size - i - 1] = (byte) ((value >> (8 * i)) & 0xFF);
        }
        return result;
    }

    public static byte[] long2byte(long value, int size) {
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[size - i - 1] = (byte) ((value >> (8 * i)) & 0xFF);
        }
        return result;
    }

    public static byte[] string2byte(String value, int size) {
        byte[] source = value.getBytes();
        byte[] result = new byte[size];
        int length = (source.length < size) ? source.length : size;
        for (int i = 0; i < length; i++) {
            result[result.length - 1 - i] = source[source.length - 1 - i];
        }
        return result;
    }

    public static int byte2int(byte[] value) {
        byte[] source = new byte[4];
        System.arraycopy(value, 0, source, source.length - value.length, value.length);
        ByteBuffer bb = ByteBuffer.wrap(source);
        return bb.getInt();
    }

    public static long byte2long(byte[] value) {
        byte[] source = new byte[8];
        System.arraycopy(value, 0, source, source.length - value.length, value.length);
        ByteBuffer bb = ByteBuffer.wrap(source);
        return bb.getLong();
    }

    public static String byte2Hex(byte[] input) {
        StringBuffer sb = new StringBuffer();
        for (byte b : input) {
            sb.append(String.format("%02x", b).toUpperCase());
        }
        return sb.toString();
    }
}
