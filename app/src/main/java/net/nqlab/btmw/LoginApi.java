package net.nqlab.btmw;

import retrofit.http.POST;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Field;
 
public interface LoginApi {
 
    @FormUrlEncoded
    @POST("/oauth/token")
    AccessToken getAccessToken(
            @Field("grant_type") String grant_type,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("code") String code,
            @Field("redirect_uri") String redirect_uri
			); 
}
