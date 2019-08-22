package finley.gmair.model.drift;

import finley.gmair.model.Entity;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 13:55 2019/8/22
 */
public class VerifyInfo extends Entity {

    private String verifyId;

    private String idCard;

    private String name;

    public VerifyInfo() {
        super();
    }

    public String getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(String verifyId) {
        this.verifyId = verifyId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
