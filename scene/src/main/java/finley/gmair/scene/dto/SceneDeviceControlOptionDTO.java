package finley.gmair.scene.dto;

import com.alibaba.fastjson.annotation.JSONField;
import finley.gmair.scene.entity.SceneOperationCommand;
import lombok.Data;

import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-26 15:39
 * @description 场景内设备控制选项
 **/

@Data
public class SceneDeviceControlOptionDTO {

    @JSONField(name = "consumer_id")
    private String consumerId;

    private Device device;

    @JSONField(name = "commands")
    private List<SceneOperationCommand> commands;
}
