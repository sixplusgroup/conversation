package finley.gmair.model.drift;

import finley.gmair.model.EnumValue;

public enum ExpressStatus implements EnumValue {
    DELIVERED(0),BACk(1);

    private  int value;

    ExpressStatus(int value){this.value=value;}

    public static ExpressStatus valueOf(int value){
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
