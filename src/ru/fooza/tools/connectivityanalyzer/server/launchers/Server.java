package ru.fooza.tools.connectivityanalyzer.server.launchers;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.server.StatisticsServer.IOmodule;

import java.lang.Exception;import java.lang.String;import java.lang.System;import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 02.07.11
 * Time: 8:17
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    public static void main(String[] args){
        Queue<Message> inboundQueue = new LinkedList<Message>();
        Queue<Message> outboundQueue = new LinkedList<Message>();
        try{
            IOmodule io = new IOmodule(InetAddress.getByName("192.168.12.28"),3333,inboundQueue,outboundQueue,10);
            io.start();

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Something wrong!!! Should to restart!!!");
            System.exit(1);
        }

    }
}
