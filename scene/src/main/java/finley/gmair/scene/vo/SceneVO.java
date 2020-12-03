package finley.gmair.scene.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SceneVO {
    @JsonProperty("sceneId")
    private long id;
    // 场景名称
    private String name;
    // 场景内的所有设备
    private List<String> qrCodes;
    // 场景内的相关数值
    private double pm25;
    private double co2;
    private double temperature;
    private double humidity;

    // 当前场景所处状态
    private String status;
}
