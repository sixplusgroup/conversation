package finley.gmair.enums.membership;
/***
 * @Description 未确认: 0, 已赋积分未确认: 1,已确认:2, 已关闭: 3
 * @Date  11/4/2021 2:19 PM
 */
public enum IntegralAddStatus {
    TODOCONFIRM(0), GIVED(1),CONFIRMED(2), CLOSED(3);

    private Integer num;
    IntegralAddStatus(Integer num){
        this.num = num;
    }
    public Integer value(){
        return num;
    }
}
