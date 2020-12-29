package finley.gmair.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import finley.gmair.model.mqttManagement.Action;
import finley.gmair.model.mqttManagement.Attribute;
import finley.gmair.service.ActionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 行为校验器
 *
 * 注意高并发的风险，map使用ConcurrentHashMap
 *
 * @author lycheeshell
 * @date 2020/12/5 00:12
 */
@Component
public class ActionChecker {

    @Resource
    private ActionService actionService;

    private Map<String, List<Attribute>> actionAttributeMap;

    /**
     * 从数据库加载行为可以包含的属性并记录到内存中
     */
    @PostConstruct
    public void init() {
        actionAttributeMap = new ConcurrentHashMap<>(20);
        List<Action> actionList = actionService.queryActionsWithAttribute();
        for (Action action : actionList) {
            actionAttributeMap.put(action.getName().toUpperCase(), action.getAttributes());
        }
    }

    /**
     * 检查json包含的属性是否符合行为的规范
     *
     * @param actionName   行为名称
     * @param json  包含多种属性的json
     * @return 规范返回true，不规范返回false
     */
    public boolean isJsonCorrectInAction(String actionName, String json) {
        actionName = actionName.toUpperCase();
        if (!actionAttributeMap.containsKey(actionName)) {
            return false;
        }
        List<Attribute> checkAttributes = actionAttributeMap.get(actionName);

        //json不为空
        if(StringUtils.isNotEmpty(json)) {
            JSONObject payload;
            try {
                payload = JSON.parseObject(json);
            } catch (Exception e) {
                return false;
            }

            // 1.检查json里面的属性都是规定的
            for (String payloadAttribute : payload.keySet()) {
                boolean isCorrect = false;
                for (Attribute normalAttribute : checkAttributes) {
                    if (normalAttribute.getName().equals(payloadAttribute)) {
                        isCorrect = true;
                        break;
                    }
                }
                if (!isCorrect) {
                    return false;
                }
            }

            // 2.检查必传属性在json中是否含有
            for (Attribute normalAttribute : checkAttributes) {
                if (normalAttribute.getRequired() && (!payload.containsKey(normalAttribute.getName()))) {
                    return false;
                }
            }

        } else {

            //json字符串为空，检查是否有必传属性
            for (Attribute normalAttribute : checkAttributes) {
                if (normalAttribute.getRequired()) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * 在内存中添加行为可以包含的属性
     *
     * @param actionName  行为名称
     * @param attribute 属性
     */
    public void addAttribute(String actionName, Attribute attribute) {
        List<Attribute> attributeList;
        if (actionAttributeMap.containsKey(actionName)) {
            attributeList = actionAttributeMap.get(actionName);
            attributeList.add(attribute);
        } else {
            attributeList = Lists.newArrayList();
            attributeList.add(attribute);
            actionAttributeMap.put(actionName, attributeList);
        }
    }

    /**
     * 在内存中删除行为可以包含的属性
     *
     * 该方法废弃，方法没有实现，因为可能会造成之前售出的设备的消息所含有的行为找不到对应的属性无法通过校验
     */
    @Deprecated
    public void removeAttribute() {
    }

}
