package ru.fooza.tools.connectivityanalyzer.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ru.fooza.R;
import ru.fooza.tools.connectivityanalyzer.model.oldshit.Response;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class NetworkAnalyzer extends Activity
{


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        queue = new LinkedList<Response>();

        setContentView(R.layout.main);
        Button launcher = (Button) findViewById(R.id.launchButton);
        Button setServer = (Button) findViewById(R.id.setServer);
        Button getStat = (Button) findViewById(R.id.getStat);
        serverAddressInput = (EditText) findViewById(R.id.servAddr);
        result = (TextView) findViewById(R.id.result);
        launcher.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                result.setText("Testing:");
                try {
                    new AsyncClient(result).execute(target);
                } catch (Exception e) {
                }
            }
        });
        setServer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                try{
                    target = InetAddress.getByName(serverAddressInput.getText().toString());
                }catch (UnknownHostException e){
                    result.setText("Bad address!!! Try again!!!");
                    try {
                        target = InetAddress.getByName("85.118.229.86");
                    }
                    catch (UnknownHostException e1) {}
                }
            }
        });
        getStat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new GetStat(result).execute(target);
            }

        });

    }
    protected InetAddress target;
    protected EditText serverAddressInput;
    protected Queue<Response> queue;
    protected TextView result;

}