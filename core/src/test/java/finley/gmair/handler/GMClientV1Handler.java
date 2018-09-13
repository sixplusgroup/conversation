package finley.gmair.handler;

import finley.gmair.model.packet.HeartbeatPacketV1;
import finley.gmair.model.packet.ProbePacket;
import finley.gmair.util.ByteUtil;
import finley.gmair.util.CRC16;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class GMClientV1Handler implements Runnable {

    private long sleepTime;         //设置板子向服务器发送报的间隔时间

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private String uid;

    private volatile boolean stop;

    public GMClientV1Handler(String host, int port, String uid) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        this.uid = uid;
        this.sleepTime = 10000;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException var1) {
            var1.printStackTrace();
            System.exit(1);
        }

        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception var2) {
                        var2.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }

                }
            } catch (Exception var1) {
                var1.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);

                    new Thread(() -> {
                        try {
                            doWrite(sc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    //doWrite(sc);
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);

                    String body = new String(bytes, "UTF-8");
                    System.out.println("Now is : " + body.trim());
                    String outBytes = ByteUtil.byte2Hex(bytes);
                    System.out.println("十六进制码流:" + outBytes);
                    System.out.println("FRH:" + outBytes.substring(0, 2));
                    System.out.println("CTF:" + outBytes.substring(2, 4));
                    System.out.println("CID:" + outBytes.substring(4, 6));
                    System.out.println("UID:" + outBytes.substring(6, 30));
                    System.out.println("LEN:" + outBytes.substring(30, 32));
                    int length = Integer.valueOf(outBytes.substring(30, 32), 16);
                    System.out.println("DAT:" + outBytes.substring(32, 32 + length * 2));
                    System.out.println("CRC:" + outBytes.substring(32 + length * 2, 32 + length * 2 + 4));
                    System.out.println("FRT:" + outBytes.substring(32 + length * 2 + 4, 32 + length * 2 + 6));
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                } else {

                }
            }
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        int flag = 1;

        byte[] CTF = new byte[]{0x02};
        byte[] CID = new byte[]{0x00};
        byte[] UID = ByteUtil.string2byte(this.uid, 12);
        //byte[] UID = new byte[]{-0x15,-0x34,0x11,0x11,0x11,0x22,0x00,0x00,0x22,0x00,0x00,0x07};
        byte[] LEN = new byte[]{0x20};
        byte[] DATA = new byte[]{0x00, 0x02, 0x33, 0x22, 0x01, 0x11, 0x11, 0x11, 0x00, 0x11, 0x01, 0x01, 0x01, 0x01};
        byte[] reserve = new byte[]{0x68, 0x01, 0x03, 0x11, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        int check = CRC16.CRCCheck(ByteUtil.concat(CTF, CID, UID, LEN, DATA, reserve));
        byte[] CRC = ByteUtil.int2byte(check, 2);

        while (true) {
            HeartbeatPacketV1 packet = new HeartbeatPacketV1(CTF, CID, UID, LEN, ByteUtil.concat(DATA, reserve), CRC);
            byte[] req = packet.convert2bytearray();

            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            sc.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {
                System.out.println("send all......");
            }

            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
