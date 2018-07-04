package ru.scorpio92.socketchat.authserver.api;

import com.sun.net.httpserver.HttpExchange;

import java.io.UnsupportedEncodingException;

import ru.scorpio92.socketchat.authserver.domain.ServiceRouter;

/**
 * Хендлер-класс выполняющий обращение к роутеру за обработкой запроса
 * после обработки запроса в роутере отсылает сообщение клиенту
 */
public class PublicConnectionHandler extends HttpConnectionHandler {

    public PublicConnectionHandler(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    byte[] handleRequest(String requestString) throws UnsupportedEncodingException {
        return new ServiceRouter(requestString).getResult();
    }
}
