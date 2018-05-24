package finley.gmair.model.machine;

public enum WorkMode {
    MANUAL, AUTO, CUSTOMIZE;

    private final static WorkMode[] modes = new WorkMode[]{MANUAL, AUTO, CUSTOMIZE};

    public WorkMode fromValue(int value) {
        return modes[value];
    }
}
