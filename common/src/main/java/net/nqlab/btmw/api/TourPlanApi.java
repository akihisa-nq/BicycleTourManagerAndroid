package net.nqlab.btmw.api;
 
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
 
public interface TourPlanApi {
 
    @GET("api/tour_plan/list")
    public Observable<TourPlanList> list(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @GET("api/tour_plan/{id}")
    public Observable<TourPlan> show(@Path("id") int id);

    @GET("api/tour_plan/{id}/schedule")
    public Observable<TourPlanSchedule> schedule(@Path("id") int id);
 
}
