package ru.scorpio92.socketchat.androidclient.exception.general;

public class IpBlockedException extends Exception {

    public IpBlockedException() {
        super("Ваш адрес IP адрес заблокирован из-за многократных запросов");
    }
}
