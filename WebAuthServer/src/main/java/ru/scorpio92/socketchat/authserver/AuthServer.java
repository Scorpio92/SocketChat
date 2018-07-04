package ru.scorpio92.socketchat.authserver;

import ru.scorpio92.socketchat.authserver.api.ApiCallback;
import ru.scorpio92.socketchat.authserver.api.HttpAPI;
import ru.scorpio92.socketchat.authserver.api.PublicConnectionHandler;
import ru.scorpio92.socketchat.authserver.api.ServiceEndpoint;
import ru.scorpio92.socketchat.authserver.api.WebSocketAPI;
import ru.scorpio92.socketchat.authserver.tools.Logger;

import static ru.scorpio92.socketchat.authserver.api.WebSocketAPI.addServer;
import static ru.scorpio92.socketchat.authserver.api.WebSocketAPI.startServers;
import static ru.scorpio92.socketchat.authserver.api.WebSocketAPI.stopServers;

/**
 * Основной класс
 */
public class AuthServer {

    private static volatile boolean stop = false;

    public static void main(String[] args) throws Throwable {

        Logger.log("AuthServer starting...");

        //инициализация глобального конфига
        //ServerConfigStore.init();
        Logger.log("ServerConfigStore init complete");

        //инициализация публичной части HttpAPI
        HttpAPI httpAPI = new HttpAPI(ServerConfigStore.PAPI_SERVER_PORT, PublicConnectionHandler.class);
        httpAPI.setCallback(getApiCallback());

        WebSocketAPI api = new WebSocketAPI(ServerConfigStore.SAPI_SERVER_PORT, ServiceEndpoint.class);
        api.setCallback(getApiCallback());

        addServer(api);

        httpAPI.run();
        startServers();

        while (true) {
            if (stop) {
                stopServers();
                httpAPI.stopAPI();
                break;
            }
        }

        Logger.log("AuthServer finished");
    }

    private static void finish() {
        stop = true;
    }

    private static ApiCallback getApiCallback() {
        return new ApiCallback() {
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
