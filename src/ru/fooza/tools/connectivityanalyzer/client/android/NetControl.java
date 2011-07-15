package ru.fooza.tools.connectivityanalyzer.client.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 14.07.11
 * Time: 1:30
 * To change this template use File | Settings | File Templates.
 */
public class NetControl {
    public NetControl(Context context){
        telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isNetReadyForTest(){
        return true;
    }

    public String getOperatorName(){
        if (mobileOperatorReady){
            return telephonyManager.getNetworkOperator();
        }else
            return null;
    }

    private TelephonyManager telephonyManager;
    private ConnectivityManager connectivityManager;
    private boolean mobileOperatorReady;
}
