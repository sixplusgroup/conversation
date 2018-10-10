package finley.gmair.util;

import java.util.Random;

public class EXSerialGenerator {
    private static final Random random = new Random();
    private static final char[] code = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
    'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
    'W', 'X', 'Y', 'Z'};

    private static int num(int min, int max) {return min + random.nextInt(max - min);}

    private static char gen() {return code[num(0, code.length)];}

    public static String generate() {
        char[] temp = new char[6];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = gen();
        }
        StringBuffer result = new StringBuffer();
        result.append(new String(temp));
        return result.toString();
    }
}
