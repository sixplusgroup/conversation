package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class MachinePm2_5 extends Entity{
    private String uid;
    private int index;
    private double pm2_5;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(double pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MachinePm2_5() {
    }

    public MachinePm2_5(String uid, int index, double pm2_5) {
        this.uid = uid;
        this.index = index;
        this.pm2_5 = pm2_5;
    }
}
