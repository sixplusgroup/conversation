package finley.gmair.model.auth;

import finley.gmair.model.Entity;
import finley.gmair.util.SerialUtil;

public class VerificationCode extends Entity {
    private String phone;

    private String serial;

    public VerificationCode(String phone) {
        this.phone = phone;
        this.serial = SerialUtil.serial();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
