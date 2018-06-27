package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.form.consumer.LocationForm;
import finley.gmair.form.consumer.LoginForm;
import finley.gmair.form.message.MessageForm;
import finley.gmair.model.auth.VerificationCode;
import finley.gmair.model.consumer.Address;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.service.ConsumerService;
import finley.gmair.service.MessageService;
import finley.gmair.service.SerialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ConsumerAuthController {
    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private SerialService serialService;

    @Autowired
    private MessageService messageService;


    /**
     * register user information
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/consumer/register")
    public ResultData register(ConsumerForm form) {
        ResultData result = new ResultData();
        Consumer consumer = new Consumer(form.getName(), form.getWechat(), form.getAddressDetail(), form.getAddressProvince(), form.getAddressCity(), form.getAddressDistrict(), form.getPhone());
        if (!StringUtils.isEmpty(form.getUsername())) {
            consumer.setUsername(form.getUsername());
        }
        //Check whether the user already exist, from perspective of wechat, phone
        if (form.getWechat() != null || form.getPhone() != null) {
            Map<String, Object> condition = new HashMap<>();
            if (form.getWechat() != null) {
                condition.put("wechat", form.getWechat());
            }
            if (form.getPhone() != null) {
                condition.put("phone", form.getPhone());
            }
            if (consumerService.exist(condition)) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("User already exist, please be sure of your wechat or phone number");
                return result;
            }
        }
        //Create the user
        try {
            ResultData response = consumerService.createConsumer(consumer);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to create consumer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * login user according to the phone number & dynamic verification code, or openid provided by wechat
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/consumer/login")
    public ResultData login(LoginForm form) {
        ResultData result = new ResultData();
        //phone and verification code #1 priority
        if (!StringUtils.isEmpty(form.getPhone()) && !StringUtils.isEmpty(form.getCode())) {
            //verify whether the phone and code is correct
            String phone = form.getPhone();
            VerificationCode code = serialService.fetch(phone);
            if (StringUtils.isEmpty(code) || !form.getCode().equals(code.getSerial())) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The phone and code is incorrect");
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(code);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/consumer/request")
    public ResultData request(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please enter your phone number correctly");
            return result;
        }
        VerificationCode code = serialService.generate(phone);
        // call message agent to send the text to corresponding phone number
        // retrieve message template from database
        System.out.println(JSON.toJSONString(code));
//        messageService.sendOne(new MessageForm());
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(code);
        result.setDescription("Message sent, please check the code");
        return result;
    }

    /**
     * This method is called to fetch detailed information of a consumer
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/consumer/profile")
    public ResultData profile() {
        ResultData result = new ResultData();
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        if (StringUtils.isEmpty(user)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", user.getUsername());
        condition.put("blockFlag", false);
        ResultData response = consumerService.fetchConsumer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ConsumerVo consumer = ((List<ConsumerVo>) response.getData()).get(0);
            result.setData(consumer);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No consumer with phone: ").append(user.getUsername()).append(" found.").toString());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find the consumer");
        }
        return result;
    }

    @PostMapping("/consumer/wechat/bind")
    public ResultData bindWechat(String openid) {
        ResultData result = new ResultData();
        String consumerId = currentConsumerId();
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", false);
        condition.put("wechat", openid);
        ResultData response = consumerService.modifyConsumer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    @PostMapping("/consumer/wechat/unbind")
    public ResultData unbindWechat() {
        ResultData result = new ResultData();
        String consumerId = currentConsumerId();
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", true);
        ResultData response = consumerService.modifyConsumer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    @PostMapping("/consumer/edit/username")
    public ResultData editUsername(String username) {
        ResultData result = new ResultData();
        String consumerId = currentConsumerId();
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", false);
        condition.put("username", username);
        ResultData response = consumerService.modifyConsumer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    @PostMapping("/consumer/edit/location")
    public ResultData editLocation(LocationForm form) {
        ResultData result = new ResultData();
        //fetch the user from context first
        String consumerId = currentConsumerId();
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }

        //change the location, if it is the only location item the user have, then made it preferred
        if (form.isPreferred()) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("consumerId", consumerId);
            condition.put("preferred", true);
            ResultData rd = consumerService.fetchConsumerAddress(condition);
            if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Address addressVo = ((List<Address>) rd.getData()).get(0);
                condition.clear();
                condition.put("addressId", addressVo.getAddressId());
                condition.put("preferred", false);
                rd = consumerService.modifyConsumerAddress(condition);
                if (rd.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("Fail to update old preferred address");
                    return result;
                }
            }
        }

        Address address = new Address(form.getDetail(), form.getProvince(), form.getCity(), form.getDistrict());
        address.setPreferred(form.isPreferred());
        ResultData response = consumerService.createConsumerAddress(address, consumerId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription(response.getDescription());
        return result;
    }

    @PostMapping("/consumer/edit/location/default")
    public ResultData preferLocation(String locationId) {
        ResultData result = new ResultData();
        String consumerId = currentConsumerId();
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("preferred", true);
        ResultData response = consumerService.fetchConsumerAddress(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System error, please try later");
            return result;
        }
        Address addressVo = ((List<Address>) response.getData()).get(0);
        condition.clear();
        condition.put("addressId", addressVo.getAddressId());
        condition.put("preferred", false);
        response = consumerService.modifyConsumerAddress(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            condition.clear();
            condition.put("addressId", locationId);
            condition.put("preferred", true);
            response = consumerService.modifyConsumerAddress(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to change user's preferred address");
            }
        }
        return result;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal user(Principal user) {
        return user;
    }

    private String currentConsumerId() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        if (StringUtils.isEmpty(user)) {
            return null;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", user.getUsername());
        condition.put("blockFlag", false);
        ResultData response = consumerService.fetchConsumer(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return null;
        }
        ConsumerVo consumer = ((List<ConsumerVo>) response.getData()).get(0);
        return consumer.getConsumerId();
    }

    @RequestMapping(value = "/consumerid", method = RequestMethod.GET)
    public ResultData getConsumerId(String phone) {
        ResultData result = new ResultData();

        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("phone is empty");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", phone);
        condition.put("blockFlag", false);
        ResultData response = consumerService.fetchConsumer(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("error or null");
            return result;
        }
        ConsumerVo consumer = ((List<ConsumerVo>) response.getData()).get(0);
        result.setData(consumer.getConsumerId());
        return result;
    }
}
