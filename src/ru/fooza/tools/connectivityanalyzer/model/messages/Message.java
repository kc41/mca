package ru.fooza.tools.connectivityanalyzer.model.messages;

import ru.fooza.tools.connectivityanalyzer.model.ClientId;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Serializable{


    public ClientId getClientId() {
        return clientId;
    }

    public void setClientId(ClientId clientId) {
        this.clientId = clientId;
    }

    protected ClientId clientId;
}
