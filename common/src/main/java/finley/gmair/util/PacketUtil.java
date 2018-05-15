package finley.gmair.util;

import finley.gmair.model.packet.AbstractPacketV2;
import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.model.packet.ProbePacket;

public class PacketUtil {
    public static AbstractPacketV2 transferV2(byte[] source) {
        byte[] CTF = new byte[]{source[1]};
        byte[] CID = new byte[]{source[2]};
        byte[] UID = new byte[]{source[3], source[4], source[5], source[6], source[7], source[8], source[9], source[10], source[11], source[12], source[13], source[14]};
        byte[] TIM = new byte[]{source[15], source[16], source[17], source[18], source[19], source[20], source[21], source[22]};
        byte[] LEN = new byte[]{source[23]};
        if (source[23] == 0x00) {
            byte[] CRC = new byte[]{source[24], source[25]};
            HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
            packet.setCRC(CRC);
            return packet;
        }
        int length = ByteUtil.byte2int(new byte[]{source[23]});
        if (length <= 0)
            return null;
        byte[] DAT = new byte[length];
        for (int i = 0; i < length; i++) {
            DAT[i] = source[23 + i + 1];
        }
        byte[] CRC = new byte[]{source[24 + length], source[25 + length]};
        ProbePacket packet = new ProbePacket(CTF, CID, UID, TIM, LEN, DAT);
        packet.setCRC(CRC);
        return packet;
    }
}
