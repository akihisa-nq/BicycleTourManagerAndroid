package net.nqlab.btmw.api;
 
import com.google.gson.Gson;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import rx.Observable;
 
public interface TourGoApi {

    @GET("api/tour_go/list")
    public Observable<TourGoList> list(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("api/tour_go/{id}")
    public Observable<TourGo> show(@Path("id") int id);

    @POST("api/tour_go")
    public Observable<TourGoCreateResult> create(@Body TourGo go);

    @PUT("api/tour_go/{id}")
    public Observable<TourGoUpdateResult> update(@Path("id") int id, @Body TourGo go);

    @DELETE("api/tour_go/{id}")
    public Observable<TourGoDeleteResult> delete(@Path("id") int id);
 
}
