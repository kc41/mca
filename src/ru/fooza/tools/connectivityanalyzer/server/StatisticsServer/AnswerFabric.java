package ru.fooza.tools.connectivityanalyzer.server.StatisticsServer;

import ru.fooza.tools.connectivityanalyzer.model.messages.CommonErrorMessage;
import ru.fooza.tools.connectivityanalyzer.model.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: kchupin
 * Date: 12.07.11
 * Time: 20:18
 * To change this template use File | Settings | File Templates.
 */

public class AnswerFabric {
    public static Message getError(Message request,String cause){
        return new CommonErrorMessage(request.getClientId(),cause);
    }


}
