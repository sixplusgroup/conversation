package finley.gmair.model.goods;

import finley.gmair.model.Entity;

public class Goods extends Entity {
    private String goodsId;

    private String goodsName;

    private String goodsDescription;

    private double goodsPrice;

    public Goods() {
        super();
    }

    public Goods(String goodsName, String goodsDescription, double goodsPrice) {
        this();
        this.goodsName = goodsName;
        this.goodsDescription = goodsDescription;
        this.goodsPrice = goodsPrice;
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

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
}
