package finley.gmair.controller;

import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reception/machine")
public class MachineController {

    @Autowired
    private MachineService machineService;

    /**
     * This method is invoked to finish user-qrcode binding
     *
     * @return bind result, RESPONSE_OK & RESPONSE_ERROR
     * @Param qrcode, the code value of the specified machine
     */
    @PostMapping("/bind")
    public ResultData bind(String qrcode) {
        ResultData result = new ResultData();
        //check whether the qrcode is online
        return result;
    }

    @PostMapping("/{component}/{operation}")
    public ResultData configComponentStatus(@PathVariable("operation") String operation, String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(operation) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided");
            return result;
        }
        
        return result;
    }
}
