package finley.gmair.model.district;

import finley.gmair.model.Entity;

public class Province extends Entity {
    private String provinceId;

    private String provinceName;

    private String provincePinyin;

    public Province() {
        super();
    }

    public Province(String provinceId, String provinceName, String provincePinyin) {
        this();
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.provincePinyin = provincePinyin;
    }

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
}