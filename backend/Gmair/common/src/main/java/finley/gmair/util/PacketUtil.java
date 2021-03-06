package finley.gmair.util;

import finley.gmair.annotation.Command;
import finley.gmair.annotation.PacketConfig;
import finley.gmair.model.packet.*;

import java.lang.reflect.Field;
import java.util.Calendar;

public class PacketUtil {
    public static AbstractPacketV1 transferV1(byte[] source) {
        byte[] CTF = new byte[]{source[1]};
        byte[] CID = new byte[]{source[2]};
        byte[] UID = new byte[]{source[3], source[4], source[5], source[6], source[7], source[8], source[9], source[10], source[11], source[12], source[13], source[14]};
        byte[] LEN = new byte[]{source[15]};
        int length = ByteUtil.byte2int(new byte[]{source[15]});
        if(length <= 0){
            return null;
        }
        byte[] DAT = new byte[length];
        for (int i = 0; i < length; i++) {
            DAT[i] = source[15 + i + 1];
        }
        byte[] CRC = new byte[]{source[16 + length], source[17 + length]};
        HeartbeatPacketV1 packetV1 = new HeartbeatPacketV1(CTF,CID,UID,LEN,DAT,CRC);
        return packetV1;
    }

    public static <T> AbstractPacketV1 generateV1DetailProbe(Action action, String command, T input, String uid, Class clazz){
        AbstractPacketV1 packet = null;
        Field[] fields = clazz.getDeclaredFields();

        byte[] CTF = new byte[]{action.getSignal()};
        byte[] CID, UID, LEN;
        byte[] data = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Command.class)) {
                Command anno = field.getAnnotation(Command.class);
                String name = anno.name();
                if (! name.equals(command)) {
                    continue;
                }
                int length = anno.length();
                int cid = anno.id();

                if (input instanceof Integer) {
                    data = ByteUtil.int2byte((Integer)input, length);
                }else if (input instanceof Long) {
                    data = ByteUtil.long2byte((Long)input, length);
                }else if (input instanceof String) {
                    data = ByteUtil.string2byte((String) input, length);
                }
                CID = ByteUtil.int2byte(cid, 1);
                UID = ByteUtil.string2byte(uid, 12);
                LEN = ByteUtil.int2byte(length, 1);

                byte[] combine = ByteUtil.concat(CTF, CID, UID, LEN, data);
                int crcValue = CRC16.CRCCheck(combine);
                byte[] CRC = ByteUtil.int2byte(crcValue, 2);
                packet = new HeartbeatPacketV1(CTF, CID, UID, LEN, data, CRC);
                break;
            }
        }
        return packet;
    }


    public static AbstractPacketV2 transferV2(byte[] source) {
        byte[] CTF = new byte[]{source[1]};
        byte[] CID = new byte[]{source[2]};
        byte[] UID = new byte[]{source[3], source[4], source[5], source[6], source[7], source[8], source[9], source[10], source[11], source[12], source[13], source[14]};
        byte[] TIM = new byte[]{source[15], source[16], source[17], source[18], source[19], source[20], source[21], source[22]};
        byte[] LEN = new byte[]{source[23]};
//        System.out.println("CTF: " + CTF[0]);
//        System.out.println("CID: " + CID[0]);
//        System.out.println("UID: " + new String(UID).trim());
//        System.out.println("TIM: " + ByteUtil.byte2long(TIM));
//        System.out.println("LEN: " + ByteUtil.byte2int(LEN));


        if (source[23] == 0x00) {
            byte[] CRC = new byte[]{source[24], source[25]};
            HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
            packet.setCRC(CRC);
            //System.out.println("Wrap a heartbeat packet");
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
        //System.out.println("Wrap a probe packet");
        return packet;
    }

    public static HeartBeatPacket generateHeartBeat(String client) {
        byte[] CTF = new byte[]{0x02};
        byte[] CID = new byte[]{0x00};
        byte[] UID = ByteUtil.string2byte(client, 12);
        long time = System.currentTimeMillis();
        byte[] TIM = ByteUtil.long2byte(time, 8);
        byte[] LEN = new byte[]{0x00};
        HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
        return packet;
    }

    public static HeartBeatPacket generateProbe(String client) {
        byte[] CTF = new byte[]{0x00};
        byte[] CID = new byte[]{0x00};
        byte[] UID = ByteUtil.string2byte(client, 12);
        long time = System.currentTimeMillis();
        byte[] TIM = ByteUtil.long2byte(time, 8);
        byte[] LEN = new byte[]{0x00};
        HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
        return packet;
    }

    public static <T> AbstractPacketV2 generateDetailProbe(Action action, String command, T input, String client) {
        AbstractPacketV2 packet = null;
        Field[] fields = PacketInfo.class.getDeclaredFields();
        byte[] CTF = new byte[]{action.getSignal()};
        byte[] CID, UID, TIM, LEN;
        for (Field field : fields) {
            if (field.isAnnotationPresent(PacketConfig.class)) {
                PacketConfig pf = field.getAnnotation(PacketConfig.class);
                if (!command.equals(pf.name())) {
                    continue;
                }
                CID = ByteUtil.int2byte(pf.command(), 1);
                UID = ByteUtil.string2byte(client, 12);
                long time = System.currentTimeMillis();
                TIM = ByteUtil.long2byte(time, 8);
                if (action == Action.PROBE) {
                    LEN = new byte[]{0x00};
                    packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
                }
                if (action == Action.CONFIG) {
                    LEN = ByteUtil.int2byte(pf.length(), 1);
                    byte[] DAT = new byte[pf.length()];
                    if (input instanceof Integer) {
                        DAT = ByteUtil.int2byte((Integer) input, pf.length());
                    }
                    if (input instanceof Long) {
                        DAT = ByteUtil.long2byte((Long) input, pf.length());
                    }
                    if (input instanceof byte[]) {
                        DAT = (byte[]) input;
                    }
                    if (input instanceof String) {
                        DAT = ByteUtil.string2byte((String) input, pf.length());
                    }
                    packet = new ProbePacket(CTF, CID, UID, TIM, LEN, DAT);
                }
            }
        }
        return packet;
    }

    public static HeartBeatPacket generateReboot(String client) {
        byte[] CTF = new byte[]{0x01};
        byte[] CID = new byte[]{0x0B};
        byte[] UID = ByteUtil.string2byte(client, 12);
        long time = System.currentTimeMillis();
        byte[] TIM = ByteUtil.long2byte(time, 8);
        byte[] LEN = new byte[]{0x00};
        HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
        return packet;
    }

    public static HeartBeatPacket generateClassification(String client) {
        return generate(new byte[]{(byte)0xFD}, client);
    }

    public static HeartBeatPacket generateFirmware(String client) {
        return generate(new byte[]{(byte)0xFE}, client);
    }

    public static HeartBeatPacket generateHardware(String client) {
        return generate(new byte[]{(byte)0xFF}, client);
    }

    private static HeartBeatPacket generate(byte[] CID, String client) {
        byte[] CTF = new byte[]{0x00};
        byte[] UID = ByteUtil.string2byte(client, 12);
        long time = System.currentTimeMillis();
        byte[] TIM = ByteUtil.long2byte(time, 8);
        byte[] LEN = new byte[]{0x00};
        HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);
        return packet;
    }

    public static HeartbeatPacketV1 generateHeartbeatPacketV1(String uid){
        byte[] CTF = new byte[]{0x02};
        byte[] CID = new byte[]{0x00};
        byte[] UID = ByteUtil.string2byte(uid,12);
        byte[] LEN = new byte[]{0x20};
        byte[] DAT = currentTimeByte();
        HeartbeatPacketV1 heartbeatPacketV1 = new HeartbeatPacketV1(CTF,CID,UID,LEN,DAT);
        return heartbeatPacketV1;
    }

    public static byte[] currentTimeByte(){
        Calendar calendar = Calendar.getInstance();
        byte[] year = ByteUtil.int2byte(calendar.get(Calendar.YEAR) % 100, 1);
        byte[] month = ByteUtil.int2byte(calendar.get(Calendar.MONTH) + 1, 1);
        byte[] date = ByteUtil.int2byte(calendar.get(Calendar.DATE) , 1);
        byte[] hour = ByteUtil.int2byte(calendar.get(Calendar.HOUR_OF_DAY) , 1);
        byte[] minute = ByteUtil.int2byte(calendar.get(Calendar.MINUTE) , 1);
        byte[] second = ByteUtil.int2byte(calendar.get(Calendar.SECOND) , 1);
        byte[] reserve = new byte[26];

        byte[] result = ByteUtil.concat(year, month, date, hour, minute, second, reserve);
        return result;
    }
}
