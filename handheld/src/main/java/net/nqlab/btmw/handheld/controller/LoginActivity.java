package net.nqlab.btmw.handheld.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.support.customtabs.CustomTabsIntent;

import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import net.nqlab.btmw.handheld.model.BtmwApiLoginAdapter;
import net.nqlab.btmw.handheld.R;

public class LoginActivity extends AppCompatActivity {
    private boolean mTryLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTryLogin = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        getBtmwApplication().getApi().unregisterLoginAdapter();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        login();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private BtmwApplication getBtmwApplication() {
        return (BtmwApplication) getApplicationContext();
    }

    private void login()
    {
        if (mTryLogin) {
            return;
        }

        getBtmwApplication().getApi().registerLoginAdapter(new BtmwApiLoginAdapter() {
            public void onLoginSuccess() {
                switchListOnlineActivity();
            }

            public void onLoginFailure() {
                switchListDownloadedActivity();
            }
        });

        if (getBtmwApplication().getApi().restoreSession()) {
            switchListOnlineActivity();

        } else {
            // 多重ログイン回避
            mTryLogin = true;

            // PIN コード取得
            final CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    // .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
                    // .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                    // .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .build();
            tabsIntent.launchUrl(this, getBtmwApplication().getApi().getLoginUri());

            //テキスト入力を受け付けるビューを作成します
            final EditText editView = new EditText(LoginActivity.this);
            new AlertDialog.Builder(LoginActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("認証コードの入力")
                    //setViewにてビューを設定します
                    .setView(editView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String code = editView.getText().toString();
                            getBtmwApplication().getApi().login(code);
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            switchListDownloadedActivity();
                        }
                    })
                    .show();
        }
    }

    private void switchListDownloadedActivity() {
        mTryLogin = false;

        Intent intent = new Intent();
        intent.setClass(this, ListDownloadedActivity.class);
        startActivity(intent);
    }

    private void switchListOnlineActivity() {
        mTryLogin = false;

        Intent intent = new Intent();
        intent.setClass(this, ListOnlineActivity.class);
        startActivity(intent);
    }
}
