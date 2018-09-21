package finley.gmair.controller;

import finley.gmair.model.assemble.Barcode;
import finley.gmair.service.BarcodeService;
import finley.gmair.util.BarcodeGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/assemble/barcode")
public class BarcodeController {
    @Autowired
    private BarcodeService barcodeService;

    @PostMapping("/batch/create")
    public ResultData createBatch(int number){
        ResultData result = new ResultData();
        if(number<=0 || number>1000){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the number cannot less than 0 or more than 1000");
            return result;
        }
        List<String> codeList = BarcodeGenerator.generateList(number);
        List<Barcode> barcodeList = new ArrayList<>();
        for(String code:codeList){
            Barcode barcode = new Barcode(code);
            barcodeList.add(barcode);
        }
        ResultData response = barcodeService.createBatch(barcodeList);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create batch barcode");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to create batch barcode");
        return result;
    }
}
