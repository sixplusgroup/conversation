package finley.gmair.controller;

import com.netflix.discovery.converters.Auto;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.Ownership;
import finley.gmair.model.machine.QRCode;
import finley.gmair.model.machine.QRCodeStatus;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.QRCodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/consumer")
public class ConsumerQRcodeController {
    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private QRCodeService qrCodeService;

    @RequestMapping(value = "/check/consumerid/accessto/qrcode", method = RequestMethod.POST)
    public ResultData checkConsumerAccesstoQRcode(String consumerId, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }

        //find if the consumerid-qrcode  exist in table consumer_qrcode_bind
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("this consumer have access to the qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the codeValue by the consumerId");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to check whether the consumer access to qrcode.");
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/qrcode/bind", method = RequestMethod.POST)
    public ResultData bindConsumerWithQRcode(String consumerId, String bindName, String qrcode, int ownership) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(bindName) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(Ownership.fromValue(ownership))) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }

        //check whether the bind exist
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("codeValue", qrcode);
        condition.put("ownership", ownership);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("exist bind,don't have to bind again");
            return result;
        }

        //save to consumer_qrcode_bind table
        ConsumerQRcodeBind consumerQRcodeBind = new ConsumerQRcodeBind();
        consumerQRcodeBind.setConsumerId(consumerId);
        consumerQRcodeBind.setBindName(bindName.trim());
        consumerQRcodeBind.setCodeValue(qrcode);
        consumerQRcodeBind.setOwnership(Ownership.fromValue(ownership));
        response = consumerQRcodeBindService.createConsumerQRcodeBind(consumerQRcodeBind);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to save the bind information to table");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find qrcode in prebind table");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to create bind information to table");
        }

        /*
         *update the qrcode table by qrcode, status  =>   QRCodeStatus.OCCUPIED
         * if ownership = 0, update the qrcode status
         * if ownership = 1, is sharer, no need to update
         */
        if (ownership == 0) {
            new Thread(() -> {
                condition.clear();
                condition.put("codeValue", qrcode);
                condition.put("status", QRCodeStatus.OCCUPIED.getValue());
                condition.put("blockFlag", false);
                qrCodeService.modifyByQRcode(condition);
            }).start();
        }
        return result;
    }

    @RequestMapping(value = "/qrcode/unbind", method = RequestMethod.POST)
    public ResultData unbindConsumerWithQRcode(String consumerId, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }

        //find the consumerId-codeValue correspond record in consumer_qrcode_bind table
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get consumer code bind");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find consumer code bind");
            return result;
        }
        ConsumerQRcodeBind consumerQRcodeBind = ((List<ConsumerQRcodeBind>) response.getData()).get(0);
        //according to the onwership,update the  consumer_qrcode_bind and qrcode table and code_machine_bind table
        condition.clear();
        condition.put("bindId", consumerQRcodeBind.getBindId());
        condition.put("codeValue", qrcode);
        if (consumerQRcodeBind.getOwnership() == Ownership.OWNER) {
            condition.put("status", QRCodeStatus.ASSIGNED.getValue());
            condition.put("blockFlag", false);
            new Thread(() -> {
                qrCodeService.modifyByQRcode(condition);
                condition.put("blockFlag", true);
                machineQrcodeBindService.modifyByQRcode(condition);
            }).start();
        } else {
            condition.put("consumerId", consumerId);
        }
        condition.put("blockFlag", true);
        response = consumerQRcodeBindService.modifyConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to unbind the consumer with the qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to unbind the consumer with the qrcode");
        }
        return result;
    }

    @RequestMapping(value = "/check/device/binded", method = RequestMethod.GET)
    public ResultData checkDeviceBinded(String consumerId, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the information");
            return result;
        }

        //check if the consumerId-codeValue has been created
        Map<String, Object> condition = new HashMap<>();
        condition.put("ownership", Ownership.OWNER);
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("the qrcode has been binded with someone");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to check if the device has been binded with someone");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("the qrcode has not been binded with any consumer");
            return result;
        }

        return result;
    }

    @RequestMapping(value = "/check/devicename/exist", method = RequestMethod.GET)
    public ResultData checkDeviceNameExist(String consumerId, String bindName) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId) || StringUtils.isEmpty(bindName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all the consumerId and bindName");
            return result;
        }

        //check bindName exist
        Map<String, Object> condition = new HashMap<>();
        condition.clear();
        condition.put("consumerId", consumerId);
        condition.put("bindName", bindName);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("exist device name");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to check the device name exist");
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("this device name has not been used");
        }
        return result;
    }

    @RequestMapping(value = "/machinelist", method = RequestMethod.GET)
    public ResultData getMachineListByConsumerId(String consumerId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(consumerId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the consumerId");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find device list by consumerid");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find device list by consumerid");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to fetch device list by consumerid");
        }
        return result;
    }

    //修改设备名称
    @RequestMapping(value = "/modify/bind/name", method = RequestMethod.POST)
    public ResultData modifyBindName(String qrcode, String bindName, String consumerId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(bindName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please the qrcode and bindName");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("consumerId", consumerId);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the qrcode");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the qrcode");
            return result;
        }
        condition.clear();
        ConsumerQRcodeBind bind = ((List<ConsumerQRcodeBind>) response.getData()).get(0);
        condition.put("bindId", bind.getBindId());
        condition.put("bindName", bindName);
        response = consumerQRcodeBindService.modifyConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to modify the bind name");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to modify the bind name");
            return result;
        }
        return result;
    }

    //根据二维码查qrcodeConsumerBind
    @GetMapping("/probe/by/qrcode")
    public ResultData probeBindByQRcode(String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the qrcode");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.clear();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the bind");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find bind");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to find bind");
            result.setData(response.getData());
            return result;
        }
        return result;
    }
}
