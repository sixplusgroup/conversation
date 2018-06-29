package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.form.consumer.LocationForm;
import finley.gmair.form.consumer.LoginForm;
import finley.gmair.model.auth.VerificationCode;
import finley.gmair.model.consumer.Address;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.model.message.MessageCatalog;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
        if (!StringUtils.isEmpty(form.getWechat()) || !StringUtils.isEmpty(form.getPhone())) {
            Map<String, Object> condition = new HashMap<>();
            if (!StringUtils.isEmpty(form.getWechat())) {
                condition.put("wechat", form.getWechat());
            }
            if (!StringUtils.isEmpty(form.getPhone())) {
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

    @RequestMapping(method = RequestMethod.POST, value = "/consumer/{action}/request")
    public ResultData request(String phone, @PathVariable("action") String action) {
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
        ResultData response = messageService.template(String.valueOf(action.toUpperCase()));
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get registration message template.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No existing registration message template.");
            return result;
        }
        JSONObject template = JSON.parseObject(JSON.toJSONString(((List) response.getData()).get(0)));
        messageService.sendOne(phone, template.getString("message").replace("###", code.getSerial()));
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
    public ResultData profile(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("phone empty");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", phone);
        condition.put("blockFlag", false);
        ResultData response = consumerService.fetchConsumer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ConsumerVo consumer = ((List<ConsumerVo>) response.getData()).get(0);
            result.setData(consumer);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No consumer with phone: ").append(phone).append(" found.").toString());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find the consumer");
        }
        return result;
    }

    @PostMapping("/consumer/wechat/bind")
    public ResultData bindWechat(String phone, String openid) {
        ResultData result = new ResultData();
        String consumerId = (String) getConsumerId(phone).getData();
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
    public ResultData unbindWechat(String phone) {
        ResultData result = new ResultData();
        String consumerId = (String) getConsumerId(phone).getData();
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
    public ResultData editUsername(String phone, String username) {
        ResultData result = new ResultData();
        String consumerId = (String) getConsumerId(phone).getData();
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

    @PostMapping("consumer/edit/phone")
    public ResultData editPhone(String oldPhone, String newPhone) {
        ResultData result = new ResultData();
        String consumerId = (String) getConsumerId(oldPhone).getData();
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have logged on to the system");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", false);
        condition.put("number", newPhone);
        ResultData response = consumerService.modifyConsumerPhone(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        return result;
    }

    @PostMapping("/consumer/edit/location")
    public ResultData editLocation(String phone, LocationForm form) {
        ResultData result = new ResultData();
        //fetch the user from context first
        String consumerId = (String) getConsumerId(phone).getData();
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

    @RequestMapping(value = "/consumer/check/existphone", method = RequestMethod.GET)
    public ResultData checkPhoneExist(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("number", phone);
        condition.put("blockFlag", false);
        ResultData response = consumerService.fetchConsumerPhone(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("find the same phone number");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the same phone number");
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }
    }
}
