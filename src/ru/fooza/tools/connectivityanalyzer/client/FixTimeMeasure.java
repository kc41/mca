package ru.fooza.tools.connectivityanalyzer.client;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 11.07.11
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class FixTimeMeasure implements Runnable{

    public FixTimeMeasure(LocationManager newLocationManager, int newRequiredAccuracy){
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < requiredAccuracy){
                    fixTime = location.getTime();
                    lastLocation = location;
                    ready = true;
                    locationManager.removeUpdates(this);
                    timer.cancel();
                    timer.purge();
                    inform();
                }else
                    inform();
            }

            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            public void onProviderEnabled(String s) {
            }

            public void onProviderDisabled(String s) {
            }
        };
        listenerKiller = new TimerTask() {
            @Override
            public void run() {
                locationManager.removeUpdates(locationListener);
            }
        };
        this.requiredAccuracy = newRequiredAccuracy;
        this.locationManager = newLocationManager;
        timer = new Timer();
        ready = false;
    }
    public void run(){
        ready = false;
        startTime = System.currentTimeMillis();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        timer.schedule(listenerKiller,60000);
    }
    public abstract void inform();

    protected Timer timer;
    protected TimerTask listenerKiller;

    protected Location lastLocation;
    protected LocationListener locationListener;

    protected int requiredAccuracy;

    protected boolean ready;

    protected long fixTime;
    protected long startTime;

    protected LocationManager locationManager;
}
