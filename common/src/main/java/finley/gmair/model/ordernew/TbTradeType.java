package finley.gmair.model.ordernew;

import finley.gmair.model.EnumValue;

/**
 * @author zm
 * @date 2020/10/21 0021 11:52
 * @description 交易类型列表：同时查询多种交易类型可用逗号分隔。
 * 默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据
 **/
public enum TbTradeType implements EnumValue {
    /*一口价*/
    FIXED(0),
    /*拍卖*/
    AUCTION(1),
    /*一口价、拍卖*/
    GUARANTEE_TRADE(2),
    /*自动发货*/
    AUTO_DELIVERY(3),
    /*旺店入门版交易*/
    INDEPENDENT_SIMPLE_TRADE(4),
    /*旺店标准版交易*/
    INDEPENDENT_SHOP_TRADE(5),
    /*直冲*/
    EC(6),
    /*货到付款*/
    COD(7),
    /*分销*/
    FENXIAO(8),
    /*游戏装备*/
    GAME_EQUIPMENT(9),
    /*ShopEX交易*/
    SHOPEX_TRADE(10),
    /*万网交易*/
    NETCN_TRADE(11),
    /*统一外部交易*/
    EXTERNAL_TRADE(12),
    /*O2O交易*/
    O2O_OFFLINETRADE(13),
    /*万人团*/
    STEP(14),
    /*无付款订单*/
    NOPAID(15),
    /*预授权0元购机交易*/
    PRE_AUTH_TYPE(16);

    private int value;

    TbTradeType(int value) {
        this.value = value;
    }


    @Override
    public int getValue() {
        return 0;
    }
}
