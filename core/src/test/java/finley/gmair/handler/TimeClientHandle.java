package finley.gmair.handler;

import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.model.packet.ProbePacket;
import finley.gmair.util.ByteUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.Provider;
import java.util.Iterator;
import java.util.Set;


public class TimeClientHandle implements Runnable{

    private long sleepTime;         //设置板子向服务器发送报的间隔时间

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
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
                        }catch (Exception e) {
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
                    System.out.println("Now is : " + body);
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
        int flag = 0;

        //测试machine_status时,flag=0
        byte[] CTF = new byte[]{0x03};

        byte[] CID = new byte[]{0x00};

        byte[] UID = ByteUtil.string2byte("asdasd", 12);

        byte[] LEN = new byte[]{0x0D};

        byte[] data = new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};

        //测试0x0E(partial_pm2_5,滤网pm2.5)时,flag=1
        if(flag==1){
            CID = new byte[]{0x0E};
            LEN = new byte[]{0x01};
            data = new byte[]{0x01};
        }
        //测试0x0F(screen,警示灯)时,flag=2
        else if(flag==2){
            CID = new byte[]{0x0F};
            LEN = new byte[]{0x01};
            data = new byte[]{0x01};
        }

//        try {
//            Thread.sleep(5 * 1000);
//        } catch (InterruptedException e) {
//
//        }
        while(true) {
            long time = System.currentTimeMillis();
            byte[] TIM = ByteUtil.long2byte(time, 8);
            ProbePacket packet = new ProbePacket(CTF, CID, UID, TIM, LEN, data);
            byte[] req = packet.convert2bytearray();

            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            sc.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {
                System.out.println("send all......");
            }

            try{
                Thread.sleep(sleepTime);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
