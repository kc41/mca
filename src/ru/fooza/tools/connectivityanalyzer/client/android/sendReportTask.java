package ru.fooza.tools.connectivityanalyzer.client.android;

import android.os.AsyncTask;
import android.os.storage.OnObbStateChangeListener;
import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.model.messages.RegAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 14.07.11
 * Time: 8:44
 * To change this template use File | Settings | File Templates.
 */
public class sendReportTask extends AsyncTask<Void,Void,Void>{
    public sendReportTask(InetAddress serverAddress, int serverPort, Queue<Message> queue) {
        super();    //To change body of overridden methods use File | Settings | File Templates.
        try{
            Object tempObject;
            socket = new Socket(serverAddress,serverPort);
            socket.setSoTimeout(3000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Message());
            tempObject = ois.readObject();
            if (!RegAckMessage.class.isInstance(tempObject)){
                publishProgress();
                this.cancel(false);
            }
        }
        catch (SocketTimeoutException e){
            this.publishProgress();
            this.cancel(false);
        }
        catch (IOException e){
            this.publishProgress();
            this.cancel(false);
        }
        catch (ClassNotFoundException e){
            this.publishProgress();
            this.cancel(false);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        Message tempMessage;
        while (!queue.isEmpty()){
            synchronized (queue){
                tempMessage = queue.poll();
            }
            try{
                if (!socket.isClosed())
                    oos.writeObject(tempMessage);
                else
                    cancel(false);
                return null;
            }
            catch (IOException e){
                if (errCounter < errMax){
                    synchronized (queue){
                        queue.add(tempMessage);
                    }
                    errCounter++;
                    continue;
                }
                else{
                    this.publishProgress();
                    this.cancel(false);
                }
            }
            try {
                ois.readObject();
            }catch (SocketTimeoutException e){
                //TODO handle
            }catch (IOException e) {
                //TODO handle
            } catch (ClassNotFoundException e) {
                //TODO handle
            }


        }
        return null;
    }

    private int errMax;
    private int errCounter;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Queue<Message> queue;
}
