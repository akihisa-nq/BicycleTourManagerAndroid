package net.nqlab.simple.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import net.nqlab.btmw.TourPlanSchedule;

import net.nqlab.simple.controller.BtmwApplication;
import net.nqlab.simple.R;

public class ShowOnlineActivity extends AppCompatActivity {
    private int mTourPlanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_online);

        Intent intent = getIntent();
        mTourPlanId = intent.getIntExtra("TourPlan", 0);

        download();
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

		getBtmwApplication().getApi().getTourPlanApi().schedule(mTourPlanId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<TourPlanSchedule>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(TourPlanSchedule schedule) {
                    if (schedule != null) {
						String strData = getBtmwApplication().getApi().toJson(schedule);
						String strSaveData = getBtmwApplication().getSecureSaveData().encryptString(strData);
						getBtmwApplication().getSaveData().saveTourPlan(
							schedule.getId(),
							schedule.getName(),
							strSaveData
							);
                    }
                }
			});
	}
}
