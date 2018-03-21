package finley.gmair.wechat;

import finley.gmair.util.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
@ComponentScan("finley.gmair.scheduler")
public class WechatApplication {
    private Logger logger = LoggerFactory.getLogger(WechatApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wechat")
    public String check(HttpServletRequest request) {
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");//
        List<String> params = new ArrayList<>();
        params.add("wechat_token");
        params.add(timestamp);
        params.add(nonce);
        Collections.sort(params);
        String temp = params.get(0) + params.get(1) + params.get(2);
        if (Encryption.SHA1(temp).equals(signature)) {
            return echostr;
        }
        return "";
    }
}
