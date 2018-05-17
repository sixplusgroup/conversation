package finley.gmair.model.packet;

import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;

public class ProbePacket extends AbstractPacketV2 {

    protected byte[] DAT;

    public ProbePacket(byte[] CTF, byte[] CID, byte[] UID, byte[] TIM, byte[] LEN, byte[] DAT) {
        super(CTF, CID, UID, TIM, LEN);
        this.DAT = DAT;
        this.CRC = ByteUtil.int2byte(CRC16.CRCCheck(source()), 2);
    }

    public ProbePacket(byte[] FRH, byte[] CTF, byte[] CID, byte[] UID, byte[] TIM, byte[] LEN, byte[] DAT, byte[] CRC, byte[] FRT) {
        super(FRH, CTF, CID, UID, TIM, LEN, CRC, FRT);
        this.DAT = DAT;
    }

    @Override
    byte[] source() {
        return ByteUtil.concat(CTF, CID, UID, TIM, LEN, DAT);
    }

    public byte[] getDAT() {
        return DAT;
    }

    public byte[] getCTF() {
        return this.CTF;
    }

    public byte[] getCID() {
        return this.CID;
    }
}
