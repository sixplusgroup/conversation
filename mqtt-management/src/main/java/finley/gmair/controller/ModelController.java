package finley.gmair.controller;

import finley.gmair.exception.MqttBusinessException;
import finley.gmair.service.ModelService;
import finley.gmair.util.ResultData;
import finley.gmair.util.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备型号处理
 *
 * @author lycheeshell
 * @date 2020/12/12 14:21
 */
@RestController
@RequestMapping("/mqttManagement/model")
public class ModelController {

    @Resource
    private ModelService modelService;

    /**
     * 保存机器型号
     *
     * @param name        型号标识名称的英文字符串
     * @param description 机器型号的描述说明
     * @return 数据库变化条数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/create")
    public ResultData saveModel(String name, String description) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(name), "型号标示名称为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(description), "型号描述说明为空");

        return ResultData.ok(modelService.saveModel(name, description));
    }


}
