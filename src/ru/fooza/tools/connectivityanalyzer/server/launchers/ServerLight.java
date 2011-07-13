package ru.fooza.tools.connectivityanalyzer.server.launchers;

import ru.fooza.tools.connectivityanalyzer.server.EchoServer;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 08.07.11
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public class ServerLight {
    public static void main(String[] args){
        try {
            EchoServer latencyTester = new EchoServer(InetAddress.getByName("192.168.12.9"));
            latencyTester.start();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
