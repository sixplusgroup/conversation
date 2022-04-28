package finley.gmair.model.consumer;

import finley.gmair.model.Entity;

public class Phone extends Entity{
    private String phoneId;

    private String number;

    private boolean preferred;

    public Phone() {
        super();
        this.preferred = false;
    }

    public Phone(String number) {
        this();
        this.number = number;
    }

    public Phone(String number, boolean preferred) {
        this(number);
        this.preferred = preferred;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}
