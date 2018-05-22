package finley.gmair.model.machine;

public class Entity {
    protected boolean blockFlag;
    protected long createAt;

    public Entity() {
        super();
        this.blockFlag = false;
        this.createAt = System.currentTimeMillis();
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
