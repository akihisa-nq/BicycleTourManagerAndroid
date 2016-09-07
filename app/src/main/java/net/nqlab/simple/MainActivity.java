package net.nqlab.simple;

import android.support.v7.app.ActionBarActivity;
import android.support.customtabs.CustomTabsIntent;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import net.nqlab.btmw.ExclusionAreaList;

import net.nqlab.simple.BtmwApplication;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 要求に応じて API 実行
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (! getBtmwApplication().getApi().isLogin()) {
                    return;
                }

                // 非同期処理の実行
                getBtmwApplication().getApi().getExclusionAreaApi().list()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ExclusionAreaList>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(ExclusionAreaList list) {
                            if (list != null) {
                                ((TextView) findViewById(R.id.textView)).setText(
                                    list.getExclusionAreas().get(0).getPoint()
                                    );
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

    private BtmwApplication getBtmwApplication()
    {
        return (BtmwApplication) getApplicationContext();
    }
}
