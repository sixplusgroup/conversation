package finley.gmair.scene.dto;

import finley.gmair.scene.entity.SceneOperationCommand;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SceneOperationDTO {
    private String id;
    private Long sceneId;
    // 场景内操作的命令
    private List<SceneOperationCommand> commands;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
