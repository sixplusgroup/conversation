package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class Goods extends Entity {
    private String goodsId;

    private String goodsName;

    public Goods() {
        super();
    }

    public Goods(String goodsName) {
        this();
        this.goodsName = goodsName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
