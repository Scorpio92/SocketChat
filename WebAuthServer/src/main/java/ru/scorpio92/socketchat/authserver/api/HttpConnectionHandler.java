package ru.scorpio92.socketchat.authserver.api;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import ru.scorpio92.socketchat.authserver.Constants;
import ru.scorpio92.socketchat.authserver.domain.ServiceRouter;
import ru.scorpio92.socketchat.authserver.tools.IO;
import ru.scorpio92.socketchat.authserver.tools.Logger;

/**
 * Хендлер-класс выполняющий обращение к роутеру за обработкой запроса
 * после обработки запроса в роутере отсылает сообщение клиенту
 */
public abstract class HttpConnectionHandler extends Thread {

    private HttpExchange exchange;

    public HttpConnectionHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void run() {
        Logger.log("connected client " + exchange.getRemoteAddress().getHostName() + ":" + exchange.getRemoteAddress().getPort());
        /*
        System.out.println("protocol: " + exchange.getProtocol());
        Headers headers = exchange.getRequestHeaders();
        for (String header : headers.keySet()) {
            System.out.println(header + "=" + headers.getFirst(header));
        }
        */
        try {
            transformRequestToString();
        } catch (IOException e) {
            Logger.error(e);
        } finally {
            if (exchange != null)
                exchange.close();
        }
    }

    private void transformRequestToString() throws IOException {
        //читаем то что пришло от клиента
        String requestBody =  new String(IO.getBytesFromInputStream(exchange.getRequestBody()), Constants.CHARSET);
        //обрабатываем и возвращаем массив байт
        sendMessageToClient(exchange, handleRequest(requestBody));
    }

    abstract byte[] handleRequest(String requestString) throws UnsupportedEncodingException;

    private void sendMessageToClient(HttpExchange exchange, byte[] bytes) {
        OutputStream os = exchange.getResponseBody();
        try {
            exchange.sendResponseHeaders(200, 0);
            os.write(bytes);
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }
}
