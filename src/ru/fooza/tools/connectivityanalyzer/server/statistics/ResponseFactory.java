package ru.fooza.tools.connectivityanalyzer.server.statistics;

import ru.fooza.tools.connectivityanalyzer.model.messages.CommonErrorMessage;
import ru.fooza.tools.connectivityanalyzer.model.messages.Message;
import ru.fooza.tools.connectivityanalyzer.model.messages.storage.StorageAckMessage;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 20:18
 * To change this template use File | Settings | File Templates.
 */

public class ResponseFactory {
    public static Message getError(Message request,String cause){
        return new CommonErrorMessage(request.getClientId(),cause);
    }
    public static Message getStorageAckMessage(Message request){
        return new StorageAckMessage(request.getClientId());
    }



}
