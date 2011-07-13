package ru.fooza.tools.connectivityanalyzer.model.messages.storage;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 22:43
 * To change this template use File | Settings | File Templates.
 */
public class StorageRecordMessage extends StorageMessages implements Serializable{
    public StorageRecordMessage(Timestamp timestamp, int packetSize, long roundTripDelay, String operator, double latitude, double longitude) {
        this.timestamp = timestamp;
        this.packetSize = packetSize;
        this.roundTripDelay = roundTripDelay;
        this.operator = operator;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public long getRoundTripDelay() {
        return roundTripDelay;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getOperator() {
        return operator;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    protected Timestamp timestamp;
    protected int packetSize;
    protected long roundTripDelay;
    protected String operator;
    protected double latitude;
    protected double longitude;
}
