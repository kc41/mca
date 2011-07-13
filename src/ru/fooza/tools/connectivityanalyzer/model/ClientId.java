package ru.fooza.tools.connectivityanalyzer.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class ClientId implements Serializable{
    public ClientId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "number "+id;
    }

    private long id;
}
