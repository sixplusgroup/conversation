package finley.gmair.model.formaldehyde;

import finley.gmair.model.Entity;

import java.util.Date;

public class CaseProfile extends Entity {
    private String caseId;
    private String caseHolder;
    private String equipmentId;
    private String checkDuration;
    private Date checkDate;
    private String caseCityId;
    private String caseCityName;
    private String caseLocation;
    private String checkTrace;
    private CaseStatus caseStatus;
    private String videoId;

    public CaseProfile() {
        super();
    }

    public CaseProfile(String caseHolder, String equipmentId, String checkDuration, Date checkDate, String caseCityId, String caseCityName, String caseLocation, String checkTrace, CaseStatus caseStatus, String videoId) {
        super();
        this.caseHolder = caseHolder;
        this.equipmentId = equipmentId;
        this.checkDuration = checkDuration;
        this.checkDate = checkDate;
        this.caseCityId = caseCityId;
        this.caseCityName = caseCityName;
        this.caseLocation = caseLocation;
        this.checkTrace = checkTrace;
        this.caseStatus = caseStatus;
        this.videoId = videoId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
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

    public String getCheckDuration() {
        return checkDuration;
    }

    public void setCheckDuration(String checkDuration) {
        this.checkDuration = checkDuration;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCaseCityId() {
        return caseCityId;
    }

    public void setCaseCityId(String caseCityId) {
        this.caseCityId = caseCityId;
    }

    public String getCaseCityName() {
        return caseCityName;
    }

    public void setCaseCityName(String caseCityName) {
        this.caseCityName = caseCityName;
    }

    public String getCaseLocation() {
        return caseLocation;
    }

    public void setCaseLocation(String caseLocation) {
        this.caseLocation = caseLocation;
    }

    public String getCheckTrace() {
        return checkTrace;
    }

    public void setCheckTrace(String checkTrace) {
        this.checkTrace = checkTrace;
    }

    public CaseStatus getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(CaseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
