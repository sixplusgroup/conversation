package finley.gmair.model.goods;

import finley.gmair.model.EnumValue;

public enum CommodityType implements EnumValue {

    GUOMAI_XINFENG(0), GUOMAI_SCREEN(1), GUOMAI_OTHER(2);

    private int value;

    CommodityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static CommodityType convertToCommodityType(int code) {
        CommodityType commodityType = CommodityType.GUOMAI_XINFENG;
        switch (code) {
            case 0: commodityType = CommodityType.GUOMAI_XINFENG; break;
            case 1: commodityType = CommodityType.GUOMAI_SCREEN; break;
            case 2: commodityType = CommodityType.GUOMAI_OTHER; break;
            default: break;
        }
        return commodityType;
    }
}
