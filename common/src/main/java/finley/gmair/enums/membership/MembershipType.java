package finley.gmair.enums.membership;

public enum MembershipType {
    ORDINARY(0),
    SILVER(1);


    private Integer num;

    MembershipType(int num){
        this.num = num;
    }
    public Integer value(){
        return num;
    }

}
