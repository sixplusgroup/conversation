package finley.gmair.check;

import com.google.common.collect.Lists;
import finley.gmair.model.mqttManagement.Model;
import finley.gmair.service.ModelService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Resource
    private ModelService modelService;

    private Map<String, List<String>> modelActionsMap;

    /**
     * 从数据库加载型号的消息可以包含的行为列表并记录到内存中
     */
    @PostConstruct
    public void init() {
        modelActionsMap = new ConcurrentHashMap<>(20);
        List<Model> modelList = modelService.queryModelsWithAction();
        for (Model model: modelList) {
            List<String> actions = Lists.newArrayList();
            model.getActions().forEach(action -> actions.add(action.getName()));
            modelActionsMap.put(model.getName(), actions);
        }
    }

    /**
     * 检查型号的规定行为中是否含有该行为
     *
     * @param modelName  型号名称
     * @param actionNome 行为名称
     * @return 含有返回true，非法不含有返回false
     */
    public boolean isModelHaveAction(String modelName, String actionNome) {
        if (!modelActionsMap.containsKey(modelName)) {
            return false;
        }
        return modelActionsMap.get(modelName).contains(actionNome);
    }

    /**
     * 在内存中添加某型号机器消息可以包含的行为
     *
     * @param modelName  型号名称
     * @param actionNome 行为名称
     */
    public void addAction(String modelName, String actionNome) {
        List<String> actions;
        if (modelActionsMap.containsKey(modelName)) {
            actions = modelActionsMap.get(modelName);
            actions.add(actionNome);
        } else {
            actions = Lists.newArrayList();
            actions.add(actionNome);
            modelActionsMap.put(modelName, actions);
        }
    }

    /**
     * 在内存中删除某型号机器消息可以包含的行为
     *
     * 该方法废弃，方法没有实现，因为可能会造成之前售出的设备发出的消息所含有的行为无法通过校验
     */
    @Deprecated
    public void removeAction() {
    }

}

