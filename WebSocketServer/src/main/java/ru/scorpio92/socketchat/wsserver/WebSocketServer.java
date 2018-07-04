package ru.scorpio92.socketchat.wsserver;

import ru.scorpio92.socketchat.wsserver.api.API;
import ru.scorpio92.socketchat.wsserver.api.PublicEndpoint;
import ru.scorpio92.socketchat.wsserver.tools.Logger;

import static ru.scorpio92.socketchat.wsserver.api.API.addServer;
import static ru.scorpio92.socketchat.wsserver.api.API.startServers;
import static ru.scorpio92.socketchat.wsserver.api.API.stopServers;

/**
 * Основной класс
 */
public class WebSocketServer {

    private static volatile boolean stop = false;


    public static void main(String[] args) throws Throwable {

        Logger.log("WebSocketServer starting...");

        //инициализация глобального конфига
        //ServerConfigStore.init();
        Logger.log("ServerConfigStore init complete");

        //инициализация публичной части API
        API api = new API(ServerConfigStore.SERVER_PORT, PublicEndpoint.class);
        api.setCallback(getApiCallback());

        addServer(api);

        startServers();

        while (true) {
            if (stop) {
                stopServers();
                break;
            }
        }

        Logger.log("WebSocketServer finished");
    }

    private static void finish() {
        stop = true;
    }

    private static API.Callback getApiCallback() {
        return new API.Callback() {
            @Override
            public void onApiStart(String apiName) {
                Logger.log(apiName + " API was started");
            }

            @Override
            public void onApiStop(String apiName) {
                Logger.log(apiName + " API was stopped");
            }

            @Override
            public void onError(Exception e) {
                Logger.error(e);
                finish();
            }
        };
    }
}
