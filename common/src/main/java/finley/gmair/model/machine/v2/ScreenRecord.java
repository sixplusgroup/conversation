package finley.gmair.model.machine.v2;

import finley.gmair.model.Entity;

import java.util.Date;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/19
 */
public class ScreenRecord extends Entity {
    private String recordId;

    private String codeValue;

    private String refreshDate;

    public ScreenRecord() {
        super();
    }

    public ScreenRecord(String codeValue, String refreshDate) {
        this();
        this.codeValue = codeValue;
        this.refreshDate = refreshDate;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(String refreshDate) {
        this.refreshDate = refreshDate;
    }
}