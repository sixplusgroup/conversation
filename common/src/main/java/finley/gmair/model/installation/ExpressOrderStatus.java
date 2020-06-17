package finley.gmair.model.installation;

import finley.gmair.model.EnumValue;

public enum ExpressOrderStatus implements EnumValue {
    DELIVERED(0),BACk(1);

    private  int value;

    ExpressOrderStatus(int value){this.value=value;}

    ExpressOrderStatus(){this.value=0;}

    public static ExpressOrderStatus valueOf(int value){
        switch (value){
            case 0:
                return DELIVERED;
            case 1:
                return BACk;
            default:
                return null;
        }
    }

    @Override
    public  int getValue(){return value;}
}
