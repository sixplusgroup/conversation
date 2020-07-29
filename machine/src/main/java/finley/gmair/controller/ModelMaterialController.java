package finley.gmair.controller;

import finley.gmair.service.MapModelMaterialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 返回设别型号对应的购买链接
     *
     * @author zm
     * @param [modelId] 设备型号
     * @return finley.gmair.util.ResultData
     * @date 2020/7/29 0029 13:32
     **/
    public ResultData getMaterial(String modelId) {

        ResultData resultData = new ResultData();

        //check empty
        if (StringUtils.isEmpty(modelId)) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("please provide modelId");
            return resultData;
        }

        ResultData response = modelMaterialService.getMaterial(modelId);

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
