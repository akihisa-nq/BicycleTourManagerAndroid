package net.nqlab.btmw.handheld.model;

import java.lang.reflect.Type;
import java.util.Date;
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

import net.nqlab.btmw.api.ExclusionAreaApi;
import net.nqlab.btmw.api.ExclusionAreaList;
import net.nqlab.btmw.api.LoginApi;
import net.nqlab.btmw.api.AccessToken;
import net.nqlab.btmw.api.TourPlanApi;

import com.squareup.okhttp.OkHttpClient;

import retrofit.client.OkClient;
import retrofit.RestAdapter;
import retrofit.RequestInterceptor;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.google.gson.Gson;

import net.nqlab.btmw.handheld.model.ServerInfo;
import net.nqlab.btmw.api.SerDes;

public class BtmwApi {
    private RestAdapter mAdapter = null;
    private SecureSaveData mSecureSaveData;
    private SaveData mSaveData;
    private String mAccessToken;
    private BtmwApiLoginAdapter mLoginAdapter;
	private SerDes mSerDes;

    public BtmwApi(SecureSaveData secureSaveData, SaveData saveData) {
		mSerDes = new SerDes();
        mSecureSaveData = secureSaveData;
        mSaveData = saveData;
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
            .setConverter(new GsonConverter(mSerDes.getGson()))
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
            .setConverter(new GsonConverter(mSerDes.getGson()))
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
}
