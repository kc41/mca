package ru.fooza.tools.connectivityanalyzer.server;

import java.io.IOException;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 02.07.11
 * Time: 8:18
 * To change this template use File | Settings | File Templates.
 */
public class EchoServer extends Thread{
    public EchoServer(InetAddress localAddr) throws SocketException, IOException{
        Thread udpEcho = new Thread(new UdpEcho(localAddr));
        udpEcho.setName("UDP echo speedtest.server thread");
        udpEcho.start();
        //tcpSocket = new ServerSocket(5555,localAddr)
    }


    //private ServerSocket tcpSocket;


    private class UdpEcho implements Runnable {
        public UdpEcho(InetAddress localAddr) throws SocketException{
            udpSocket = new DatagramSocket(5555,localAddr);
        }

        public void run() {
            byte[] udpData = new byte[1024];
            DatagramPacket udpPacket= new DatagramPacket(udpData,udpData.length);
            while (true){
                try{
                    udpSocket.receive(udpPacket);
                    System.out.println("Packet Recieved form "+udpPacket.getPort()+":"+udpPacket.getAddress().toString());
                }
                catch (IOException e){
                    //Cleaning buffer in case of recieving error
                    for (int i=0 ; i<1024 ; i++){
                        udpData[i]=0;
                    }
                    continue;
                }
                if (udpData[1]!=0){
                    try {
                        udpSocket.send(new DatagramPacket(udpData,udpData.length,udpPacket.getAddress(),udpPacket.getPort()));
                    } catch (IOException e) {

                    }
                }
                //Cleaning buffer
                for (int i=0 ; i<1024 ; i++){
                    udpData[i]=0;
                }
            }



        }
        private DatagramSocket udpSocket;


    }
}

