package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: MembershipController
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/12 11:46 PM
 */
@RestController
@RequestMapping("/membership")
public class MembershipController {

    /**
     * 果麦系统用户成为会员接口，录入membership表
     *
     * @param consumerId
     * @return
     */
    @PostMapping(value = "/enroll")
    public ResultData enroll(String consumerId) {
        ResultData result = new ResultData();

        return result;
    }

    /**
     * 用户积分投放，为用户的积分账户添加积分
     *
     * @param consumerId
     * @param integral
     * @return
     */
    @PostMapping(value = "/deposit")
    public ResultData deposit(String consumerId, Integer integral) {
        ResultData result = new ResultData();

        return result;
    }

    /**
     * 用户积分使用，从用户的积分账户中消耗积分，优先使用时间较早的积分
     *
     * @param consumerId
     * @param integral
     * @return
     */
    @PostMapping(value = "/withdraw")
    public ResultData withdraw(String consumerId, Integer integral) {
        ResultData result = new ResultData();

        return result;
    }
}
