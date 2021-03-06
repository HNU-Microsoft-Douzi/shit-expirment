/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: GBNClient
 * Author:   Administrator
 * Date:     2019/5/6 0006 20:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 客户端
 */
public class GBNClient {
    private final int port = 80;
    private DatagramSocket datagramSocket = new DatagramSocket();
    private InetAddress inetAddress;
    private Model model;
    private int nextSeq = 1;
    private int base = 1;


    public GBNClient() throws Exception {
        model = new Model();
        Timer timer = new Timer(this, model);
        model.setTime(0);
        timer.start();
        while(true){
            //向服务器端发送数据
            sendData();
            //从服务器端接受ACK
            byte[] bytes = new byte[4096];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
            datagramSocket.receive(datagramPacket);
            String fromServer = new String(bytes, 0, bytes.length);
            int ack = Integer.parseInt(fromServer.substring(fromServer.indexOf("ack:")+4).trim());
            base = ack+1;
            if(base == nextSeq){
                //停止计时器
                model.setTime(0);
            }else {
                //开始计时器
                model.setTime(3);
            }
            System.out.println("从服务器获得的数据:" + fromServer);
            System.out.println("\n");
        }

    }

    public static void main(String[] args) throws Exception {
        GBNClient gbnClient = new GBNClient();

    }
    /**
     * 向服务器发送数据     【已测试】
     *
     * @throws Exception
     */
    private void sendData() throws Exception {
        inetAddress = InetAddress.getLocalHost();
        int n = 5;
        // 总共只发10个数据，如果不采用主动设置丢包的动作，那么服务端可以正确的收到所有的包
        // 如果发10个数据，采用主动设置3位置丢包，则服务端会丢弃当前包并回馈之前的得到的Ack，然后由客户端对窗口内的所有包重发
        while (nextSeq < base + n && nextSeq <= 10) {
            /// 不发编号为3的数据，模拟数据丢失
//            if(nextSeq == 3) {
//                nextSeq++;
//                continue;
//            }

            String clientData = "客户端发送的数据编号:" + nextSeq;
            System.out.println("向服务器发送的数据:"+nextSeq);

            byte[] data = clientData.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
            datagramSocket.send(datagramPacket);

            if(nextSeq == base){
                //开始计时
                model.setTime(3);
            }
            nextSeq++;
        }
    }

    /**
     * 超时数据重传
     */
    void timeOut() throws Exception {
        for(int i = base;i < nextSeq;i++){
            String clientData = "客户端重新发送的数据编号:" + i;
            System.out.println("向服务器重新发送的数据:" + i);
            byte[] data = clientData.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
            datagramSocket.send(datagramPacket);
        }
    }
}