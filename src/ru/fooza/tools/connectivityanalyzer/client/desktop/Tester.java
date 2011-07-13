package ru.fooza.tools.connectivityanalyzer.client.desktop;

import ru.fooza.tools.connectivityanalyzer.model.messages.CommonErrorMessage;
import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StorageRecordMessage;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StorageAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class Tester {
    protected Socket s;

    public Tester(){
        try {
            s = new Socket(InetAddress.getByName("192.168.77.77"),3333);
            System.out.println("Binded...");
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(new Message());
            ois = new ObjectInputStream(s.getInputStream());
            Object temp = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public String test(){
        try {
            oos.writeObject(new StorageRecordMessage(new Timestamp(System.currentTimeMillis()),1000,30,"MTS",2435.534,2345.2345));
            Object temp = ois.readObject();
            if (CommonErrorMessage.class.isInstance(temp)){
                return ((CommonErrorMessage)temp).toString();
            }else if(StorageAckMessage.class.isInstance(temp)){
                return ("Statistics successfully recorded");

            }else return temp.getClass().toString();
        }catch (Exception e){
            return e.getMessage();
        }
    }
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

}
