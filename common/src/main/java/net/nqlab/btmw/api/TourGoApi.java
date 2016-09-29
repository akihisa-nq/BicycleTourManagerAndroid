package net.nqlab.btmw.api;
 
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Body;
import rx.Observable;
 
public interface TourGoApi {
 
    @GET("/api/tour_go/list")
    public Observable<TourGoList> list(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("/api/tour_go/{id}")
    public Observable<TourGo> show(@Path("id") int id);

    @POST("/api/tour_go")
    public Observable<TourGoCreateResult> create(@Body TourGo go);
 
}
