package finley.gmair.controller;

import finley.gmair.exception.MqttBusinessException;
import finley.gmair.service.ActionService;
import finley.gmair.util.ResultData;
import finley.gmair.util.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 行为处理
 *
 * @author lycheeshell
 * @date 2020/12/12 16:39
 */
@RestController
@RequestMapping("/mqtt/action")
public class ActionController {

    @Resource
    private ActionService actionService;

    /**
     * 保存行为
     *
     * @param name        行为标识名称的英文字符串
     * @param description 行为描述说明
     * @return 数据库变化条数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/save")
    public ResultData saveAction(String name, String description) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(name), "行为名称为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(description), "行为描述说明为空");

        return ResultData.ok(actionService.saveAction(name, description));
    }

    /**
     * 根据行为id查询不包含其属性的行为信息
     *
     * @param actionId 行为id
     * @return 行为
     * @throws MqttBusinessException 异常
     */
    @GetMapping(value = "/queryOne")
    public  ResultData queryOne(String actionId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(actionId), "行为id为空");

        return ResultData.ok(actionService.queryOneWithoutAttribute(actionId));
    }

    /**
     * 模糊查询，查询不包含其属性的行为信息
     *
     * @param name 行为标识名称的英文字符串
     * @return 行为列表
     */
    @GetMapping(value = "/queryActionsByName")
    public  ResultData queryActionsByName(String name) {
        if (StringUtils.isEmpty(name.trim())) {
            name = "";
        }
        return ResultData.ok(actionService.queryActionsWithoutAttributeByName(name.trim()));
    }

    /**
     * 删除行为
     *
     * @param actionId 行为id
     * @return 删除行数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/delete")
    public ResultData deleteAction(String actionId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(actionId), "行为id为空");

        return ResultData.ok(actionService.deleteAction(actionId));
    }

    /**
     * 新增行为与属性的对应关系
     *
     * @param actionId    行为id
     * @param attributeId 属性id
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/addActionAttribute")
    public ResultData addActionAttributeRelation(String actionId, String attributeId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(actionId), "行为id为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(attributeId), "属性id为空");

        return ResultData.ok(actionService.insertActionAttributeRelation(actionId, attributeId));
    }

    /**
     * 根据型号id查询该型号所有的行为
     *
     * @param modelId 型号id
     * @return 行为列表
     * @throws MqttBusinessException 异常
     */
    @GetMapping(value = "/queryActionsByModel")
    public  ResultData queryActionsByModel(String modelId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(modelId), "型号id为空");

        return ResultData.ok(actionService.queryActionsByModel(modelId));
    }

}
