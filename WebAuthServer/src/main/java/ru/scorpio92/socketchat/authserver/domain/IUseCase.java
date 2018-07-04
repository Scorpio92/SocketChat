package ru.scorpio92.socketchat.authserver.domain;

import ru.scorpio92.socketchat.authserver.data.model.message.base.BaseMessage;

public interface IUseCase {

    BaseMessage execute(BaseMessage requestMessage);
}
