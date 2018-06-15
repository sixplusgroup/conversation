package finley.gmair.model.express;

import finley.gmair.model.Entity;

public class ExpressCompany extends Entity {

    private String companyId;

    private String companyName;

    private String companyUrl;

    private String companyCode;

    public ExpressCompany() {
        super();
    }

    public ExpressCompany(String companyName, String companyCode) {
        this();
        this.companyName = companyName;
        this.companyCode = companyCode;
    }

    public ExpressCompany(String companyName, String companyCode, String companyUrl) {
        this(companyName, companyCode);
        this.companyUrl = companyUrl;
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

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
