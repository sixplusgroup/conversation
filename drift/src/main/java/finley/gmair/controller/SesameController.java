package finley.gmair.controller;

import finley.gmair.service.AlipayService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: SesameController
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/1 6:57 PM
 */
@RestController
@RequestMapping("/drift/sesame")
public class SesameController {

    @Autowired
    private AlipayService alipayService;

    @GetMapping("/openid")
    public ResultData code2openid(String code) {
        ResultData result = new ResultData();
        ResultData response = alipayService.code2openid(code);
        return result;
    }
}
