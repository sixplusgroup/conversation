package finley.gmair.model.openplatform;

import finley.gmair.model.Entity;

public class CorpProfile extends Entity {
    private String profileId;

    private String corpName;

    private String corpEmail;

    private String appid;

    public CorpProfile() {
        super();
    }

    public CorpProfile(String corpName, String corpEmail) {
        this();
        this.corpName = corpName;
        this.corpEmail = corpEmail;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpEmail() {
        return corpEmail;
    }

    public void setCorpEmail(String corpEmail) {
        this.corpEmail = corpEmail;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
