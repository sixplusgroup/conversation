package finley.gmair.controller;

import finley.gmair.service.CoreService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class CommunicationController {

//    @Autowired
//    @Qualifier("repository")
//    private GMRepository repository;

    @Autowired
    private CoreService coreService;

    @GetMapping("/one")
    public String test() {
        return null;
    }

    @PostMapping("/insertTest")
    public ResultData insertTest(){
        ResultData result = new ResultData();
        ResultData response = coreService.insert();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to add express company: ").append("").toString());
        }
        return result;
    }
}
