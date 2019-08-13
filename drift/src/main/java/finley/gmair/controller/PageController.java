package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.service.AlipayService;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @ClassName: PageController
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/2 1:36 PM
 */
@RestController
@RequestMapping("/drift/page")
@PropertySource("classpath:sesame.properties")
public class PageController {
    private Logger logger = LoggerFactory.getLogger(PageController.class);

    @Value("${ali_gateway}")
    private String gatewayUrl;

    @Value("${zhima_appid}")
    private String appId;

    @Value("${private_key}")
    private String privateKey;

    @Value("${redirect_url}")
    private String redirectUrl;

    @Autowired
    private AlipayService alipayService;


    @GetMapping("/credit")
    public ModelAndView access(ModelAndView view, String app_id, String scope, String auth_code, HttpServletResponse response) {
        if (StringUtil.isEmpty(app_id, scope, auth_code)) {
            try {
                String url = generate("https://microservice.gmair.net/drift/page/credit");
                response.sendRedirect(url);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
//            ResultData r = alipayService.code2openid(auth_code);
//            logger.info("token: ".concat(JSON.toJSONString(r.getData())));
        }
        view.setViewName("credit");
        return view;
    }


    private String generate(String url) {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(redirectUrl);
            sb.append("?");
            sb.append("app_id=").append(appId);
            sb.append("&");
            sb.append("scope=").append("auth_zhima");
            sb.append("&");
            sb.append("redirect_uri=").append(URLEncoder.encode(url, "utf-8"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info(sb.toString());
        return sb.toString();
    }
}
