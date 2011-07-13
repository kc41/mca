package ru.fooza.tools.connectivityanalyzer.client.android;

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
    private boolean mobileOperatorReady;
}
