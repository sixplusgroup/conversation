package finley.gmair.client;

import finley.gmair.handler.GMClientV1Handler;

public class GMClientV1 {

    public static void main(String[] args) {
        int port = 8888;
        //服务器host: 118.31.78.254     my host: 192.168.2.59
//        for(int i=0;i<10;i++) {
        String uid = "F0FE6B9BA794" ;
        GMClientV1Handler server = new GMClientV1Handler(null, port, uid,5000);
        new Thread(server, "server").start();
//        }
    }
}
