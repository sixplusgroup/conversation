package finley.gmair.controller;

import finley.gmair.form.machine.QRCodeCreateForm;
import finley.gmair.form.machine.QRCodeForm;
import finley.gmair.model.goods.GoodsModel;
import finley.gmair.model.machine.QRCode;
import finley.gmair.service.GoodsService;
import finley.gmair.service.QrcodeService;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * qrcode is a representation of machine once it is bind with a machine id
 * user's settings will be related to qrcode
 * qrcode should have several status, created, assigned, occupied, recalled
 */
@RestController
@RequestMapping("/machine/qrcode")
public class QRCodeController {

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private GoodsService goodsService;

    /**
     * This method is used to create a record of qrcode
     *
     * @return
     */
    @PostMapping("/create/one")
    public ResultData createOne(QRCodeForm form) {
        ResultData result = new ResultData();
        String modelId = form.getModelId();
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Empty modelId parameter: " + modelId);
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        ResultData response = goodsService.fetchModel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Incorrect modelId parameter: " + modelId);
            return result;
        }
        GoodsModel gmVo = ((List<GoodsModel>) response.getData()).get(0);
        String batch = new StringBuffer(gmVo.getModelCode()).append(form.getBatchValue()).toString();
        response = qrcodeService.create(form.getGoodsId(), form.getModelId(), batch, 1);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Sorry, the qrcode is not generated as expected, please try again");
        return result;
    }

    /**
     * This method is used to create a batch of qrcode
     *
     * @return
     */
    @PostMapping("/create")
    public ResultData create(QRCodeCreateForm form) {
        ResultData result = new ResultData();
        String modelId = form.getModelId();
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Empty modelId parameter: " + modelId);
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        ResultData response = goodsService.fetchModel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Incorrect modelId parameter: " + modelId);
            return result;
        }
        GoodsModel gmVo = ((List<GoodsModel>) response.getData()).get(0);
        String batch = new StringBuffer(gmVo.getModelCode()).append(form.getBatchValue()).toString();
        response = qrcodeService.create(form.getGoodsId(), form.getModelId(), batch, form.getNum());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            String filename = generateZip(batch);
            result.setData(filename);
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Sorry, the qrcodes are not generated as expected, please try again");
        return result;
    }

    private String generateZip(String batchValue) {
        // judge whether the batch no is illegal, including emptry and not exist
        if (StringUtils.isEmpty(batchValue)) {
            return "";
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("batchValue", batchValue);
        ResultData response = qrcodeService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return "";
        }
        // read all qrcodes in the batch, generate a zip file
        String base = PathUtil.retrivePath();
        File directory = new File(new StringBuffer(base).append("/material/zip").toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String tempSerial = IDGenerator.generate("ZIP");
        File zip = new File(
                new StringBuffer(base).append("/material/zip/").append(tempSerial).append(".zip").toString());
        if (!zip.exists()) {
            try {
                zip.createNewFile();
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        }
        List<QRCode> list = (List<QRCode>) response.getData();
        File[] files = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            File file = new File(new StringBuffer(PathUtil.retrivePath()).append(list.get(i)).toString());
            files[i] = file;
        }
        return tempSerial;
    }
}
