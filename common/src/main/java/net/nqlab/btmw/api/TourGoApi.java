package net.nqlab.btmw.api;
 
import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import rx.Observable;
 
public class TourGoApi {
    public interface Impl {

        @GET("api/tour_go/list")
        public Observable<TourGoList> list(@Query("offset") Integer offset, @Query("limit") Integer limit);

        @GET("api/tour_go/{id}")
        public Observable<TourGo> show(@Path("id") int id);

        @POST("api/tour_go")
        public Observable<TourGoCreateResult> create(@Body RequestBody go);

        @PUT("api/tour_go/{id}")
        public Observable<TourGoUpdateResult> update(@Path("id") int id, @Body RequestBody go);

        @DELETE("api/tour_go/{id}")
        public Observable<TourGoDeleteResult> delete(@Path("id") int id);
     
    }

    class RequestTourGoBody extends RequestBody {
        private String mValue;

        public RequestTourGoBody(String val) {
            mValue = val;
        }

        @Override
        public MediaType contentType() {
            return MediaType.parse("application/json");
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            sink.write(("{ \"tour_go\": " + mValue + "}").getBytes("UTF-8"));
        }
    }

    private Impl mImpl;
    private SerDes mSerDes;

    public TourGoApi(Impl impl, SerDes serDes) {
        mImpl = impl;
        mSerDes = serDes;
    }

    public Observable<TourGoList> list(Integer offset, Integer limit) {
        return mImpl.list(offset, limit);
    }

    public Observable<TourGo> show(int id) {
        return mImpl.show(id);
    }

    public Observable<TourGoCreateResult> create(TourGo go) {
        return mImpl.create(new RequestTourGoBody(mSerDes.toJson(go)));
    }

    public Observable<TourGoUpdateResult> update(int id, TourGo go) {
        return mImpl.update(id, new RequestTourGoBody(mSerDes.toJson(go)));
    }

    public Observable<TourGoDeleteResult> delete(int id) {
        return mImpl.delete(id);
    }
}

