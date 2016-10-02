package net.nqlab.btmw.handheld.model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import android.net.Uri;
import android.util.Log;

import net.nqlab.btmw.api.ExclusionArea;
import net.nqlab.btmw.api.ExclusionAreaApi;
import net.nqlab.btmw.api.ExclusionAreaList;
import net.nqlab.btmw.api.LoginApi;
import net.nqlab.btmw.api.AccessToken;
import net.nqlab.btmw.api.TourGoApi;
import net.nqlab.btmw.api.TourGoCreateResult;
import net.nqlab.btmw.api.TourGoEvent;
import net.nqlab.btmw.api.TourGoUpdateResult;
import net.nqlab.btmw.api.TourPlanApi;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.google.gson.Gson;

import net.nqlab.btmw.handheld.controller.ListLocalGoActivity;
import net.nqlab.btmw.handheld.model.ServerInfo;
import net.nqlab.btmw.api.SerDes;

public class BtmwApi {
    private Retrofit mAdapter = null;
    private SecureSaveData mSecureSaveData;
    private SaveData mSaveData;
    private String mAccessToken;
    private SerDes mSerDes;

    public interface OnLoginListener {
        void onLoginSuccess();
        void onLoginFailure();
    }

    public BtmwApi(SecureSaveData secureSaveData, SaveData saveData) {
        mSerDes = new SerDes();
        mSecureSaveData = secureSaveData;
        mSaveData = saveData;
    }

    public OkHttpClient createOkClient() {
        final TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
        };

        OkHttpClient client = null;
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            final X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            // ホスト名の検証を行わない
                            return true;
                        }
                    })
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            final Request.Builder builder = chain.request().newBuilder();
                            builder.addHeader("Authorization", "Bearer " + mAccessToken);
                            return chain.proceed(builder.build());
                        }
                    })
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return client;
    }

    private void createSession(String token, final OnLoginListener listener)
    {
        mAccessToken = token;

        mAdapter = new Retrofit.Builder()
            .baseUrl(ServerInfo.URL_BASE + "/")
            .addConverterFactory(GsonConverterFactory.create(mSerDes.getGson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(createOkClient())
            .build();

        getExclusionAreaApi().list()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ExclusionAreaList>() {
                @Override
                public void onCompleted() {
                    listener.onLoginSuccess();
                }

                @Override
                public void onError(Throwable e) {
                    mAdapter = null;
                    mAccessToken = null;
                    listener.onLoginFailure();
                }

                @Override
                public void onNext(ExclusionAreaList exclusionAreaList) {
                }
            });
    }

    public void restoreSession(OnLoginListener listener)
    {
        String encryptedToken = mSaveData.loadToken("token");
        if (encryptedToken == null) {
            listener.onLoginFailure();
            return;
        }

        String token = mSecureSaveData.decryptKey(encryptedToken);
        if (token == null) {
            listener.onLoginFailure();
            return;
        }

        createSession(token, listener);
    }

    public void login(String code, final OnLoginListener listener) {
        Retrofit adapter = new Retrofit.Builder()
            .baseUrl(ServerInfo.URL_BASE + "/")
            .addConverterFactory(GsonConverterFactory.create(mSerDes.getGson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(createOkClient())
            .build();

        // アクセス トークン取得
        adapter.create(LoginApi.class).getAccessToken(
                "authorization_code",
                ServerInfo.CLIENT_ID,
                ServerInfo.CLIENT_SECRET,
                code,
                ServerInfo.REDIRECT_URL
                )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<AccessToken>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    listener.onLoginFailure();
                }

                @Override
                public void onNext(AccessToken token) {
                    if (token != null) {
                        String accessToken = token.getAccessToken();
                        mSaveData.saveToken("token", mSecureSaveData.encryptKey(accessToken));
                        createSession(accessToken, listener);
                    }
                }
            });
    }

    public Uri getLoginUri()
    {
        Uri uri = Uri.parse(
            ServerInfo.URL_BASE
                + "/oauth/authorize?"
                + "client_id=" + ServerInfo.CLIENT_ID
                + "&redirect_uri=" + java.net.URLEncoder.encode(ServerInfo.REDIRECT_URL)
                + "&response_type=code"
                + "&scope="
            );
        return uri;
    }

    public boolean isLogin()
    {
        return mAdapter != null;
    }

    public ExclusionAreaApi getExclusionAreaApi()
    {
        if (mAdapter == null) { return null; }
        return mAdapter.create(ExclusionAreaApi.class);
    }

    public interface UploadTourGoListerner {
        void onBegin();
        void onError();
        void onDone();
    }

    public void uploadTourGo(final TourGo go, final UploadTourGoListerner listener) {
        final TourGoApi api = getTourGoApi();
        if (api == null) {
            listener.onError();
            return;
        }

        List<TourGoEvent> listEvents = new ArrayList<TourGoEvent>();
        for (TourGoPassPoint passPoint : mSaveData.getTourGoPassPoints(go._id)) {
            TourGoEvent event = new TourGoEvent();
            event.setEventType("pass_point");
            event.setOccuredOn(passPoint.passed_on);
            event.setTourPlanPointId((int)passPoint.tour_plan_point_id);
            listEvents.add(event);
        }

        final net.nqlab.btmw.api.TourGo goApi = new net.nqlab.btmw.api.TourGo();
        goApi.setStartTime(go.start_time);
        goApi.setTourPlanId((int)go.tour_plan_shedule_id);
        goApi.setTourGoEvents(listEvents);

        listener.onBegin();

        if (go.tour_go_id.longValue() == 0) {
            api.create(goApi)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TourGoCreateResult>() {
                        @Override
                        public void onCompleted() {
                            listener.onDone();
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onError();
                        }

                        @Override
                        public void onNext(TourGoCreateResult result) {
                            go.tour_go_id = result.getId().longValue();
                            mSaveData.updateTourGoId(go);
                        }
                    });
        } else {
            api.update(go.tour_go_id.intValue(), goApi)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TourGoUpdateResult>() {
                        @Override
                        public void onCompleted() {
                            listener.onDone();
                        }

                        @Override
                        public void onError(Throwable e) {
                            api.create(goApi)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<TourGoCreateResult>() {
                                    @Override
                                    public void onCompleted() {
                                        listener.onDone();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        listener.onError();
                                    }

                                    @Override
                                    public void onNext(TourGoCreateResult result) {
                                        go.tour_go_id = result.getId().longValue();
                                        mSaveData.updateTourGoId(go);
                                    }
                                });
                        }

                        @Override
                        public void onNext(TourGoUpdateResult result) {
                        }
                    });
        }
    }

    public TourPlanApi getTourPlanApi()
    {
        if (mAdapter == null) { return null; }
        return mAdapter.create(TourPlanApi.class);
    }

    public TourGoApi getTourGoApi()
    {
        if (mAdapter == null) { return null; }
        return new TourGoApi(mAdapter.create(TourGoApi.Impl.class), getSerDes());
    }

    public String toJson(Object obj)
    {
        return mSerDes.toJson(obj);
    }

    public Object fromJson(String str, Type type)
    {
        return mSerDes.fromJson(str, type);
    }

    public Date fromStringToDate(String strDate) {
        return mSerDes.fromStringToDate(strDate);
    }

    public String fromDateToString(Date date) {
        return mSerDes.fromDateToString(date);
    }

    public SerDes getSerDes() {
        return mSerDes;
    }
}
