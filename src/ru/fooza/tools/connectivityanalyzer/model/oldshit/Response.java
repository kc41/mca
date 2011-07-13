package ru.fooza.tools.connectivityanalyzer.model.oldshit;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 07.07.11
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class Response {
    public Response(int seqNo, long latency) {
        SeqNo = seqNo;
        this.latency = latency;
    }
    @Override
    public String toString() {
        return ("Response in " + latency + " ms");
    }
    private int SeqNo;
    private long latency;
}
