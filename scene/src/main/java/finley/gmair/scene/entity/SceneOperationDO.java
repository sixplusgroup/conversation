package finley.gmair.scene.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Lyy
 * @create : 2020-12-05 14:17
 **/
@Data
@Document(collection = "scene_operation")
public class SceneOperationDO {
    @Id
    private String id;
    @Field(value = "sid")
    private Long sceneId;

    @Field("commands")
    private List<SceneOperationCommand> commands;

    @Field("create_time")
    private LocalDateTime createTime;
    @Field("update_time")
    private LocalDateTime updateTime;
}