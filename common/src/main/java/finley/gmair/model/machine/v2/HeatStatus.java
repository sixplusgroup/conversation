package finley.gmair.model.machine;

public enum HeatStatus {
    ON, OFF;

    private static HeatStatus status[] = new HeatStatus[]{ON, OFF};

    public HeatStatus fromValue(int value) {
        return status[value];
    }
}
