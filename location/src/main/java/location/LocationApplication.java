package location;

import finley.gmair.util.ResultData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequestMapping("/location")
public class LocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/address/resolve")
    public ResultData geocoder(String address) {
        ResultData result = new ResultData();
        
        return result;
    }
}
