package finley.gmair.check;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 设备型号校验器
 *
 * 注意高并发的风险，map使用ConcurrentHashMap
 *
 * @author lycheeshell
 * @date 2020/12/4 23:51
 */
@Component
public class ModelChecker {

    /**
     * 从数据库加载型号的消息可以包含的行为列表并记录到内存中
     */
    @PostConstruct
    public void init() {
        //todo
    }

    /**
     * 检查型号的规定行为中是否含有该行为
     *
     * @param modelName  型号名称
     * @param actionNome 行为名称
     * @return 含有返回true，非法不含有返回false
     */
    public boolean isModelHaveAction(String modelName, String actionNome) {
        //todo
        return false;
    }

    /**
     * 在内存中添加某型号机器消息可以包含的行为
     *
     * @param modelName  型号名称
     * @param actionNome 行为名称
     */
    public void addAction(String modelName, String actionNome) {

    }

    /**
     * 在内存中删除某型号机器消息可以包含的行为
     *
     * @param modelName  型号名称
     * @param actionNome 行为名称
     */
    public void removeAction(String modelName, String actionNome) {

    }


}

