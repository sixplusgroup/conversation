package finley.gmair.model.packet;

import finley.gmair.annotation.AQIData;
import finley.gmair.model.machine.v1.MachineStatus;
import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;
import finley.gmair.util.MethodUtil;
import finley.gmair.util.ResultData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HeartbeatPacketV1 extends AbstractPacketV1{

    protected byte[] DAT;

    public HeartbeatPacketV1(byte[] CTF, byte[] CID, byte[] UID, byte[] LEN, byte[] DAT, byte[] CRC){
        super(CTF, CID, UID, LEN);
        this.LEN = LEN;
        this.DAT = DAT;
        this.CRC = CRC;
    }

    public HeartbeatPacketV1(byte[] FRH, byte[] CTF, byte[] CID, byte[] UID, byte[] LEN, byte[] DAT, byte[] CRC, byte[] FRT) {
        super(FRH, CTF, CID, UID, LEN, CRC, FRT);
        this.DAT = DAT;
    }

    @Override
    byte[] source() {
        return ByteUtil.concat(CTF, CID, UID, LEN, DAT);
    }

    public void calCRC(){
        int expectCRC = CRC16.CRCCheck(ByteUtil.concat(CTF, CID, UID, LEN, DAT));
        byte[] CRC = ByteUtil.int2byte(expectCRC, 2);
        this.CRC = CRC;
    }

    public void setDAT(byte[] DAT) {
        this.DAT = DAT;
    }

    public byte[] getDAT() {
        return DAT;
    }

}
