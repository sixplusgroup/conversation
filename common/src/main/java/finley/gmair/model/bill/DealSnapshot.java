package finley.gmair.model.bill;

import finley.gmair.model.Entity;

public class DealSnapshot extends Entity {

    private String dealSnapshotId;

    private String dealAccount;

    private SnapshotStatus status;

    private String billId;

    private String channelId;

    private String channelName;

    public DealSnapshot() {
        super();
        this.status = SnapshotStatus.UNPAYED;
    }

    public  DealSnapshot(String dealSnapshotId, String dealAccount, SnapshotStatus status,String billId,String channelId,String channelName){
        this();
        this.dealAccount=dealAccount;
        this.dealSnapshotId=dealSnapshotId;
        this.status=status;
        this.billId=billId;
        this.channelId=channelId;
        this.channelName=channelName;
    }


    public String getDealAccount() {
        return dealAccount;
    }

    public void setDealAccount(String dealAccount) {
        this.dealAccount = dealAccount;
    }

    public SnapshotStatus getStatus() {
        return status;
    }

    public void setStatus(SnapshotStatus status) {
        this.status = status;
    }

    public String getDealSnapshotId() {
        return dealSnapshotId;
    }

    public void setDealSnapshotId(String dealSnapshotId) {
        this.dealSnapshotId = dealSnapshotId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
