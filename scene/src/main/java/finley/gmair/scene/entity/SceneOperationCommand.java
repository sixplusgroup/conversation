package finley.gmair.scene.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author : Lyy
 * @create : 2020-12-05 14:19
 **/
@Data
public class SceneOperationCommand implements Serializable {
    private static final long serialVersionUID = -1542262857541608015L;
    @Field(value = "cid")
    @JSONField(name = "cid")
    private Integer commandId;


    @Field(value = "mid")
    @JSONField(name = "mid")
    private String machineId;


    @Field(value = "qr_code")
    @JSONField(name = "qr_code")
    private String qrCode;

    @Field(value = "seq")
    @JSONField(name = "seq")
    private Integer sequence;

    @Field(value = "c_name")
    @JSONField(name = "c_name")
    private String commandName;

    @Field(value = "c_component")
    @JSONField(name = "c_component")
    private String commandComponent;

    @Field(value = "c_operation")
    @JSONField(name = "c_operation")
    private String commandOperation;

}
