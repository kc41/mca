package ru.fooza.tools.connectivityanalyzer.client;

import ru.fooza.tools.connectivityanalyzer.model.oldshit.TestResult;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 11.07.11
 * Time: 3:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicPinger {

    public TestResult singleUdpPing() throws IOException{
        byte[] udpResponse = new byte[512];
        DatagramPacket recievedPacket = new DatagramPacket(udpResponse,512);
        Date sendTime = new Date(System.currentTimeMillis()); //TODO Check for type mismatch
        DatagramPacket original = this.packetFactory(512);
        udpSocket.send(original); //What's about exception
        try {
            udpSocket.receive(recievedPacket);
        } catch (SocketTimeoutException e) {
            return null;
        }
        return null;
    }

    public abstract TestResult singleUdpPing(DatagramPacket packet);

    public abstract long singleTcpPing();

    public abstract List<TestResult> udpPing();

    public abstract List<TestResult> complexUdpPing();

    //Never try to make a longtime operations here
    public abstract void singleResultNotify();

    protected TestResult lastResult;
    protected Socket tcpSocket;
    protected DatagramSocket udpSocket;
    protected InetAddress server;
    protected int timeOut;
    protected boolean ready;
    protected int sourcePort;
    protected int currentSeqNo;

    public DatagramPacket packetFactory(int length){
        return null;
    }

    public final void init() throws SocketException{
        if (udpSocket != null && !udpSocket.isClosed()){
            udpSocket.close();
        }
        udpSocket = new DatagramSocket(sourcePort);
        udpSocket.setSoTimeout(timeOut);
        ready = true;
    }

    public final int getSeqNo(byte[] data, int length){
        if (length<2)
            return 0;
        return (((data[0]*128)+data[1])*128);


    }
}
