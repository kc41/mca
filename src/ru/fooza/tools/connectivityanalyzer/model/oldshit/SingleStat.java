package ru.fooza.tools.connectivityanalyzer.model.oldshit;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 08.07.11
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */
public class SingleStat implements Serializable{
    public SingleStat(long middleDelay, String failureReason) {
        this.middleDelay = middleDelay;
        this.failureReason = failureReason;
    }

    public long getMiddleDelay() {
        return middleDelay;
    }

    public String getFailureReason() {
        return failureReason;
    }

    private long middleDelay;
    private String failureReason;
}
