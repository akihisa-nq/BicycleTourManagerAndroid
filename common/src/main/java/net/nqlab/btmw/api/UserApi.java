package net.nqlab.btmw.api;
 
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
 
public interface UserApi {
 
    @GET("api/user/{id}")
    public Observable<User> show(@Path("id") String id);
 
}

