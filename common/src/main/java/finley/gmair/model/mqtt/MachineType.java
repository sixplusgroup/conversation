package finley.gmair.model.mqtt;

import finley.gmair.model.Entity;

public class MachineType extends Entity {
    private String boardId;
    private int boardVersion;
    private String deviceName;
    private String typeName;

    public MachineType() {
        super();
    }

    public MachineType(int boardVersion, String deviceName, String typeName) {
        this();
        this.boardVersion = boardVersion;
        this.deviceName = deviceName;
        this.typeName = typeName;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public int getBoardVersion() {
        return boardVersion;
    }

    public void setBoardVersion(int boardVersion) {
        this.boardVersion = boardVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
