package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.model.machine.Ownership;
import finley.gmair.model.machine.QRCodeStatus;
import finley.gmair.service.*;
import finley.gmair.service.impl.RedisService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Autowired
    private AuthConsumerService authConsumerService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OutPm25DailyService outPm25DailyService;

    @Autowired
    private MachineListDailyService machineListDailyService;

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
        if (consumerQRcodeBind.getOwnership() == Ownership.OWNER) {
            new Thread(() -> {
                condition.clear();
                condition.put("codeValue", qrcode);
                condition.put("status", QRCodeStatus.ASSIGNED.getValue());
                condition.put("blockFlag", false);
                qrCodeService.modifyByQRcode(condition);

                condition.clear();
                condition.put("codeValue", qrcode);
                condition.put("blockFlag", true);
                machineQrcodeBindService.modifyByQRcode(condition);
            }).start();


            condition.clear();
            condition.put("codeValue", qrcode);
            condition.put("blockFlag", false);
            ResultData temp = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
            if (temp.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<ConsumerQRcodeBind> list = (List<ConsumerQRcodeBind>) temp.getData();
                for (ConsumerQRcodeBind cqb : list) {
                    condition.put("bindId", cqb.getBindId());
                    condition.put("blockFlag", true);
                    response = consumerQRcodeBindService.modifyConsumerQRcodeBind(condition);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                        result.setDescription("fail to set the bindId " + cqb.getBindId() + "'s block flag = true");
                        return result;
                    }
                }
            } else if (temp.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to unbind the consumer list with qrcode");
                return result;
            } else if (temp.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("not find the consumer list with qrcode");
                return result;
            }
        } else {
            condition.put("bindId", consumerQRcodeBind.getBindId());
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
    public ResultData probeBindByQRcode(String qrcode, String consumerId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the qrcode");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.clear();
        condition.put("codeValue", qrcode);
        condition.put("consumerId", consumerId);
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

    @GetMapping("/owner/machine/list")
    public ResultData getOwnerMachineList(int curPage, int pageSize, String qrcode, String phone, String createTimeGTE, String createTimeLTE, String online, String overCount, String overCountGTE, String overCountLTE) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(phone)) {
            condition.put("phone", phone);
        }
        if (!StringUtils.isEmpty(qrcode)) {
            condition.put("codeValue", qrcode);
        }
        if (!StringUtils.isEmpty(createTimeGTE)) {
            condition.put("createTimeGTE", new Timestamp(Long.parseLong(createTimeGTE)));
        }
        if (!StringUtils.isEmpty(createTimeLTE)) {
            condition.put("createTimeLTE", new Timestamp(Long.parseLong(createTimeLTE)));
        }
        if (!StringUtils.isEmpty(overCount)) {
            int ovc = Integer.parseInt(overCount);
            condition.put("overCount", ovc);
        }
        if (!StringUtils.isEmpty(overCountGTE)) {
            int ovcGTE = Integer.parseInt(overCountGTE);
            condition.put("overCountGTE", ovcGTE);
        }
        if (!StringUtils.isEmpty(overCountLTE)) {
            int ovcLTE = Integer.parseInt(overCountLTE);
            condition.put("overCountLTE", ovcLTE);
        }
        condition.put("start", (curPage - 1) * pageSize);
        condition.put("pageSize", pageSize);
        ResultData response = machineListDailyService.queryMachineListDaily(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find any data according to your condition");
            return result;
        }

        List<MachineListDaily> resultList = (List<MachineListDaily>) response.getData();
        int totalPage = resultList.size() / pageSize + 1;
        if (curPage < 1 || curPage > totalPage) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to got that page because that page not exist");
            return result;
        }
        resultList = resultList.subList((curPage - 1) * pageSize, Math.min(curPage * pageSize, resultList.size()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalPage", totalPage);
        jsonObject.put("machineList", resultList);
        result.setData(jsonObject);
        result.setDescription("success to fetch data");
        return result;
    }
}
