package ru.scorpio92.socketchat.androidclient.data.repository.network.base;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.scorpio92.socketchat.androidclient.data.model.BaseMessage;

public interface IAuthRepo {

    Completable register(BaseMessage regMsg);

    Single<String> authorizeAndGetToken(BaseMessage authMsg);
}
