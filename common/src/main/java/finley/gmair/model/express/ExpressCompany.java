package finley.gmair.model.express;

import finley.gmair.model.Entity;

public class ExpressCompany extends Entity {

    private String companyId;

    private String companyName;

    public ExpressCompany() {
        super();
    }

    public ExpressCompany(String companyName) {
        this();
        this.companyName = companyName;
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
}
