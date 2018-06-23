package ru.scorpio92.socketchat.authserver;

import ru.scorpio92.socketchat.authserver.api.PublicAPI;
import ru.scorpio92.socketchat.authserver.api.ServiceAPI;
import ru.scorpio92.socketchat.authserver.tools.Logger;

/**
 * Основной класс
 */
public class AuthServer {

    public static void main(String[] args) throws Throwable {

        //инициализация глобального конфига
        ServerConfigStore.init();
        Logger.log("ServerConfigStore init complete");

        //инициализация публичной части API
        new PublicAPI().start();

        //инициализация сервисной части API
        new ServiceAPI().start();
    }
}
