package ru.scorpio92.socketchat.androidclient.data.repository.network;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.scorpio92.socketchat.androidclient.data.model.BaseMessage;

public interface API {

    @POST("/")
    Single<BaseMessage> register(@Body BaseMessage regMsg);

    @POST("/")
    Single<BaseMessage> authorize(@Body BaseMessage regMsg);
}
