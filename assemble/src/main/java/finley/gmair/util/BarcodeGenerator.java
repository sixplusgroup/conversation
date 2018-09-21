package finley.gmair.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class BarcodeGenerator {
    public static String generateOne(int id) {
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        result.append(sdf.format(Calendar.getInstance().getTime()));
        result.append(String.format("%03d", id));
        result.append(random.nextInt(10));
        result.append(random.nextInt(10));
        result.append(random.nextInt(10));
        return result.toString();
    }

    public static List<String> generateList(int number) {
        if (number <= 0 || number > 1000)
            return null;
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            resultList.add(BarcodeGenerator.generateOne(i));
        }
        return resultList;
    }
}
