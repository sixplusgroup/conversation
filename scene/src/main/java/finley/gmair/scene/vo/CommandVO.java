package finley.gmair.scene.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : Lyy
 * @create : 2020-12-04 17:19
 **/
@Data
public class CommandVO {
    private String id;
    private Long sceneId;
    private String machineId;
    private String consumerId;
    private Integer sequence;
    private String qrCode;
    private String commandName;
    private String commandValue;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
