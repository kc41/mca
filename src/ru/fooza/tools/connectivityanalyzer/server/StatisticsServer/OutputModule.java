package ru.fooza.tools.connectivityanalyzer.server.StatisticsServer;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
public class OutputModule extends Thread{

    public OutputModule(Queue<Message> queue, Registrar registrar) {
        this.queue = queue;
        this.registrar = registrar;
        cache = new LinkedList<Message>();
        running = true;
    }

    @Override
    public void run() {
        Message tempMessage;
        ObjectOutputStream oos;
        System.out.println("Output module running OK");
        while (running){
            synchronized (queue){
                try {
                    //TODO Optimize caching
                    while (!queue.isEmpty()){
                        cache.add(queue.poll());
                    }
                    queue.wait();
                    System.out.println("IO: Sending response");
                    while (!queue.isEmpty()){
                        cache.add(queue.poll());
                    }


                } catch (InterruptedException e) {
                    running = false;
                    System.out.println("Sending thread was interrupted");
                    break;
                }
            }
            while (!cache.isEmpty()){
                //TODO Analyze errors
                tempMessage = cache.poll();
                oos = registrar.getObjectOutputStream(tempMessage.getClientId());
                try {
                    //TODO Add failure notifications
                    if(oos != null){
                        oos.writeObject(tempMessage);
                    }
                }
                catch (EOFException e){
                    if (registrar.isRegistred(tempMessage.getClientId())){
                        System.out.println("Client "+tempMessage.getClientId()+"gone offline");
                    }
                }
                catch (IOException e) {
                    System.out.println("Error while sending message to client "+tempMessage.getClientId());
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    protected Queue<Message> queue;
    protected Registrar registrar;

    protected boolean running;
    protected Queue<Message> cache;
}
