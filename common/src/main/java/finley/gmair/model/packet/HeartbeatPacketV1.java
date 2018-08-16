package finley.gmair.model.packet;

import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;
import finley.gmair.util.ResultData;

public class HeartbeatPacketV1 extends AbstractPacketV1{

    protected byte[] DAT;

    public HeartbeatPacketV1(byte[] CTF, byte[] CID, byte[] UID, byte[] LEN, byte[] DAT){
        super(CTF, CID, UID, LEN);
        this.LEN = ByteUtil.int2byte(0, 1);
        this.DAT = DAT;
        this.CRC = ByteUtil.int2byte(CRC16.CRCCheck(source()), 2);
    }

    public HeartbeatPacketV1(byte[] FRH, byte[] CTF, byte[] CID, byte[] UID, byte[] LEN, byte[] DAT, byte[] CRC, byte[] FRT) {
        super(FRH, CTF, CID, UID, LEN, CRC, FRT);
        this.DAT = DAT;
    }

    @Override
    byte[] source() {
        return ByteUtil.concat(CTF, CID, UID, LEN, DAT);
    }

}
