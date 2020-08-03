package finley.gmair.controller;

import finley.gmair.service.MapModelMaterialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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


    @GetMapping(value = "/getMaterials")
    /**
     * 返回设别型号对应的购买链接
     *
     * @author zm
     * @param [modelId] 设备型号
     * @return finley.gmair.util.result
     * @date 2020/7/29 0029 13:32
     **/
    public ResultData getMaterial(String modelId) {

        ResultData result = new ResultData();

        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide modelId");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("modelId",modelId);
        ResultData response = modelMaterialService.fetch(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription(response.getDescription());
            return result;
        }
        return result;
    }
}
