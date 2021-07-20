package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Company extends Entity {

    private String companyId;

    private String companyName;

    private String messageTitle;

    private String companyDetail;

    public Company(){
        super();
    }

    public Company(String companyName, String messageTitle, String companyDetail) {
        this();
        this.companyName = companyName;
        this.messageTitle = messageTitle;
        this.companyDetail = companyDetail;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getCompanyDetail() {
        return companyDetail;
    }

    public void setCompanyDetail(String companyDetail) {
        this.companyDetail = companyDetail;
    }
}
