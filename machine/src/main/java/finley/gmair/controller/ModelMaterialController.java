package finley.gmair.controller;

import finley.gmair.model.machine.ModelLight;
import finley.gmair.service.MapModelMaterialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 设备型号-耗材控制器
 * @Author zm
 * @Date 2020/7/28 0028 15:45
 **/
@RestController
@RequestMapping("/machine/model")
public class ModelMaterialController {

    @Autowired
    private MapModelMaterialService modelMaterialService;


    @GetMapping(value = "/getMaterial")
    /**
     *
     * @author zm
     * @param [modelId] 设备型号
     * @return 返回设别型号对应的所有耗材信息：1. 耗材名称；2. 耗材购买链接
     * @date 2020/7/28 0028 15:49
     **/
    public ResultData getMapModelMaterial(String modelId) {

        ResultData resultData = new ResultData();

        //check empty
        if (StringUtils.isEmpty(modelId)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("please provide modelId");
            return resultData;
        }

        ResultData response = modelMaterialService.getModelMaterialMap(modelId);

        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return response;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            resultData.setData(response.getData());
            resultData.setDescription(response.getDescription());
            return resultData;
        }
        return resultData;
    }
}
