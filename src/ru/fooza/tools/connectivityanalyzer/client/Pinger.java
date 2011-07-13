package ru.fooza.tools.connectivityanalyzer.client;

import ru.fooza.tools.connectivityanalyzer.model.oldshit.SingleStat;
import ru.fooza.tools.connectivityanalyzer.model.oldshit.TestResult;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 10.07.11
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */

//TODO Make an universal class

public class Pinger {
    public Pinger(){
        try {
            this.currentServer = InetAddress.getByName("192.168.77.77");
        } catch (UnknownHostException e) {
        }
    }
    public Pinger(InetAddress currentServer) {
        this.currentServer = currentServer;
    }

    public void setCurrentServer(InetAddress currentServer) {
        this.currentServer = currentServer;
    }

    public void ping(){

        try{
            udpSocket = new DatagramSocket(5556);
            udpSocket.setSoTimeout(1000);
        }catch (Exception e){
            System.out.println("Can't bind udp socket");
            return;
        }
        try{
            statSocket = new Socket(currentServer,3333);
        }
        catch (Exception e){
            System.out.println("Can't bind tcp socket");
        }
        boolean sendStat;
        ObjectOutputStream oos;
        try{
            oos = new ObjectOutputStream(statSocket.getOutputStream());
            sendStat = true;
        }catch (Exception e){
            System.out.println("Statistics won't be sent");
            sendStat = false;
            oos = null;
        }
        //Init of original data
        byte[] origin = new byte[1024];
        for (int j = 0 ; j < 1024 ; j++){
            origin[j]=(byte)(j);
        }
        //Array for recieved data
        byte[] udpResponse = new byte[1024];

        boolean succeed;

        //Init timers
        Date sendDate;
        Date recvDate;

        for (int i = 0 ; i < 5 ; i++){
            succeed = true;
            try{
                sendDate = new Date(System.currentTimeMillis());
                udpSocket.send(
                        new DatagramPacket(origin,origin.length,currentServer,5555));
                try{
                    udpSocket.receive(
                            new DatagramPacket(udpResponse,udpResponse.length,
                                    currentServer,5555));
                }
                catch (SocketTimeoutException e){
                    //Cleaning buffer in case of recieving error
                    for (int j = 0 ; j < 1024 ; j++){
                        udpResponse[i]=0;
                    }
                    System.out.println("Request timeout");
                    continue;
                }

                recvDate = new Date(System.currentTimeMillis());
                for (int j = 0 ; j < 1024 ; j++){
                    if (udpResponse[j] != origin[j])
                        succeed = false;
                }
                if (!succeed)
                    System.out.println("Bad packet");
                else{
                    System.out.println("Answer in "+(recvDate.getTime()-sendDate.getTime()));
                    if (sendStat){
                        oos.writeObject(new TestResult(recvDate.getTime()-sendDate.getTime()));

                    }
                }
                Thread.sleep(1000);
            }
            catch (Exception e){
                continue;
            }
        }
        if (udpSocket != null)
            udpSocket.close();
        if (statSocket != null){
          try{
            statSocket.close();
          }catch (Exception e){
              System.out.println("Already closed!");
          }
        }


    }
    public void getStat(){
        SingleStat temp;
        try{
            statSocket = new Socket(currentServer,3333);
            statSocket.setSoTimeout(10000);
        }
        catch (Exception e){
            System.out.println("Can't bind tcp socket");
        }
        try{
            ObjectOutputStream oos = new ObjectOutputStream(statSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(statSocket.getInputStream());
            oos.writeObject("<command>getSingleStat</command>");
            temp = (SingleStat)ois.readObject();
            System.out.println("Average delay = "+temp.getMiddleDelay() + "Reason = "+temp.getFailureReason());
            statSocket.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }



    }
    private InetAddress currentServer;
    private int echoPort;
    private int dataPort;
    protected Socket statSocket;
    protected DatagramSocket udpSocket;

}
