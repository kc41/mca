package ru.fooza.tools.connectivityanalyzer.server.launchers;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StatSendMessage;
import ru.fooza.tools.connectivityanalyzer.server.StatisticsServer.IOmodule;
import ru.fooza.tools.connectivityanalyzer.server.StatisticsServer.Processing;
import ru.fooza.tools.connectivityanalyzer.server.StatisticsServer.Storage;

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

//TODO add registration, unified processing, XML
//TODO isolate and prioritize echoserver

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
            IOmodule io = new IOmodule(InetAddress.getByName(args[0]),3333,inboundQueue,outboundQueue,10);
            io.start();
            Processing processing = new Processing(inboundQueue,outboundQueue);
            Storage storageModule = new Storage(outboundQueue);
            processing.addHandler(StatSendMessage.class,storageModule.getRequestQueue());
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