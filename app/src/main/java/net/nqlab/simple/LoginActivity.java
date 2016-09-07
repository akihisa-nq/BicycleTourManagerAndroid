package net.nqlab.simple;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.support.customtabs.CustomTabsIntent;

import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import net.nqlab.simple.BtmwApplication;
import net.nqlab.simple.BtmwApiLoginAdapter;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

		getBtmwApplication().getApi().registerLoginAdapter(new BtmwApiLoginAdapter() {
			public void onLoginSuccess() {
				Intent intent = new Intent();
				intent.setClassName("net.nqlab.simple", "net.nqlab.simple.ListOnlineActivity");
				startActivity(intent);
			}

			public void onLoginFailure() {
				Intent intent = new Intent();
				intent.setClassName("net.nqlab.simple", "net.nqlab.simple.ListDownloadedActivity");
				startActivity(intent);
			}
		});

        if (! getBtmwApplication().getApi().restoreSession()) {
            // PIN コード取得
            final CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                // .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
                // .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                // .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .build();
            tabsIntent.launchUrl(this, getBtmwApplication().getApi().getLoginUri());

            //テキスト入力を受け付けるビューを作成します。
            final EditText editView = new EditText(LoginActivity.this);
            new AlertDialog.Builder(LoginActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("テキスト入力ダイアログ")
                //setViewにてビューを設定します。
                .setView(editView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String code = editView.getText().toString();
                        getBtmwApplication().getApi().login(code);
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
        }
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

    private BtmwApplication getBtmwApplication()
    {
        return (BtmwApplication) getApplicationContext();
    }
}
