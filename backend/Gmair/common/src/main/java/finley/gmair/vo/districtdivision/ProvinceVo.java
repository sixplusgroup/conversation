package finley.gmair.vo.districtdivision;

import java.sql.Timestamp;

public class ProvinceVo {
    private String provinceId;

    private String provinceName;

    private String provincePinyin;

    private boolean blockFlag;

    private Timestamp createAt;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvincePinyin() {
        return provincePinyin;
    }

    public void setProvincePinyin(String provincePinyin) {
        this.provincePinyin = provincePinyin;
    }

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}