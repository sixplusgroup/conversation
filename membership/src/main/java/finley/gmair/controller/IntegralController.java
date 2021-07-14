package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: IntegralController
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/13 11:35 AM
 */
@RestController
@RequestMapping("/membership/integral")
public class IntegralController {

    /**
     * 定时任务调用接口，对所有用户的积分做维护。过期未使用积分将自动失效，并从用户可用积分中扣除
     *
     * @return
     */
    @PostMapping(value = "/maintain")
    public ResultData maintain() {
        ResultData result = new ResultData();

        return result;
    }
}
