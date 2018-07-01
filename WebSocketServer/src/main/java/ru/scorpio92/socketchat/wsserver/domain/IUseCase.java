package ru.scorpio92.socketchat.wsserver.domain;

import ru.scorpio92.socketchat.wsserver.data.model.message.base.BaseMessage;

public interface IUseCase {

    BaseMessage execute(BaseMessage requestMessage);
}
