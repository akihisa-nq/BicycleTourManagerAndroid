package net.nqlab.simple.model;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import android.net.Uri;
import android.nfc.FormatException;

import net.nqlab.btmw.ExclusionAreaApi;
import net.nqlab.btmw.ExclusionAreaList;
import net.nqlab.btmw.LoginApi;
import net.nqlab.btmw.AccessToken;
import net.nqlab.btmw.TourPlanApi;

import com.squareup.okhttp.OkHttpClient;

import retrofit.client.OkClient;
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

import net.nqlab.simple.model.ServerInfo;
import net.nqlab.simple.model.SecureSaveData;
import net.nqlab.simple.model.SaveData;
import net.nqlab.simple.model.BtmwApiLoginAdapter;

public class BtmwApi {
    private RestAdapter mAdapter = null;
    private SecureSaveData mSecureSaveData;
    private SaveData mSaveData;
    private String mAccessToken;
    private Gson mGson;
    private BtmwApiLoginAdapter mLoginAdapter;
    private SimpleDateFormat mFormat;

    public BtmwApi(SecureSaveData secureSaveData, SaveData saveData) {
        mGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();
        mSecureSaveData = secureSaveData;
        mSaveData = saveData;
        mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm::ss", Locale.JAPANESE);
    }

	public OkClient createOkClient() {
		OkHttpClient client = new OkHttpClient();
		final TrustManager[] trustManagers = new TrustManager[]{
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						// 特に何もしない
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						// 特に何もしない
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				}
		};

		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustManagers, new java.security.SecureRandom());
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			client.setSslSocketFactory(sslSocketFactory);
			client.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					// ホスト名の検証を行わない
					return true;
				}
			});

            client.setReadTimeout(1, TimeUnit.MINUTES);
            client.setWriteTimeout(1, TimeUnit.MINUTES);
            client.setConnectTimeout(1, TimeUnit.MINUTES);

		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
		return new OkClient(client);
	}

    private boolean createSession(String token)
    {
        mAccessToken = token;

        mAdapter = new RestAdapter.Builder()
            .setEndpoint(ServerInfo.URL_BASE)
            .setConverter(new GsonConverter(mGson))
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setLog(new AndroidLog("=NETWORK="))
            .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addHeader("Authorization", "Bearer " + mAccessToken);
                    }
                })
			.setClient(createOkClient())
            .build();

        try {
            ExclusionAreaList list = getExclusionAreaApi().list().toBlocking().first();
            if (list == null) {
                mAdapter = null;
                mAccessToken = null;
                return false;
            }
        } catch (Exception e) {
             mAdapter = null;
             mAccessToken = null;
             return false;
        }

        return true;
    }

    public boolean restoreSession()
    {
        String encryptedToken = mSaveData.loadToken("token");
        if (encryptedToken == null) {
            return false;
        }

        String token = mSecureSaveData.decryptKey(encryptedToken);
        if (token == null) {
            return false;
        }

        return createSession(token);
    }

    public void login(String code) {
        // RestAdapterの生成
        RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint(ServerInfo.URL_BASE)
            .setConverter(new GsonConverter(mGson))
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setLog(new AndroidLog("=NETWORK="))
			.setClient(createOkClient())
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
                    if (mLoginAdapter != null) {
                        mLoginAdapter.onLoginFailure();
                    }
                }

                @Override
                public void onNext(AccessToken token) {
                    if (token != null) {
                        String accessToken = token.getAccessToken();
                        mSaveData.saveToken("token", mSecureSaveData.encryptKey(accessToken));
                        if (createSession(accessToken)) {
                            if (mLoginAdapter != null) {
                                mLoginAdapter.onLoginSuccess();
                            }
                        } else {
                            if (mLoginAdapter != null) {
                                mLoginAdapter.onLoginFailure();
                            }
                        }
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

    public TourPlanApi getTourPlanApi()
    {
        if (mAdapter == null) { return null; }
        return mAdapter.create(TourPlanApi.class);
    }

    public void registerLoginAdapter(BtmwApiLoginAdapter loginAdapter)
    {
        mLoginAdapter = loginAdapter;
    }

    public void unregisterLoginAdapter()
    {
        mLoginAdapter = null;
    }

    public String toJson(Object obj)
    {
        return mGson.toJson(obj);
    }

    public Object fromJson(String str, Type type)
    {
        return mGson.fromJson(str, type);
    }

    public Date fromStringToDate(String strDate) {
        try {
            return mFormat.parse(strDate);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String fromDateToString(Date date) {
        return mFormat.format(date);
    }
}
