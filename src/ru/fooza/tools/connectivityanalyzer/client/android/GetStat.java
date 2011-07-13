package ru.fooza.tools.connectivityanalyzer.client.android;

import android.os.AsyncTask;
import android.widget.TextView;
import ru.fooza.tools.connectivityanalyzer.model.oldshit.SingleStat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 08.07.11
 * Time: 23:28
 * To change this template use File | Settings | File Templates.
 */
public class GetStat extends AsyncTask<InetAddress,SingleStat,Void>{
    protected Socket socket;
    protected TextView tv;

    public GetStat(TextView tv) {
        this.tv = tv;
    }

    @Override
    protected Void doInBackground(InetAddress... inetAddresses) {
        ObjectOutputStream oos;
        ObjectInputStream ois;
        Object tempObject;

        try {
            socket = new Socket(inetAddresses[0],3333);
            socket.setSoTimeout(1200);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("<command>getstat</command>");
            tempObject = ois.readObject();
            if (SingleStat.class.isInstance(tempObject)){
                publishProgress((SingleStat)tempObject);
            }

        } catch (SocketTimeoutException e){
            publishProgress(new SingleStat(-1, "Server unreacheble: "+e.getMessage() ));
            return null;

        } catch (IOException e) {
            publishProgress(new SingleStat(-1, "Server unreacheble: "+e.getMessage() ));
            socket = null;
            oos = null;
            return null;

        } catch (ClassNotFoundException e){
            publishProgress(new SingleStat(-1, "Server is crazy: "+e.getMessage() ));
            return null;
        }
        finally {
            try {
                socket.close();
            } catch (Exception e) {}
            return null;
        }

    }
    @Override
    protected void onProgressUpdate(SingleStat... values) {
        if (values[1].getMiddleDelay()==-1){
            tv.setText("Failure to get stat\nReason: "+values[1].getFailureReason());
        }else
            tv.setText("Middle delay: "+values[1].getMiddleDelay()+"ms\n");
    }
}
