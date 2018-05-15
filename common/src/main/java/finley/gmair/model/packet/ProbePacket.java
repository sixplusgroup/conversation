package finley.gmair.model.packet;

import finley.gmair.util.ByteUtil;

public class ProbePacket extends AbstractPacketV2 {

    protected byte[] DAT;

    public ProbePacket(byte[] FRH, byte[] CTF, byte[] CID, byte[] UID, byte[] TIM, byte[] LEN, byte[] DAT, byte[] CRC, byte[] FRT) {
        super(FRH, CTF, CID, UID, TIM, LEN, CRC, FRT);
        this.DAT = DAT;
    }

    @Override
    byte[] source() {
        return ByteUtil.concat(CTF, CID, UID, TIM, LEN, DAT);
    }
}
