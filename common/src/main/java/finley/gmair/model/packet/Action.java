package finley.gmair.model.packet;

public enum Action {
    PROBE(0, (byte) 0x00), CONFIG(1, (byte) 0x01), DATA(2, (byte) 0x02);

    int code;

    byte signal;

    Action(int code, byte signal) {
        this.code = code;
        this.signal = signal;
    }

    public byte getSignal() {
        return this.signal;
    }
}
