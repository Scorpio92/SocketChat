package ru.scorpio92.socketchat.authserver.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import ru.scorpio92.socketchat.authserver.tools.Logger;

/**
 * Базовый HttpAPI класс поддерживающий выполнение в отдельном потоке
 * и содержащий инстансы HTTP и WebSocket серверов
 */
public class HttpAPI extends Thread implements HttpHandler {

    private HttpServer server;
    private Class httpConnectionHandlerClass;
    private int port;
    private ApiCallback callback;
    private String apiName;

    public HttpAPI(int port, Class httpConnectionHandlerClass) {
        this.port = port;
        this.httpConnectionHandlerClass = httpConnectionHandlerClass;
        this.apiName = this.getClass().getSimpleName() + "(" + port + ")";
    }

    @Override
    public void run() {
        try {
            if (port <= 0 || port == 80 || port == 8080)
                throw new IllegalArgumentException("bad server port");

            if (!httpConnectionHandlerClass.getSuperclass().isAssignableFrom(HttpConnectionHandler.class))
                throw new IllegalArgumentException("connectionHandler is not HttpConnectionHandler");

            server = HttpServer.create();
            server.bind(new InetSocketAddress(port), 0);
            server.createContext("/", this);
            server.start();
            if (callback != null)
                callback.onApiStart(apiName);
        } catch (Exception e) {
            if (callback != null)
                callback.onError(e);
        }
    }

    public void setCallback(ApiCallback callback) {
        this.callback = callback;
    }

    public void stopAPI() {
        if (server != null) {
            server.stop(0);
            if (callback != null) {
                callback.onApiStop(apiName);
                callback = null;
            }
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            HttpConnectionHandler httpConnectionHandler = (HttpConnectionHandler) httpConnectionHandlerClass.getConstructor(HttpExchange.class).newInstance(httpExchange);
            httpConnectionHandler.start();
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
