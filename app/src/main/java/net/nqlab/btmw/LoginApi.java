package net.nqlab.btmw;

import retrofit.http.POST;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Field;
 
public interface LoginApi {
 
    @FormUrlEncoded
    @POST("/token")
    AccessToken getAccessToken(
            @Field("code") String code
			); 
}
