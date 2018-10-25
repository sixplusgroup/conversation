package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftRepository extends Entity {
    private String repositoryId;

    private String equipId;

    private int poolSize;

    public DriftRepository() {
        super();
    }

    public DriftRepository(String equipIdId, int poolSize) {
        this();
        this.equipId = equipIdId;
        this.poolSize = poolSize;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
