package ru.scorpio92.socketchat.wsserver.api;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ru.scorpio92.socketchat.wsserver.data.model.message.base.BaseMessage;
import ru.scorpio92.socketchat.wsserver.data.model.message.base.ErrorCode;
import ru.scorpio92.socketchat.wsserver.data.model.message.base.ErrorMessage;
import ru.scorpio92.socketchat.wsserver.domain.AuthorizeUseCase;
import ru.scorpio92.socketchat.wsserver.domain.IUseCase;
import ru.scorpio92.socketchat.wsserver.tools.JsonWorker;
import ru.scorpio92.socketchat.wsserver.tools.Logger;

@ServerEndpoint(value = "/")
public class PublicEndpoint {

    @OnMessage
    public String onMessage(String message, Session session) {
        Logger.log("onMessage", message);
        return routeRequest(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Logger.log(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    private String routeRequest(String request) {
        String responseMessage;
        try {
            BaseMessage requestMessage = JsonWorker.getDeserializeJson(request, BaseMessage.class);
            IUseCase useCase;
            switch (requestMessage.getType()) {
                case AUTHORIZE:
                    useCase = new AuthorizeUseCase();
                    break;
                default:
                    throw new IllegalArgumentException("unknown message type");
            }
            responseMessage = JsonWorker.getSerializeJson(useCase.execute(requestMessage));
        } catch (Exception e) { //не смогли распарсить сообщение или сообщение неопределенного типа
            Logger.error(e);
            responseMessage = JsonWorker.getSerializeJson(new ErrorMessage(ErrorCode.General.WTF));
        }
        return responseMessage;
    }
}