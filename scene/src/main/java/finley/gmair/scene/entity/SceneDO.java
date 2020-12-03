package finley.gmair.scene.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName(value = "scene")
public class SceneDO {
    @TableId(value = "scene_id", type = IdType.ASSIGN_ID)
    private Long sceneId;

    @TableField(value = "consumer_id")
    // 场景所属对象即命令的执行人
    private String consumerId;
    // 场景名称
    private String name;
    // 场景内的相关数值
    private double pm25;
    private double temperature;
    private double humidity;
    private double co2;
    // 当前场景所处状态
    private String status;

    // 创建日期
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新日期
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
