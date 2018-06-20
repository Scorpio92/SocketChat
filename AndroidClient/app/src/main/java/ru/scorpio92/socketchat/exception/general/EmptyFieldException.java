package ru.scorpio92.socketchat.exception.general;

public class EmptyFieldException extends Exception {

    public EmptyFieldException(String field) {
        super("Необходимо обязательно заполнить поле: " + field);
    }
}
