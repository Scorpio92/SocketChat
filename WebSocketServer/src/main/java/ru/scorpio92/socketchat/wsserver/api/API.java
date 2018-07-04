package ru.scorpio92.socketchat.wsserver.api;

import org.glassfish.tyrus.server.Server;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.ServerEndpoint;


/**
 * Базовый API класс поддерживающий выполнение в отдельном потоке
 */
public class API extends Thread {

    private static List<API> servers = new ArrayList<>();

    private int port;
    private Server server;
    private Class endpoint;
    private Callback callback;
    private String apiName;

    public API(int port, Class endpoint) {
        this.port = port;
        this.endpoint = endpoint;
        this.apiName = endpoint.getSimpleName() + "(" + port + ")";
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
            if (callback != null)
                callback.onApiStart(apiName);
        } catch (Exception e) {
            if (callback != null)
                callback.onError(e);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void stopAPI() {
        if (server != null) {
            server.stop();
            if (callback != null) {
                callback.onApiStop(apiName);
                callback = null;
            }
        }
    }

    public static void addServer(API api) {
        servers.add(api);
    }

    public static void startServers() {
        for (API api : servers)
            api.run();
    }

    public static void stopServers() {
        for (API api : servers)
            api.stopAPI();
        servers.clear();
    }

    public interface Callback {
        void onApiStart(String apiName);

        void onApiStop(String apiName);

        void onError(Exception e);
    }
}
