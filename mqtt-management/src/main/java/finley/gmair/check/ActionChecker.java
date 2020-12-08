package finley.gmair.check;

import finley.gmair.model.mqttManagement.Attribute;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 行为校验器
 *
 * @author lycheeshell
 * @date 2020/12/5 00:12
 */
@Component
public class ActionChecker {

    /**
     * 从数据库加载行为可以包含的属性并记录到内存中
     */
    @PostConstruct
    public void init() {
        //todo
    }

    /**
     * 检查json包含的属性是否符合行为的规范
     *
     * @param actionName   行为名称
     * @param json  包含多种属性的json
     * @return 规范返回true，不规范返回false
     */
    public boolean isJsonCorrectInAction(String actionName, String json) {
        //todo
        return false;
    }

    /**
     * 在内存中添加行为可以包含的属性
     *
     * @param actionName  行为名称
     * @param attribute 属性
     */
    public void addAttribute(String actionName, Attribute attribute) {

    }

    /**
     * 在内存中删除行为可以包含的属性
     *
     * @param actionName  行为名称
     * @param attribute 属性
     */
    public void removeAttribute(String actionName, Attribute attribute) {

    }

}
