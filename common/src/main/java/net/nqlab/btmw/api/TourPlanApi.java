package net.nqlab.btmw.api;
 
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
 
public interface TourPlanApi {
 
    @GET("/api/tour_plan/list")
    public Observable<TourPlanList> list(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("/api/tour_plan/{id}")
    public Observable<TourPlan> show(@Path("id") int id);

    @GET("/api/tour_plan/{id}/schedule")
    public Observable<TourPlanSchedule> schedule(@Path("id") int id);
 
}
