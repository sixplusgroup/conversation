package finley.gmair.controller;

import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * qrcode is a representation of machine once it is bind with a machine id
 * user's settings will be related to qrcode
 * qrcode should have several status, created, assigned, occupied, recalled
 */
@RestController
@RequestMapping("/machine/qrcode")
public class QRCodeController {

    /**
     * This method is used to create a record of qrcode
     *
     * @return
     */
    @PostMapping("/create/one")
    public ResultData createOne() {
        ResultData result = new ResultData();

        return result;
    }

    /**
     * This method is used to create a batch of qrcode
     *
     * @return
     */
    @PostMapping("/create")
    public ResultData create() {
        ResultData result = new ResultData();

        return result;
    }
}
