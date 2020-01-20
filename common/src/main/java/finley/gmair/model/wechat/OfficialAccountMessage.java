package finley.gmair.model.wechat;

import finley.gmair.model.EnumValue;

public enum  OfficialAccountMessage implements EnumValue{
    DELIVERED(0),RETURN(1);

    private  int value;

    OfficialAccountMessage(int value){this.value=value;}

    public static OfficialAccountMessage valueOf(int value){
        switch (value){
            case 0:
                return DELIVERED;
            case 1:
                return RETURN;
            default:
                return null;
        }
    }

    @Override
    public  int getValue(){return value;}
}
