package finley.gmair.controller;

import finley.gmair.service.ComputeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    ComputeClient computeService ;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Integer add() {
        return computeService.add(10, 20);
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return computeService.hello();
    }
}
