package finley.gmair.util;

import java.util.Random;

public class SerialUtil {
    private static final Random seed = new Random();
    private static final char[] code = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static int num(int min, int max) {
        return min + seed.nextInt(max - min);
    }

    public static char generate() {
        return code[num(0, code.length)];
    }

    public static String serial() {
        char[] temp = new char[6];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = generate();
        }
        StringBuffer result = new StringBuffer(new String(temp));
        return result.toString();
    }
}
