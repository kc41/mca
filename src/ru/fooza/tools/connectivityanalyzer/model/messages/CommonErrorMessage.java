package ru.fooza.tools.connectivityanalyzer.model.messages;

import ru.fooza.tools.connectivityanalyzer.model.ClientId;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 20:01
 * To change this template use File | Settings | File Templates.
 */
public class CommonErrorMessage extends Message implements Serializable {
    public CommonErrorMessage(ClientId clientId, String errorCause){
        this.clientId = clientId;
        this.errorCause = errorCause;
    }

    @Override
    public String toString() {
        return "Cause = "+errorCause;
    }

    protected String errorCause;
}
