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

public class ShowDownloadedActivity extends AppCompatActivity {
    TourPlanSchedule mTourPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_downloaded);
        getBtmwApplication().setupNavigation(this, R.id.drawer_layout_show_downloaded);

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
