package ru.scorpio92.socketchat.wsserver.domain;

import java.io.UnsupportedEncodingException;

public class ServiceRouter {

    private String requestBody;

    public ServiceRouter(String requestBody) {
        this.requestBody = requestBody;
    }

    public byte[] getResult() throws UnsupportedEncodingException {
        return null;
    }
}
