package finley.gmair.bean.enums;

public enum PayClientType {

    OFFICIALACCOUNT(0), // 公众号
    SHOPMP(1); // 商城小程序

    private final int value;

    PayClientType(int value){
        this.value =value;
    }

    public int getValue(){
        return this.value;
    }
}
