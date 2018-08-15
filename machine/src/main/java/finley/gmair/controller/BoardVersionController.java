package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.machine.BoardVersionForm;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.service.BoardVersionService;
import finley.gmair.service.MachineQrcodeBindService;
import finley.gmair.service.PreBindService;
import finley.gmair.service.QRCodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine/board")
public class BoardVersionController {

    @Autowired
    private BoardVersionService boardVersionService;

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private PreBindService preBindService;

    //绑定machineId(板子的id)和version(板子的版本)
    @PostMapping("/record/single")
    public ResultData recordSingleBoardVersion(BoardVersionForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getMachineId()) || StringUtils.isEmpty(form.getVersion())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }
        BoardVersion version = new BoardVersion(form.getMachineId(), form.getVersion());
        ResultData response = boardVersionService.createBoardVersion(version);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to create board version with information: ").append(JSON.toJSONString(version)).toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    //通过machineId查找板子的版本
    @GetMapping("/record/list")
    public ResultData searchBoardVersion(String machineId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(machineId)) {
            condition.put("machineId", machineId);
        }
        ResultData response = boardVersionService.fetchBoardVersion(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find any board version information.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No board version information found.");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @GetMapping("/by/qrcode")
    public ResultData findBoardVersionByQRcode(String qrcode){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the qrcode");
            return result;
        }
        //according the qrcode, find the machineId.
        Map<String,Object> condition = new HashMap<>();
        condition.put("codeValue",qrcode);
        condition.put("blockFlag",false);
        ResultData response = machineQrcodeBindService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the qrcode");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the qrcode");
            return result;
        }
        String machineId = ((List<MachineQrcodeBindVo>)response.getData()).get(0).getMachineId();
        return searchBoardVersion(machineId);
    }

    @PostMapping(value = "/bind/batch")
    public ResultData bindBatchVersion(String bindList){
        ResultData result =  new ResultData();
        if (StringUtils.isEmpty(bindList)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the bind list");
            return result;
        }
        JSONArray jsonArray = JSONArray.parseArray(bindList);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            //make sure codeValue exist in qrcode table,codeValue has not been binded (in pre_bind table)
            //and machineId has not been binded (in pre_bind table)
            Map<String, Object> condition = new HashMap<>();
            condition.put("codeValue", jsonObject.getString("codeValue"));
            condition.put("blockFlag", false);
            ResultData response = qrCodeService.fetch(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;

            condition.clear();
            condition.put("codeValue", jsonObject.getString("codeValue"));
            condition.put("blockFlag", false);
            response = preBindService.fetch(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_NULL)
                continue;

            condition.clear();
            condition.put("machineId", jsonObject.getString("machineId"));
            condition.put("blockFlag", false);
            response = preBindService.fetch(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_NULL)
                continue;

            //bind machineId with version
            BoardVersionForm boardVersionForm = new BoardVersionForm();
            boardVersionForm.setMachineId(jsonObject.getString("machineId"));
            boardVersionForm.setVersion(jsonObject.getIntValue("version"));
            recordSingleBoardVersion(boardVersionForm);

            //bind machineId with codeValue into pre_bind table
            PreBindCode code = new PreBindCode(jsonObject.getString("machineId"), jsonObject.getString("codeValue"));
            preBindService.create(code);
        }
        return result;
    }
}
