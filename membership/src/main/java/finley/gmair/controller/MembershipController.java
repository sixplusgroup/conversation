package finley.gmair.controller;


import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.service.IntegralService;
import finley.gmair.service.MembershipService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: MembershipController
 * @Description: 会员积分controller
 * @Author fan
 * @Date 2021/7/12 11:46 PM
 */
@RestController
@RequestMapping("/membership")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private IntegralService integralService;
    /**
     * 果麦系统用户成为会员接口，录入membership表
     *
     * @param consumerId
     * @return
     */
    @PostMapping(value = "/enroll")
    public ResultData enroll(String consumerId) {
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(consumerId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please pass in the ID");
            return result;
        }
        ResultData response = membershipService.create(consumerId);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the operation of adding membership failed");
        }
        return result;
    }

    /**
     * 用户积分投放
     *
     * @param consumerId
     * @param integral
     * @return
     */
    @Transactional(rollbackFor=Exception.class) // this annotation need add at controller layer
    @PostMapping(value = "/deposit")
    public ResultData deposit(String consumerId, Integer integral, String productId) {
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(consumerId)||StringUtils.isEmpty(productId)||integral<0){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please keep the validity of parameters");
            return result;
        }
        IntegralAdd integralAdd = new IntegralAdd(consumerId,integral,productId);
        ResultData response = integralService.addIntegral(integralAdd);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("add the integral failed");
        }
        return result;
    }

    /**
     * @Description integral confirm, add to `secondIntegral` after confirming
     * @Date  2021/7/18 19:35
     * @param addId:
     * @return finley.gmair.util.ResultData
     **/
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value="/confirmIntegral")
    public ResultData confirmIntegral(String addId,String consumerId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(addId)||StringUtils.isEmpty(consumerId)){
            result.setDescription("parameters can not be null");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if(!integralService.checkIntegralIsValid(addId)){
            result.setDescription("the record of integral is not exist or is confirmed");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if(!membershipService.checkMemberIsValid(consumerId)){
            result.setDescription("the member is not exist");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        ResultData response = integralService.confirmIntegral(addId,consumerId);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setDescription("integral confirmation failed");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        }
        return result;
    }

    /**
     * 用户积分使用，从用户的积分账户中消耗积分，优先使用时间较早的积分
     *
     * @param consumerId
     * @param integral
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @PostMapping(value = "/withdraw")
    public ResultData withdraw(String consumerId, Integer integral) {
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(consumerId)||integral<0){
            result.setDescription("parameters are not valid");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        if(!membershipService.checkMemberIsValid(consumerId)){
            result.setDescription("the member is not exist");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        ResultData response = membershipService.withdrawIntegral(consumerId,integral);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            result.setDescription(response.getDescription());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }


}
