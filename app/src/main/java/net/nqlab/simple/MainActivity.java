package net.nqlab.simple;

import android.support.v7.app.ActionBarActivity;
import android.support.customtabs.CustomTabsIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
 
import java.util.Date;
 
import retrofit.RestAdapter;
import retrofit.RequestInterceptor;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import net.nqlab.btmw.ExclusionAreaApi;
import net.nqlab.btmw.ExclusionAreaList;
import net.nqlab.btmw.LoginApi;
import net.nqlab.btmw.AccessToken;

import net.nqlab.simple.ServerInfo;

public class MainActivity extends ActionBarActivity {
    private RestAdapter m_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // JSONのパーサー
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
 
        // PIN コード取得
        Uri uri = Uri.parse(
            ServerInfo.URL_BASE
                + "/oauth/authorize?"
                + "client_id=" + ServerInfo.CLIENT_ID
                + "&redirect_uri=" + java.net.URLEncoder.encode(ServerInfo.REDIRECT_URL)
                + "&response_type=code"
                + "&scope="
            );
        final CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder()
            .setShowTitle(true)
            // .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
            // .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
            // .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .build();
        tabsIntent.launchUrl(this, uri);

        //テキスト入力を受け付けるビューを作成します。
        final EditText editView = new EditText(MainActivity.this);
        new AlertDialog.Builder(MainActivity.this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("テキスト入力ダイアログ")
            //setViewにてビューを設定します。
            .setView(editView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String code = editView.getText().toString();

                    // RestAdapterの生成
                    RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(ServerInfo.URL_BASE)
                        .setConverter(new GsonConverter(gson))
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
                                Log.d("MainActivity", "onCompleted()");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("MainActivity", "Error : " + e.toString());
                            }

                            @Override
                            public void onNext(AccessToken token) {
                                Log.d("MainActivity", "onNext()");
                                if (token != null) {
                                    final String accessToken = token.getAccessToken();

                                    m_adapter = new RestAdapter.Builder()
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
                            }
                        });
                }
            })
            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .show();

        // 要求に応じて API 実行
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (m_adapter == null) {
                    return;
                }

                // 非同期処理の実行
                m_adapter.create(ExclusionAreaApi.class).list()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ExclusionAreaList>() {
                        @Override
                        public void onCompleted() {
                            Log.d("MainActivity", "onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("MainActivity", "Error : " + e.toString());
                        }

                        @Override
                        public void onNext(ExclusionAreaList list) {
                            Log.d("MainActivity", "onNext()");
                            if (list != null) {
                                ((TextView) findViewById(R.id.textView)).setText(list.getExclusionAreas().get(0).getPoint());
                            }
                        }
                    });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
