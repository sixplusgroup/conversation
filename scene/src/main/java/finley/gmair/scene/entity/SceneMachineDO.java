package finley.gmair.scene.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : Lyy
 * @description : 场景内所包含的设备表
 * @create : 2020-12-03 19:20
 **/
@Data
@TableName(value = "scene_machine")
public class SceneMachineDO {
    @TableId(type = IdType.AUTO)
    private long id;

    // 场景ID
    @TableField(value = "scene_id")
    private long sceneId;

    @TableField(value = "consumer_id")
    // 场景所属对象即命令的执行人
    private String consumerId;
    // 场景名称
    private String name;

    // 场景内设备id
    @TableField(value = "machine_id")
    private String machineId;

    // 场景内的设备二维码
    @TableField(value = "qr_code")
    private String qrCode;

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
