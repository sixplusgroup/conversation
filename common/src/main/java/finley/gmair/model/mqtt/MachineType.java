package finley.gmair.model.mqtt;

import finley.gmair.model.Entity;

public class MachineType extends Entity {
    private String boardId;
    private int boardVersion;
    private String type;

    public MachineType() {
        super();
    }

    public MachineType(int boardVersion, String type) {
        this();
        this.boardVersion = boardVersion;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
