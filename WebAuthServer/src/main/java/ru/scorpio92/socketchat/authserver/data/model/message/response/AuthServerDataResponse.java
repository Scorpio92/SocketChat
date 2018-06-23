package ru.scorpio92.socketchat.authserver.data.model.message.response;

public class AuthServerDataResponse {

    private String authToken;

    public AuthServerDataResponse(String authToken) {
        this.authToken = authToken;
    }
}
