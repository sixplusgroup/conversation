package finley.gmair.controller;

import finley.gmair.form.machine.ControlOptionForm;
import finley.gmair.model.machine.ControlOption;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.service.ControlOptionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.ControlOptionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/machine/control/option")
public class ControlOptionController {

    @Autowired
    private ControlOptionService controlOptionService;

    @PostMapping(value = "/create")
    public ResultData setControlOption(ControlOptionForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getOptionName()) || StringUtils.isEmpty(form.getOptionComponent())
                || StringUtils.isEmpty(form.getModelId()) || StringUtils.isEmpty(form.getActionName())
                || StringUtils.isEmpty(form.getActionOperator())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        String controlId = null;
        /** query database, verify the option exist or not
         *  if exist, don't create
         *  if not, create
         * */
        Map<String, Object> condition = new HashMap<>();
        condition.put("optionName", form.getOptionName());
        condition.put("optionComponent", form.getOptionComponent());
        ResultData response = controlOptionService.fetchControlOption(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("System parse error, please try later");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            controlId = ((List<ControlOptionVo>) response.getData()).get(0).getControlId();
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            ControlOption option = new ControlOption(form.getOptionName(), form.getOptionComponent());
            response = controlOptionService.createControlOption(option);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(response.getDescription());
                return result;
            }
            controlId = ((ControlOption) response.getData()).getControlId();
        }

        /**
         * create option action
         * */
        ControlOptionAction action = new ControlOptionAction(controlId, form.getModelId(),
                form.getActionName(), form.getActionOperator());
        response = controlOptionService.createControlOptionAction(action);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }
}