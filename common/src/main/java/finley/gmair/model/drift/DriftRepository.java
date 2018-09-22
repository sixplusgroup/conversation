package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftRepository extends Entity {
    private String repositoryId;

    private String goodsId;

    private int poolSize;

    public DriftRepository() {
        super();
    }

    public DriftRepository(String goodsId, int poolSize) {
        this();
        this.goodsId = goodsId;
        this.poolSize = poolSize;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
