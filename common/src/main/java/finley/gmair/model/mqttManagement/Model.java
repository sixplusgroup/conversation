package finley.gmair.model.mqttManagement;

import finley.gmair.model.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 机器的型号
 *
 * @author lycheeshell
 * @date 2020/12/4 23:35
 */
@Getter
@Setter
public class Model extends Entity {

    /**
     * 型号id
     */
    private String modelId;

    /**
     * 型号标识名称的英文字符串
     */
    private String name;

    /**
     * 型号描述说明
     */
    private String description;

    /**
     * 该型号机器的消息可以包含哪些行为
     */
    private List<Action> actions;

    public Model() {
        super();
    }

}
