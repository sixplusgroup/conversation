package finley.gmair.model.auth;

import finley.gmair.model.Entity;
import finley.gmair.util.SerialUtil;

import java.io.Serializable;

public class VerificationCode extends Entity implements Serializable {
    private static final long serialVersionUID = -3757938937186622059L;
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
