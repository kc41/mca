package ru.fooza.tools.connectivityanalyzer.server.StatisticsServer;

import ru.fooza.tools.connectivityanalyzer.model.ClientId;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class Registrar {
    public Registrar(){
        inMap = new HashMap<Socket,ClientId>();
        outputStreamMap = new HashMap<ClientId,ObjectOutputStream>();


    }
    public synchronized ClientId regUser(Socket socket) throws IOException{
        if (inMap.containsKey(socket)){
            return inMap.get(socket);
        }
        regCounter++;
        ClientId clientId = new ClientId(regCounter);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); //This can throw exception
        inMap.put(socket,clientId);
        outputStreamMap.put(clientId,oos);
        return clientId;

    }

    public synchronized void unregUser(Socket socket){
        if (inMap.containsKey(socket)){
            if (outputStreamMap.containsKey(inMap.get(socket)))
                outputStreamMap.remove(inMap.get(socket));
            inMap.remove(socket);
        }
    }


    public synchronized ObjectOutputStream getObjectOutputStream(ClientId clientId){
        if (outputStreamMap.containsKey(clientId))
            return outputStreamMap.get(clientId);
        return null;
    }

    public synchronized boolean isRegistred(ClientId clientId){
        if (inMap.containsKey(clientId)){
            return true;
        }
        else return false;
    }

    private Map<Socket,ClientId> inMap;
    private Map<ClientId,ObjectOutputStream> outputStreamMap;
    private long regCounter;


}
