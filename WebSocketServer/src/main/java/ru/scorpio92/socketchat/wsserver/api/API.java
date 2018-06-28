package ru.scorpio92.socketchat.wsserver.api;

import org.glassfish.tyrus.server.Server;

import javax.websocket.server.ServerEndpoint;

import ru.scorpio92.socketchat.wsserver.WebSocketServer;
import ru.scorpio92.socketchat.wsserver.tools.Logger;


/**
 * Базовый API класс поддерживающий выполнение в отдельном потоке
 */
public class API extends Thread {

    private int port;
    private Server server;
    private Class endpoint;

    public API(int port, Class endpoint) {
        this.port = port;
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        try {
            if (port <= 0 || port == 8025)
                throw new IllegalArgumentException("bad server port");

            if (!endpoint.isAnnotationPresent(ServerEndpoint.class))
                throw new IllegalArgumentException("endpoint not annotated as ServerEndpoint");

            server = new Server("localhost", port, "/", null, endpoint);
            server.start();
        } catch (Exception e) {
            WebSocketServer.finish();
            Logger.error("Failed start API: " + this.getClass().getSimpleName());
            Logger.error(e);
        }
    }

    public void stopAPI() {
        if (server != null)
            server.stop();
    }
}
