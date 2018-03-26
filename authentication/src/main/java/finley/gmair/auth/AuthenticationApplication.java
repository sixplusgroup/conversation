package finley.gmair.auth;

import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.form.consumer.LoginForm;
import finley.gmair.form.message.MessageForm;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.service.ConsumerService;
import finley.gmair.service.MessageService;
import finley.gmair.service.SerialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/auth")
@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.config", "finley.gmair.factory", "finley.gmair.controller"})
@EnableCaching
@EnableEurekaClient
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableDiscoveryClient
@SpringBootApplication
public class AuthenticationApplication {
    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private SerialService serialService;

    @Autowired
    private MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    /**
     * register user information
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register")
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
            if (consumerService.existConsumer(condition)) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("User already exist, please make sure of your wechat or phone number");
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
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResultData login(LoginForm form) {
        ResultData result = new ResultData();
        //phone and verification code #1 priority
        if (!StringUtils.isEmpty(form.getPhone()) && !StringUtils.isEmpty(form.getCode())) {
            //verify whether the phone and code is correct
            String phone = form.getPhone();
            Map<String, String> value = serialService.fetch(phone);
            if (StringUtils.isEmpty(value) || !form.getCode().equals(value.get(phone))) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The phone and code is incorrect");
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(value.get(phone));
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/request")
    public ResultData request(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please enter your phone number correctly");
            return result;
        }
        Map<String, String> value = serialService.generate(phone);
        // call message agent to send the text to corresponding phone number
        // retrieve message template from database
        ResultData response = messageService.sendOne(new MessageForm());
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(value.get(phone));
        return result;
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
