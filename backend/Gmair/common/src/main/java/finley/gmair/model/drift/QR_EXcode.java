package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class QR_EXcode extends Entity {
    private String Id;

    private String qrcode;

    private String excode;

    public QR_EXcode() {
        super();
    }

    public QR_EXcode(String qrcode, String excode) {
        this();
        this.qrcode = qrcode;
        this.excode = excode;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getExcode() {
        return excode;
    }

    public void setExcode(String excode) {
        this.excode = excode;
    }
}
