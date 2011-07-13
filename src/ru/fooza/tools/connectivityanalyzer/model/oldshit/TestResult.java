package ru.fooza.tools.connectivityanalyzer.model.oldshit;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 08.07.11
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class TestResult implements Serializable{
    public TestResult(long latency) {
        networkData = new NetworkData(latency);
        testStartTime = new Timestamp(System.currentTimeMillis());
    }

    protected Timestamp testStartTime;
    protected Timestamp testEndTime;
    protected LocationData locationData;
    protected NetworkData networkData;
    public  String toSqlString(){
        return ("INSERT INTO latency.rawresults VALUES ('" +testStartTime.toString()+"',"
                +networkData.getLatency()+")");
    }
}
