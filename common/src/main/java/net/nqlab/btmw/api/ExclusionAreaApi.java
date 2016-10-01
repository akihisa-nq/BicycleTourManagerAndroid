package net.nqlab.btmw.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
 
public interface ExclusionAreaApi {
 
    @GET("api/exclusion_area/list")
    public Observable<ExclusionAreaList> list();
 
}
