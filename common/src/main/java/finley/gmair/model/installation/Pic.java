package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Pic extends Entity {
    private String picId;
    private String picAddress;
    private String picMd5;
    private String memberPhone;
    private boolean copyFlag;
    public Pic(){super();}
    public Pic(String picAddress,String memberPhone){
        this();
        this.picAddress=picAddress;
        this.memberPhone=memberPhone;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    public String getPicMd5() {
        return picMd5;
    }

    public void setPicMd5(String picMd5) {
        this.picMd5 = picMd5;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public boolean isCopyFlag() {
        return copyFlag;
    }

    public void setCopyFlag(boolean copyFlag) {
        this.copyFlag = copyFlag;
    }
}
