package finley.gmair.model.packet;

import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;

public class HeartBeatPacket extends AbstractPacketV2 {
    public HeartBeatPacket(byte[] CTF, byte[] CID, byte[] UID, byte[] TIM, byte[] LEN) {
        super(CTF, CID, UID, TIM, LEN);
        this.LEN = ByteUtil.int2byte(0, 1);
        this.CRC = ByteUtil.int2byte(CRC16.CRCCheck(source()), 2);
    }

    @Override
    byte[] source() {
        return ByteUtil.concat(CTF, CID, UID, TIM, LEN);
    }
}
