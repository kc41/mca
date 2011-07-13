package ru.fooza.tools.connectivityanalyzer.server.statistics;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class IOmodule extends Thread{

    public IOmodule(InetAddress bindAddress,int bindPort,
                    Queue<Message> inboundQueue, Queue<Message> outboundQueue,
                    int maxErrorsPerSession) throws IOException{
        serverSocket = new ServerSocket(bindPort,0,bindAddress);
        this.inboundQueue = inboundQueue;
        this.outboundQueue = outboundQueue;
        this.maxErrorsPerSession = maxErrorsPerSession;
        registrar = new Registrar();
        outputModule = new OutputModule(this.outboundQueue,registrar);

    }

    @Override
    public void start() {
        super.start();
        outputModule.start();
    }

    @Override
    public void run(){
        Socket tempClientSocket;
        ConnectionHandler handler;
        System.out.println("Input module running OK");
        while (true){
            try {
                tempClientSocket = serverSocket.accept();
                System.out.println("IO: Connection accepted from "+tempClientSocket.toString());
                handler = new ConnectionHandler(tempClientSocket,inboundQueue,registrar,maxErrorsPerSession);
                handler.start();
            } catch (IOException e) {
                System.out.println("IO: Error in connection process");
            }
        }

    }


    protected Queue<Message> inboundQueue;
    protected Queue<Message> outboundQueue;
    protected int maxErrorsPerSession;

    protected OutputModule outputModule;
    protected ServerSocket serverSocket;
    protected Registrar registrar;


}
