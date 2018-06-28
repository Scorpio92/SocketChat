package ru.scorpio92.socketchat.wsserver.api;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import ru.scorpio92.socketchat.wsserver.tools.Logger;

@ServerEndpoint(value = "/")
public class PublicEndpoint {

    @OnMessage
    public String onMessage(String message, Session session) throws Exception {
        Logger.log("onMessage", message);
        return message;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Logger.log(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

}