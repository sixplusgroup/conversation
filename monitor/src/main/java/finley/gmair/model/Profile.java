package finley.gmair.model;

/**
 * @author fan
 * @create_time 2019-2019/2/27 10:25 AM
 */
public class Profile extends Entity {
    private String profileId;

    private String qrcode;

    private String logoPath;

    private String qrcodePath;

    public Profile() {
        super();
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getQrcodePath() {
        return qrcodePath;
    }

    public void setQrcodePath(String qrcodePath) {
        this.qrcodePath = qrcodePath;
    }
}
