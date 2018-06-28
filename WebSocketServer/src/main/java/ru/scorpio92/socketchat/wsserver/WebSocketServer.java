package ru.scorpio92.socketchat.wsserver;

import java.util.ArrayList;
import java.util.List;

import ru.scorpio92.socketchat.wsserver.api.API;
import ru.scorpio92.socketchat.wsserver.api.PublicEndpoint;
import ru.scorpio92.socketchat.wsserver.tools.Logger;

/**
 * Основной класс
 */
public class WebSocketServer {

    private static volatile boolean stop = false;

    private static List<API> servers = new ArrayList<>();

    public static void main(String[] args) throws Throwable {

        //инициализация глобального конфига
        //ServerConfigStore.init();
        Logger.log("ServerConfigStore init complete");

        //инициализация публичной части API
        servers.add(new API(ServerConfigStore.SERVER_PORT, PublicEndpoint.class));

        startServers();

        while (true) {
            if (stop) {
                stopServers();
                break;
            }
        }
    }

    private static void startServers() {
        for (API api : servers)
            api.run();
    }

    private static void stopServers() {
        for (API api : servers)
            api.stopAPI();
        servers.clear();
    }

    public static void finish() {
        stop = true;
    }
}
