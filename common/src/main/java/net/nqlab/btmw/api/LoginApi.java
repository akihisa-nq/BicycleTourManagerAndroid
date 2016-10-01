package net.nqlab.btmw.api;

import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import rx.Observable;

public interface LoginApi {
 
    @FormUrlEncoded
    @POST("oauth/token")
    Observable<AccessToken> getAccessToken(
            @Field("grant_type") String grant_type,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("code") String code,
            @Field("redirect_uri") String redirect_uri
			); 
}
