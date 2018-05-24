package finley.gmair.model.goods;

public class OrderCommodity extends Goods {
    private CommodityType commodityType;

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }
}
