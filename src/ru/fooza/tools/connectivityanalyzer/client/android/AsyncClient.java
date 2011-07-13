package ru.fooza.tools.connectivityanalyzer.client.android;

import android.os.AsyncTask;
import android.widget.TextView;
import ru.fooza.tools.connectivityanalyzer.model.oldshit.Response;
import ru.fooza.tools.connectivityanalyzer.model.oldshit.TestResult;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 02.07.11
 * Time: 8:17
 * To change this template use File | Settings | File Templates.
 */
public class AsyncClient extends AsyncTask<InetAddress,Response,Void>{
    public AsyncClient(TextView out) {
        this.out = out;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
        try{
            udpSocket = new DatagramSocket(5556);
            udpSocket.setSoTimeout(1000);

        }catch (Exception e){
            out.setText(out.getText()+"\nCan't bind socket\n"+e.getMessage());
            this.cancel(canceled);
            return;
        }
        try{
            statSocket = new Socket(InetAddress.getByName("192.168.12.9"),3333);
        }
        catch (Exception e){
            out.setText(out.getText()+"\nNo stats!!!\n"+e.getMessage());
        }
    }
    @Override
    protected Void doInBackground(InetAddress... inetAddresses) {
        boolean sendStat;
        ObjectOutputStream oos;
        try{
            oos = new ObjectOutputStream(statSocket.getOutputStream());
            sendStat = true;
        }catch (Exception e){
//            out.setText(out.getText()+"\nStat won't be sent\n");
            sendStat = false;
            oos = null;
        }
        //Init of original data
        byte[] origin = new byte[1024];
        for (int j = 0 ; j < 1024 ; j++){
            origin[j]=(byte)(j);
        }
        //Array for recieved data
        byte[] udpResponse = new byte[1024];

        boolean succeed;

        //Init timers
        Date sendDate;
        Date recvDate;
        if (canceled){
            return null;
        }
        for (int i = 0 ; i < 5 ; i++){
            succeed = true;
            try{
                sendDate = new Date(System.currentTimeMillis());
                udpSocket.send(
                        new DatagramPacket(origin,origin.length,inetAddresses[0],5555));
                try{
                    udpSocket.receive(
                            new DatagramPacket(udpResponse,udpResponse.length,
                                    inetAddresses[0],5555));
                }
                catch (SocketTimeoutException e){
                    //Cleaning buffer in case of recieving error
                    for (int j = 0 ; j < 1024 ; j++){
                        udpResponse[i]=0;
                    }
                    publishProgress(new Response(i,-1));
                    continue;
                }
                recvDate = new Date(System.currentTimeMillis());
                for (int j = 0 ; j < 1024 ; j++){
                    if (udpResponse[j] != origin[j])
                        succeed = false;
                }
                if (!succeed)
                    publishProgress(new Response(i,-1));
                else{
                    publishProgress(new Response(i,recvDate.getTime()-sendDate.getTime()));
                    if (sendStat){
                        oos.writeObject(new TestResult(recvDate.getTime()-sendDate.getTime()));

                    }
                }
                Thread.sleep(1000);
            }
            catch (Exception e){
                continue;
            }
        }
        if (udpSocket != null)
            udpSocket.close();

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Override
    protected void onProgressUpdate(Response... values) {
        out.setText(out.getText()+"\n"+values[0].toString());
    }
    protected void onPostExecute(){
        if (udpSocket != null)
            udpSocket.close();
        if (statSocket != null)
            try{
            statSocket.close();
            }catch (IOException e){

            }
    }

    protected Socket statSocket;
    protected DatagramSocket udpSocket;
    protected TextView out;
    boolean canceled;
}
