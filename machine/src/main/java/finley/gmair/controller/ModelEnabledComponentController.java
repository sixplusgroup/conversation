package finley.gmair.controller;

import finley.gmair.model.machine.ModelEnabledComponent;
import finley.gmair.model.machine.QRCode;
import finley.gmair.service.ModelEnabledComponentService;
import finley.gmair.service.QRCodeService;
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
@RequestMapping("/machine/model/enabled/component")
public class ModelEnabledComponentController {
    @Autowired
    private ModelEnabledComponentService modelEnabledComponentService;


    @PostMapping("/create")
    public ResultData createModelEnabledComponent(String modelId, String componentName) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(modelId) || StringUtils.isEmpty(componentName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId and componentName");
            return result;
        }

        //check has been binded
        ResultData response = fetchModelEnabledComponent(modelId,componentName);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("the modelId and component has been binded,do not have to bind again");
            return result;
        }

        //bind the modelId with component
        ModelEnabledComponent modelEnabledComponent = new ModelEnabledComponent();
        modelEnabledComponent.setModelId(modelId);
        modelEnabledComponent.setComponentName(componentName);
        response = modelEnabledComponentService.create(modelEnabledComponent);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create model enabled component.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to create model enabled component.");
            return result;
        }
        return result;
    }

    @GetMapping("/fetch")
    public ResultData fetchModelEnabledComponent(String modelId, String componentName) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(modelId) || StringUtils.isEmpty(componentName)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the modelId and componentName");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId", modelId);
        condition.put("componentName", componentName);
        condition.put("blockFlag", false);
        ResultData response = modelEnabledComponentService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the modelId-component");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the modelId-component");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the modelId-component");
            return result;
        }
        return result;
    }

}
