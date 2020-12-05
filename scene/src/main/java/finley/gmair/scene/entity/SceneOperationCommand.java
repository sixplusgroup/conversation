package finley.gmair.scene.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author : Lyy
 * @create : 2020-12-05 14:19
 **/
@Data
public class SceneOperationCommand {
    @Field(value = "mid")
    private String machineId;
    @Field(value = "qr_code")
    private String qrCode;
    @Field(value = "cid")
    private String consumerId;
    @Field(value = "seq")
    private Integer sequence;
    @Field(value = "c_name")
    private String commandName;
    @Field(value = "c_component")
    private String commandComponent;
    @Field(value = "c_operation")
    private String commandOperation;

}
