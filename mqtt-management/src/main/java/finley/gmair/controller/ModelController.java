package finley.gmair.controller;

import finley.gmair.exception.MqttBusinessException;
import finley.gmair.service.ModelService;
import finley.gmair.util.ResultData;
import finley.gmair.util.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/mqtt/model")
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
    @PostMapping(value = "/save")
    public ResultData saveModel(String name, String description) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(name), "型号标示名称为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(description), "型号描述说明为空");

        return ResultData.ok(modelService.saveModel(name, description));
    }

    /**
     * 根据型号id查询不包含其行为的型号信息
     *
     * @param modelId 型号id
     * @return 型号
     * @throws MqttBusinessException 异常
     */
    @GetMapping(value = "/queryOne")
    public ResultData queryOne(String modelId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(modelId), "型号id为空");

        return ResultData.ok(modelService.queryOneWithoutAction(modelId));
    }

    /**
     * 查询不包含其行为的型号信息
     *
     * @param name 型号标识名称的英文字符串
     * @return 型号列表
     * @throws MqttBusinessException 异常
     */
    @GetMapping(value = "/queryModels")
    public ResultData queryModels(String name) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(name), "型号标示名称为空");

        return ResultData.ok(modelService.queryModelsWithoutAction(name));
    }

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId  型号id
     * @param actionId 行为id
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/addModelAction")
    public ResultData addModelActionRelation(String modelId, String actionId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(modelId), "型号id为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(actionId), "行为id为空");

        return ResultData.ok(modelService.insertModelActionRelation(modelId, actionId));
    }

}
