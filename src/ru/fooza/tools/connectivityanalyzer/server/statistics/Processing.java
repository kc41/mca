package ru.fooza.tools.connectivityanalyzer.server.statistics;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
public class Processing extends Thread{
    public Processing(Queue<Message> inboundQueue, Queue<Message> outboundQueue) {
        this.inboundQueue = inboundQueue;
        this.outboundQueue = outboundQueue;
        running = true;
        handlerMap = new HashMap<Class,Queue<Message>>();
    }

    public synchronized void addHandler(Class handledClass,Queue<Message> queue){
        handlerMap.put(handledClass,queue);
    }

    @Override
    public void run() {
        Message currentMessage;

        System.out.println("Message processing running OK");
        while (running){
            synchronized (inboundQueue){
                if (inboundQueue.isEmpty()){
                    try {
                        inboundQueue.wait();
                    } catch (InterruptedException e) {
                        running = false;
                        System.out.println("Processing thread was interrupted");
                        continue;
                    }
                }
                currentMessage = inboundQueue.poll();
                for (Class i : handlerMap.keySet()){
                    if (i.isInstance(currentMessage)) {
                        synchronized (handlerMap.get(i)){
                            System.out.println("Processing: Message sent to handler");
                            handlerMap.get(i).add(currentMessage);
                            handlerMap.get(i).notifyAll();
                            currentMessage = null;
                        }
                    }
                }
                if (currentMessage != null){
                    System.out.println("Processing: Service not defined");
                    synchronized (outboundQueue){
                        outboundQueue.add(AnswerFabric.getError(currentMessage,"Requested service not available"));
                        outboundQueue.notifyAll();
                    }
                }
            }
        }
    }

    protected Queue<Message> inboundQueue;
    protected Queue<Message> outboundQueue;
    protected Map<Class,Queue<Message>> handlerMap;


    protected boolean running;
}
