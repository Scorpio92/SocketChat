package ru.scorpio92.socketchat.wsserver;

import ru.scorpio92.socketchat.wsserver.tools.Logger;

/**
 * Основной класс
 */
public class WebSocketServer {

    public static void main(String[] args) throws Throwable {

        //инициализация глобального конфига
        ServerConfigStore.init();
        Logger.log("ServerConfigStore init complete");
    }
}
