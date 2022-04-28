package finley.gmair.model.packet;

import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;

public abstract class AbstractPacketV2 {
    protected byte[] FRH;

    protected byte[] CTF;

    protected byte[] CID;

    protected byte[] UID;

    protected byte[] TIM;

    protected byte[] LEN;

    protected byte[] CRC;

    protected byte[] FRT;

    public AbstractPacketV2(byte[] CTF, byte[] CID, byte[] UID, byte[] TIM, byte[] LEN) {
        super();

        this.FRH = new byte[]{(byte) 0xFF};
        this.FRT = new byte[]{(byte) 0xEE};

        this.CTF = CTF;
        this.CID = CID;
        this.UID = UID;
        this.TIM = TIM;
        this.LEN = LEN;
    }

    public AbstractPacketV2(byte[] FRH, byte[] CTF, byte[] CID, byte[] UID, byte[] TIM, byte[] LEN, byte[] CRC, byte[] FRT) {
        this(CTF, CID, UID, TIM, LEN);
        this.FRH = FRH;
        this.FRT = FRT;
        this.CRC = CRC;
    }

    public boolean isValid() {
        /* band v2 start with 0xFF and end with 0xEE */
        if (FRH[0] != (byte) 0xFF || FRT[0] != (byte) 0xEE) {
            return false;
        }
        byte[] expected = ByteUtil.int2byte(CRC16.CRCCheck(source()), 2);
        if (this.CRC.length != expected.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i ++) {
            if (this.CRC[i] != expected[i]) {
                return false;
            }
        }
        return true;
    }

    abstract byte[] source();

    public byte[] convert2bytearray() {
        return ByteUtil.concat(FRH, source(), CRC, FRT);
    }

    public void setCRC(byte[] CRC) {
        this.CRC = CRC;
    }

    public String getUID() {
        return new String(UID);
    }

    public long getTime() {
        return ByteUtil.byte2long(TIM);
    }
}
