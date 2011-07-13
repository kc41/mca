package ru.fooza.tools.connectivityanalyzer.model.oldshit;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 08.07.11
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class NetworkData implements Serializable {
    public NetworkData(long latency) {
        this.latency = latency;
    }

    public long getLatency() {
        return latency;
    }

    private long latency;
}
