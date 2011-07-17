package ru.fooza.tools.connectivityanalyzer.client.desktop;

import android.provider.Settings;
import ru.fooza.tools.connectivityanalyzer.client.DelayMeasure;
import ru.fooza.tools.connectivityanalyzer.client.Pinger;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 10.07.11
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public class DesktopClient {
    public static void main(String[] arg){
        String currentCommand;
        String currentCommandString;
        StringTokenizer tokenizer;
        Scanner a = new Scanner(System.in);
        System.out.println("KosNet desktop client v0.0.1b");
        DelayMeasure pinger;
        try {
            pinger = new DelayMeasure(InetAddress.getByName("192.168.77.77"),5555);
        }catch (UnknownHostException e){
            pinger = null;
        }
        Tester tester = new Tester();
        while (true){
            System.out.print('>');
            currentCommandString =  a.next();
            tokenizer = new StringTokenizer(currentCommandString);
            if (tokenizer.hasMoreTokens()){
                currentCommand = tokenizer.nextToken();
            }
            else continue;

            if (currentCommand.equals("exit")){
                System.exit(1);
            }
            else if (currentCommand.equals("ping")){
                if (tokenizer.hasMoreTokens()){
                    try{
                        //pinger.setCurrentServer(InetAddress.getByName(tokenizer.nextToken()));
                        //pinger.ping();
                    }
                    catch (Exception e){
                        System.out.println("Wrong address");
                        continue;
                    }
                }
                else {
                    pinger.testDelay(1024,10);
                }
            }
            else if (currentCommand.equals("getstat")){
                //pinger.getStat();
            }
            else if (currentCommand.equals("test")){
                System.out.println(tester.test());
            }
            else{
                System.out.println("You typed " + currentCommand);
            }
        }
    }

}
