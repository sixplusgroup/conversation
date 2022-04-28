package finley.gmair.vo.drift;

import finley.gmair.model.goods.DriftCommodityType;

import java.util.Date;

public class DriftOrderCommodityVo {
    private String commodityId;

    private String commodityName;

    private DriftCommodityType commodityType;

    private String commodityPrice;

    private boolean blockFlag;

    private Date createAt;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public DriftCommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(DriftCommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(String commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
