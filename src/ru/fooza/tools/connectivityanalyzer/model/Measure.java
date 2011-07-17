package ru.fooza.tools.connectivityanalyzer.model;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 15.07.11
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
public class Measure {

    public Measure(int packetSize, long delay, boolean damaged) {
        this.packetSize = packetSize;
        this.delay = delay;
        this.damaged = damaged;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isDamaged() {
        return damaged;
    }

    private int packetSize;
    private long delay;
    private boolean damaged;
}
