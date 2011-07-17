package ru.fooza.tools.connectivityanalyzer.server.launchers;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StorageRecordMessage;
import ru.fooza.tools.connectivityanalyzer.server.EchoServer;
import ru.fooza.tools.connectivityanalyzer.server.statistics.IOmodule;
import ru.fooza.tools.connectivityanalyzer.server.statistics.Processing;
import ru.fooza.tools.connectivityanalyzer.server.statistics.Storage;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 08.07.11
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */

//TODO add XML

public class ServerFinal {
    public static void main(String[] args){
        if (args.length != 1){
            System.out.println("Usage: java -jar speedtest.server.jar xxx.xxx.xxx.xxx");
            System.exit(1);
        }
        //Init base infrastructure
        Queue<Message> inboundQueue = new LinkedList<Message>();
        Queue<Message> outboundQueue = new LinkedList<Message>();
        try{
            EchoServer echoServer = new EchoServer(InetAddress.getByName(args[0]));
            echoServer.start();
            IOmodule io = new IOmodule(InetAddress.getByName(args[0]),3333,inboundQueue,outboundQueue,10);
            io.start();
            Processing processing = new Processing(inboundQueue,outboundQueue);
            Storage storageModule = new Storage(outboundQueue);
            processing.addHandler(StorageRecordMessage.class,storageModule.getRequestQueue());
            storageModule.start();
            processing.start();




        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Something wrong!!! Should to restart!!!");
            System.exit(1);
        }


    }
}
