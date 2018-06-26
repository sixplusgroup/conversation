package finley.gmair.controller;

import finley.gmair.model.machine.Ownership;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/machine/consumer")
public class ConsumerQRcodeController {
    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @RequestMapping(value="/bindwithqrcode",method = RequestMethod.POST)
    public ResultData bindConsumerWithQRcode(String consumerId, String bindName, String qrcode, Ownership ownership){
        ResultData result = new ResultData();

        return result;
    }

}
