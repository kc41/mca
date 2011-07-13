package ru.fooza.tools.connectivityanalyzer.server.StatisticsServer;

import ru.fooza.tools.connectivityanalyzer.model.ClientId;
import ru.fooza.tools.connectivityanalyzer.model.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandler extends Thread{

    public ConnectionHandler(Socket socket, Queue<Message> messageQueue,
                             Registrar registrar, int maxErrors) throws IOException{
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.registrar = registrar;
        this.maxErrors = maxErrors;
        ois = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run(){
        int errorCounter = 0;
        Object tempObject;
        Message tempMessage;
        while (true){
            try {
                tempObject = ois.readObject();
                if (Message.class.isInstance(tempObject)){
                    tempMessage = (Message)tempObject;
                    if (!clientRegistered){
                        myClientId = registrar.regUser(socket);
                    }
                    tempMessage.setClientId(myClientId);
                    synchronized (messageQueue){
                        messageQueue.add(tempMessage);
                        messageQueue.notifyAll();
                    }
                }
                else {
                    System.out.println("IO: Bad object recieved");
                    continue;
                }
            } catch (EOFException e){
                registrar.unregUser(socket);
                System.out.println("IO: Client "+myClientId+" gone offline");
                return;

            } catch (IOException e) {
                System.out.println("IO: Network error while message recieving");
                if (errorCounter <= maxErrors){
                    errorCounter++;
                }
                else{
                    registrar.unregUser(socket);
                    return;
                }
                continue;
            } catch (ClassNotFoundException e) {
                System.out.println("IO: Very bad object recieved");
                if (errorCounter <= maxErrors){
                    errorCounter++;
                }
                else{
                    registrar.unregUser(socket);
                    return;
                }
                continue;
            }
        }
    }


    protected Socket socket;
    protected Queue<Message> messageQueue;
    protected Registrar registrar;
    protected int maxErrors;

    protected ClientId myClientId;
    protected ObjectInputStream ois;
    boolean clientRegistered;


}
