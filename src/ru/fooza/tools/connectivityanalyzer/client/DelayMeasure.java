package ru.fooza.tools.connectivityanalyzer.client;

import ru.fooza.tools.connectivityanalyzer.model.ByteOps;
import ru.fooza.tools.connectivityanalyzer.model.Measure;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 15.07.11
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class DelayMeasure {
    public DelayMeasure(InetAddress echoServer, int echoPort) {
        this.echoServer = echoServer;
        this.echoPort = echoPort;
        random = new Random();
    }

    public void init(int timeout){
        try{
            echoSocket = new DatagramSocket(echoPort);
            echoSocket.setSoTimeout(timeout);
            ready = true;
        }
        catch (SocketException e){
            ready = false;
        }
    }

    public Set<Measure> bulkTest(int num){
        Set<Measure> result = new HashSet<Measure>();
        for (int i = 0 ; i < num ; i++){
            result.add(testDelay(i , 1024));
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public Measure testDelay(int packetSize, int seqNo) {

        boolean damaged = true;
        int recvSeqNo = -1;


        //Generating packet
        DatagramPacket originPacket = this.generatePacket(packetSize,seqNo,echoServer,echoPort);
        //Creating container for response packet
        byte[] response = new byte[packetSize];
        DatagramPacket responsePacket = new DatagramPacket(response,packetSize);

        //Sending packet origin to server;
        try {
            echoSocket.send(originPacket);
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }

        long startTime = System.currentTimeMillis();
        long delay = -1;

        try{
            //In case of to slowly packets we wait for our packet
            while (recvSeqNo < seqNo){
                echoSocket.receive(responsePacket);
                recvSeqNo = ByteOps.getSeqNo(responsePacket.getData());
                if ((startTime - System.currentTimeMillis())>1000){
                    return (new Measure(packetSize,-1,true));
                }
                delay = startTime - System.currentTimeMillis();
            }
        }catch (SocketTimeoutException e){
            return new Measure(packetSize,-1,true);
        }catch (IOException e){
            return null;
        }

        for (int i = 0 ; i < packetSize ; i++ ){
            if (originPacket.getData()[i] != response[i]){
                damaged = true;
            }
        }

        return new Measure(packetSize,delay,damaged);

    }

    public DatagramPacket generatePacket(int length, int seqNo, InetAddress destination, int port){   //Generate random packet
        byte[] data;
        if (length < 10){
             data = new byte[10];
        }
        else {
            data = new byte[length];
        }
        random.nextBytes(data);
        //Setting seqNum;
        ByteOps.intToByte(seqNo, data, 0);
        //Setting packet length;
        ByteOps.intToByte(length, data, 4);

        return new DatagramPacket(data,data.length,destination,port);
    }

    public boolean isReady() {
        return ready;
    }

    protected boolean ready;
    protected InetAddress echoServer;
    protected int echoPort;
    protected DatagramSocket echoSocket;
    protected Random random;

}
