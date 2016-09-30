package net.nqlab.btmw.handheld.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.handheld.controller.BtmwApplication;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.TourPlanScheduleStore;

public class ShowOnlineActivity extends AppCompatActivity {
    private int mTourPlanId;
    private TourPlanScheduleStore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_online);

        mStore = new TourPlanScheduleStore(
            getBtmwApplication().getSaveData(),
            getBtmwApplication().getSecureSaveData(),
            getBtmwApplication().getApi()
            );

        Intent intent = getIntent();
        mTourPlanId = intent.getIntExtra("TourPlan", 0);

        Button button = (Button)findViewById(R.id.download);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                download();
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

    private void download()
    {
        if (! getBtmwApplication().getApi().isLogin()) {
            return;
        }

        final Button button = (Button) findViewById(R.id.download);
        button.setText(getResources().getText(R.string.download));

        getBtmwApplication().getApi().getTourPlanApi().schedule(mTourPlanId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<TourPlanSchedule>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    button.setText(getResources().getText(R.string.download_failure));
                }

                @Override
                public void onNext(TourPlanSchedule schedule) {
                    if (schedule != null) {
                        mStore.store(schedule);
                    }
                    button.setText(getResources().getText(R.string.download_success));
                }
            });
    }
}
