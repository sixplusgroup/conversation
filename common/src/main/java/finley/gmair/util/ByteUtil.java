package finley.gmair.util;

import java.io.ByteArrayOutputStream;

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
}
