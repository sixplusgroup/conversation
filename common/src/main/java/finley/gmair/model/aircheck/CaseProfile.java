package finley.gmair.model.aircheck;

import finley.gmair.model.Entity;

import java.sql.Date;

public class CaseProfile extends Entity {
    private String profileId;

    private String caseHolder;

    private String equipmentId;

    private int duration;

    private Date checkDate;

    private String cityId;

    private String location;

    private String trace;

    private boolean status;

    public CaseProfile() {
        super();
        this.status = false;
    }

    public CaseProfile(String caseHolder, String equipmentId, int duration, Date checkDate, String cityId, String location, String trace) {
        this();
        this.caseHolder = caseHolder;
        this.equipmentId = equipmentId;
        this.duration = duration;
        this.checkDate = checkDate;
        this.cityId = cityId;
        this.location = location;
        this.trace = trace;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getCaseHolder() {
        return caseHolder;
    }

    public void setCaseHolder(String caseHolder) {
        this.caseHolder = caseHolder;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
