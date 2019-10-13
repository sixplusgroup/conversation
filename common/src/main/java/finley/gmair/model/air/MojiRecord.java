package finley.gmair.model.air;

/**
 * @ClassName: MojiRecord
 * @Description: TODO
 * @Author fan
 * @Date 2019/10/10 1:33 AM
 */
public class MojiRecord {
    private int fid;

    private String name;

    private String enName;

    private String pyName;

    private String province;

    public MojiRecord() {
    }

    public MojiRecord(int fid, String name, String enName, String pyName, String province) {
        this.fid = fid;
        this.name = name;
        this.enName = enName;
        this.pyName = pyName;
        this.province = province;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
