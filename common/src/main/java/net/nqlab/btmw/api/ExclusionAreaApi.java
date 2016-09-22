package net.nqlab.btmw.api;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
 
public interface ExclusionAreaApi {
 
    @GET("/api/exclusion_area/list")
    public Observable<ExclusionAreaList> list();
 
}
