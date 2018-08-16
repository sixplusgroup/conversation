package finley.gmair.model.packet;

import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;

public abstract class AbstractPacketV1 {
    protected byte[] FRH;

    protected byte[] CTF;

    protected byte[] CID;

    protected byte[] UID;

    protected byte[] LEN;

    protected byte[] CRC;

    protected byte[] FRT;

    public AbstractPacketV1(byte[] CTF, byte[] CID, byte[] UID, byte[] LEN) {

        this.FRH = new byte[]{(byte) 0xEF};
        this.FRT = new byte[]{(byte) 0xEE};

        this.CTF = CTF;
        this.CID = CID;
        this.UID = UID;
        this.LEN = LEN;
    }

    public AbstractPacketV1(byte[] FRH, byte[] CTF, byte[] CID, byte[] UID, byte[] LEN, byte[] CRC, byte[] FRT) {
        this(CTF, CID, UID, LEN);
        this.FRH = FRH;
        this.FRT = FRT;
        this.CRC = CRC;
    }

    public boolean isValid() {
        /* band v1 start with 0xEF and end with 0xEE */
        if (FRH[0] != (byte) 0xEF || FRT[0] != (byte) 0xEE) {
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

}



