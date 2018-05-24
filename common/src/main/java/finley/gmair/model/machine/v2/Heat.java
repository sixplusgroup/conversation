package finley.gmair.model.machine.v2;

public class Heat {
    private HeatStatus status;

    private int watt;

    public Heat(int value) {
        if (value == 0) {
            status = HeatStatus.OFF;
            watt = 0;
        } else {
            status = HeatStatus.ON;
            
        }
    }
}
