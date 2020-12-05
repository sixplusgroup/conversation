package finley.gmair.scene.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SceneDTO implements Serializable {

    private static final long serialVersionUID = -6721656051044346730L;

    @JSONField(name = "scene_id")
    private Long id;

    // 场景所属对象即命令的执行人
    @JSONField(name = "consumer_id")
    private String consumerId;
    // 场景名称
    private String name;

    // 场景内的相关数值
    private double pm25;
    private double temperature;
    private double humidity;
    private double co2;

    // 场景内执行的操作
    @JSONField(name = "scene_operation")
    private SceneOperationDTO sceneOperation;
    // 当前场景所处状态
    private String status;

    // 创建日期
    @JSONField(name = "create_time")
    private LocalDateTime createTime;
    // 更新日期
    @JSONField(name = "update_time")
    private LocalDateTime updateTime;

    // 场景内的所有设备(冗余字段)
    private List<String> qrCodes;
}
