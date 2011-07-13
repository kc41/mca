package ru.fooza.tools.connectivityanalyzer.server.StatisticsServer;


import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StatSendMessage;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StorageAckMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class Storage extends Thread implements MessageHandler{

    //TODO Add getAvgDelay

    public Storage(Queue<Message> outputQueue) throws SQLException{
        requestQueue = new LinkedList<Message>();   //Queue of users requests, for Processing
        writeQueue = new LinkedList<Message>();     //Queue of messages to write to DB
        readQueue = new LinkedList<Message>();      //Queue of messages to read from DB
        this.outputQueue = outputQueue;
        db = DriverManager.getConnection("jdbc:postgresql://192.168.77.77/kosnetdb","kchupin","qwdcesazx");
        cacheSize = 1;
    }

    public Queue<Message> getRequestQueue() {
        return requestQueue;
    }

    @Override
    public void start() {
        super.start();
        WriteDatabaseThread writeDatabaseThread = new WriteDatabaseThread();
        writeDatabaseThread.start();
    }

    public void run(){
        System.out.println("Storing thread running OK");
        Message currentMessages;
        while (true){
            synchronized (requestQueue) {
                if (requestQueue.isEmpty()){
                    try {
                        requestQueue.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Storing thread was interrupted");
                        continue;
                    }
                }
                currentMessages = requestQueue.poll();
                //TODO: Switch read/write requests;
                if (StatSendMessage.class.isInstance(currentMessages)){
                    synchronized (writeQueue){
                        System.out.println("Storage: Message sent to DB");
                        writeQueue.add(currentMessages);
                        writeQueue.notifyAll();
                    }
                }else {
                    System.out.println("Storage: Wrong message sent to storage");
                }
            }
        }
    }


    protected void store(Queue<Message> task){
        while (!task.isEmpty()){
            try {
                Statement st = db.createStatement();
                for (Message i : task){
                    try {
                        st.executeUpdate( getSqlRequest( (StatSendMessage)i ) );
                    } catch (SQLException e) {
                        e.printStackTrace();  //TODO Add full error handling
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();  //TODO Add full error handling
            }
            finally {
                task.clear();
            }
        }
    }

    protected void store(StatSendMessage message) throws SQLException{
        try {
            Statement st = db.createStatement();
            st.executeQuery( getSqlRequest(message));
        }catch (SQLException e){
            throw e;
        }
    }

    //TODO Replace with ORM;
    protected String getSqlRequest(StatSendMessage message){
        String request;
        request = "INSERT INTO latency.delays (time,packetSize,delay,operator,lat,long) VALUES ("+
                "'"+message.getTimestamp().toString()+"',"+
                message.getPacketSize()+","+message.getRoundTripDelay()+","+
                "'"+message.getOperator()+"',"+message.getLatitude()+","+message.getLongitude()+");";

        return request;
    }

    protected Queue<Message> requestQueue;
    protected Queue<Message> writeQueue;
    protected Queue<Message> readQueue;
    protected Queue<Message> outputQueue;

    protected int cacheSize;
    protected Connection db;

    //
    //Thread fo handling users 'write to DB' request
    //
    private class WriteDatabaseThread extends Thread{
        private WriteDatabaseThread() {
            cache = new LinkedList<Message>();
        }

        @Override
        public void run(){
            Message currentMessage;
            while (true){
                synchronized (writeQueue){
                    if (writeQueue.isEmpty()){
                        try {
                            writeQueue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("DB writing thread was interrupted");
                            continue;
                        }
                    }
                    currentMessage = writeQueue.poll();
                }
                try{
                    store((StatSendMessage)currentMessage);
                    synchronized (outputQueue){
                        outputQueue.add(AnswerFabric.getStorageAckMessage(currentMessage));
                        outputQueue.notifyAll();
                    }
                    System.out.println("Storage: data stored");
                }catch (SQLException e){
                    synchronized (outputQueue){
                        outputQueue.add(AnswerFabric.getStorageAckMessage(currentMessage));
                        outputQueue.notifyAll();
                        System.out.println("Storage: data rejected; cause = "+e.getMessage());
                    }
                }
            }
        }
        protected Queue<Message> cache;
    }

    //TODO Add reading queue;




}
