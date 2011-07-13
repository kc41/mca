package ru.fooza.tools.connectivityanalyzer.server.statistics;

import ru.fooza.tools.connectivityanalyzer.model.messages.Message;

import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public interface MessageHandler {
    public Queue<Message> getRequestQueue();
}
