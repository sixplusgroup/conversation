package finley.gmair.controller;

import finley.gmair.exception.MqttBusinessException;
import finley.gmair.service.AttributeService;
import finley.gmair.util.ResultData;
import finley.gmair.util.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 属性处理
 *
 * @author lycheeshell
 * @date 2020/12/13 16:13
 */
@RestController
@RequestMapping("/mqtt/attribute")
public class AttributeController {

    @Resource
    private AttributeService attributeService;

    /**
     * 保存属性
     *
     * @param name        属性标示名称英文字符串
     * @param description 属性描述
     * @param required    该属性是否必须有
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/save")
    public ResultData saveAttribute(String name, String description, Boolean required) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(name), "属性名称为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(description), "属性描述说明为空");
        VerifyUtil.verify(required != null, "属性是否必须为空");

        return ResultData.ok(attributeService.saveAttribute(name, description, required));
    }

    /**
     * 根据属性id查询属性信息
     *
     * @param attributeId 属性id
     * @return 属性
     * @throws MqttBusinessException 异常
     */
    @GetMapping(value = "/queryOne")
    public ResultData queryOne(String attributeId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(attributeId), "属性id为空");

        return ResultData.ok(attributeService.queryOne(attributeId));
    }

    /**
     * 模糊查询，查询属性信息
     *
     * @param name 名称
     * @return 属性列表
     */
    @GetMapping(value = "/queryAttributesByName")
    public ResultData queryAttributesByName(String name) {
        if (StringUtils.isEmpty(name.trim())) {
            name = "";
        }

        return ResultData.ok(attributeService.queryAttributesByName(name.trim()));
    }

    /**
     * 根据行为id查询该行为所有的属性
     *
     * @param actionId 行为id
     * @return 属性列表
     * @throws MqttBusinessException 异常
     */
    @GetMapping(value = "/queryAttributesByAction")
    public ResultData queryAttributesByAction(String actionId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(actionId), "行为id为空");

        return ResultData.ok(attributeService.queryAttributesByAction(actionId));
    }

}
