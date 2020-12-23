package finley.gmair.controller;

import finley.gmair.service.impl.ModeFilterServiceImpl;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zm
 * @Description 设备型号-耗材控制器
 * @Date 2020/7/28 0028 15:45
 **/
@RestController
@RequestMapping("/machine/model")
public class ModelFilterController {

    @Resource
    private ModeFilterServiceImpl modeFilterService;


    /**
     * @param [modelId] 机器型号
     * @return finley.gmair.util.result
     * @author zm
     * @date 2020/7/29 0029 13:32
     */
    @GetMapping(value = "/filterLinks")
    public ResultData getFilterLinks(
            @RequestParam("modelId") String modelId
    ) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(modelId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide modelId");
            return result;
        }

        ResultData response = modeFilterService.fetch(modelId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }

        result.setDescription(response.getDescription());
        return result;
    }
}