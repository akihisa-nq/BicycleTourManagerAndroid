package net.nqlab.simple.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import net.nqlab.btmw.TourPlanSchedule;
import net.nqlab.simple.controller.BtmwApplication;
import net.nqlab.simple.R;
import net.nqlab.simple.model.TourPlanScheduleStore;

public class GoActivity extends AppCompatActivity {
    private TourPlanSchedule mTourPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        Intent intent = getIntent();
        int tourPlanId = intent.getIntExtra("TourPlan", 0);
        mTourPlan = new TourPlanScheduleStore(
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getSecureSaveData(),
                getBtmwApplication().getApi()
        ).load(tourPlanId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
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
}
