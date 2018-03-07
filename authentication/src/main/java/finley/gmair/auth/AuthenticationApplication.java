package finley.gmair.auth;

import finley.gmair.form.consumer.ConsumerForm;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequestMapping("auth")
@ComponentScan("finley.gmair.service")
@ComponentScan("finley.gmair.dao")
public class AuthenticationApplication {
    @Autowired
    private ConsumerService consumerService;

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResultData register(ConsumerForm form) {
        ResultData result = new ResultData();
        Consumer consumer = new Consumer(form.getName(), form.getWechat(), form.getAddressDetail(), form.getAddressProvince(), form.getAddressCity(), form.getAddressDistrict(), form.getPhone());
        ResultData response = consumerService.createConsumer(consumer);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK) {

        }
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {

        }
        return result;
    }
}
