package finley.gmair.model.drift;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/26 15:32
 * @description ：object to sync from ordercentre to drift
 */

public class DriftOrderExpress {
    private DriftOrder driftOrder;
    private DriftExpress driftExpress;

    public DriftOrderExpress() {
    }

    public DriftOrderExpress(DriftOrder driftOrder, DriftExpress driftExpress) {
        this.driftOrder = driftOrder;
        this.driftExpress = driftExpress;
    }

    public DriftOrder getDriftOrder() {
        return driftOrder;
    }

    public void setDriftOrder(DriftOrder driftOrder) {
        this.driftOrder = driftOrder;
    }

    public DriftExpress getDriftExpress() {
        return driftExpress;
    }

    public void setDriftExpress(DriftExpress driftExpress) {
        this.driftExpress = driftExpress;
    }
}
