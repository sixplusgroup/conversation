package finley.gmair.scene.dto;

import lombok.Data;

@Data
public class CommandDTO {
    private long id;
    // 设备二维码
    private String qrCode;
    // 命令执行次序
    private int sequence;
    // 具体需要执行的指令
    private String command;
}
