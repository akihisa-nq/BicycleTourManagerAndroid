package net.nqlab.simple;

import java.util.Date;

import android.net.Uri;

import net.nqlab.btmw.ExclusionAreaApi;
import net.nqlab.btmw.ExclusionAreaList;
import net.nqlab.btmw.LoginApi;
import net.nqlab.btmw.AccessToken;

import retrofit.RestAdapter;
import retrofit.RequestInterceptor;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import net.nqlab.simple.ServerInfo;
import net.nqlab.simple.SecureSaveData;
import net.nqlab.simple.SaveData;

public class BtmwApi {
    private RestAdapter mAdapter = null;
    private SecureSaveData mSecureSaveData;
    private SaveData mSaveData;

    public BtmwApi(SecureSaveData secureSaveData, SaveData saveData) {
        mSecureSaveData = secureSaveData;
        mSaveData = saveData;
    }

    private void createSession(String token)
    {
        final String accessToken = token;

        // JSONのパーサー
        final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

        mAdapter = new RestAdapter.Builder()
            .setEndpoint(ServerInfo.URL_BASE)
            .setConverter(new GsonConverter(gson))
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setLog(new AndroidLog("=NETWORK="))
            .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addHeader("Authorization", "Bearer " + accessToken);
                    }
                })
            .build();
    }

    public boolean restoreSession()
    {
        String encryptedToken = mSaveData.load("token");
        if (encryptedToken == null) {
            return false;
        }

        String token = mSecureSaveData.decryptString(encryptedToken);
        if (token == null) {
            return false;
        }

        createSession(token);

        try {
            ExclusionAreaList list = getExclusionAreaApi().list().toBlocking().first();
            if (list == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void login(String code) {
        // RestAdapterの生成
        RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint(ServerInfo.URL_BASE)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setLog(new AndroidLog("=NETWORK="))
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
                }

                @Override
                public void onNext(AccessToken token) {
                    if (token != null) {
                        String accessToken = token.getAccessToken();
                        mSaveData.save("token", mSecureSaveData.encryptString(accessToken));
                        createSession(accessToken);
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
}
