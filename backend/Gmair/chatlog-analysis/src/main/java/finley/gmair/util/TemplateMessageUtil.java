package finley.gmair.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemplateMessageUtil {
    public boolean isTemplate(String str) {
        String s = str.replaceAll("[\\s]", "");
//        System.out.println("template check:" + s);
        return str.isEmpty() || isMobile(s) || isTaxNum(s) || isUrl(s);
    }

    private boolean isMobile(String str) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
//        System.out.println("is mobile:" + b);
        return b;
    }

    private boolean isUrl(String str) {
        return str.startsWith("http");
    }

    private boolean isTaxNum(String str) {
        return false;
    }

}
