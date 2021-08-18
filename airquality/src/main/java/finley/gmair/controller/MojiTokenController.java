package finley.gmair.controller;

import finley.gmair.model.air.MojiToken;
import finley.gmair.service.MojiTokenService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MojiTokenController
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/20 6:37 PM
 */
@RestController
@RequestMapping("/token")
public class MojiTokenController {
    private Logger logger = LoggerFactory.getLogger(MojiTokenController.class);

    @Resource
    private MojiTokenService mojiTokenService;

    @GetMapping("/token/overview")
    public ResultData overview() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        return mojiTokenService.fetch(condition);
    }

    @PostMapping("/token/create")
    public ResultData create(String token, String url, String password, String base) {
        MojiToken mojiToken = new MojiToken(token, password, url, base);
        return mojiTokenService.create(mojiToken);
    }

}
