package finley.gmair.scene.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import finley.gmair.scene.entity.SceneOperationCommand;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SceneOperationDTO implements Serializable {
    private static final long serialVersionUID = 7748805393641457489L;
    @JSONField(serialize = false)
    private String id;
    @JSONField(name = "scene_id")
    private Long sceneId;
    @JSONField(name = "scene_name")
    private String sceneName;
    @JSONField(name = "consumer_id")
    private String consumerId;
    // 场景内操作的命令
    @JSONField(name = "commands")
    private List<SceneOperationCommand> commands;
    @JSONField(name = "create_time")
    private LocalDateTime createTime;
    @JSONField(name = "update_time")
    private LocalDateTime updateTime;
}
