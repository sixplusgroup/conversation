package finley.gmair.scene.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SceneDTO {
    private long sceneId;
    // 场景所属对象即命令的执行人
    private String consumerId;
    // 场景名称
    private String name;
    // 场景内的所有设备
    private List<String> qrCodes;
    // 场景内的相关数值
    private double pm25;
    private double temperature;
    private double humidity;
    private double co2;
    // 场景内可执行的操作
    private List<CommandDTO> commands;
    // 当前场景所处状态
    private String status;
    // 创建日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 更新日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
