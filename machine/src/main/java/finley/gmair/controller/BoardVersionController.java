package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.form.machine.BoardVersionForm;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.service.BoardVersionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/machine/board")
public class BoardVersionController {

    @Autowired
    private BoardVersionService boardVersionService;

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
            condition.put("machineId", false);
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
}
