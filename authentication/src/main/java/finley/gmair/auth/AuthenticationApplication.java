package finley.gmair.auth;

import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@SpringBootApplication
@RequestMapping("/auth")
@ComponentScan("finley.gmair.service")
@ComponentScan("finley.gmair.dao")
public class AuthenticationApplication {
    @Autowired

    private ConsumerService consumerService;

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    /**
     * register user information
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResultData register(ConsumerForm form) {
        ResultData result = new ResultData();
        Consumer consumer = new Consumer(form.getName(), form.getWechat(), form.getAddressDetail(), form.getAddressProvince(), form.getAddressCity(), form.getAddressDistrict(), form.getPhone());
        if(!StringUtils.isEmpty(form.getUsername())) {
            consumer.setUsername(form.getUsername());
        }
        //Check whether the user already exist, from perspective of wechat, phone
        if(form.getWechat() != null || form.getPhone() != null) {
            Map<String, Object> condition = new HashMap<>();
            if(form.getWechat() != null) {
                condition.put("wechat", form.getWechat());
            }
            if(form.getPhone() != null) {
                condition.put("phone", form.getPhone());
            }
            if(consumerService.existConsumer(condition)) {
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
