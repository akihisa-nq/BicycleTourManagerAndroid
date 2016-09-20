package net.nqlab.btmw.handheld.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.TourPlanScheduleStore;
import net.nqlab.btmw.handheld.view.ShowDownloadedExpandableListViewAdapter;

public class ShowDownloadedActivity extends AppCompatActivity {
    private TourPlanSchedule mTourPlan;

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

        ExpandableListView listView = (ExpandableListView)findViewById(R.id.expandableListView);
        final ShowDownloadedExpandableListViewAdapter adapter = new ShowDownloadedExpandableListViewAdapter(this, mTourPlan);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_downloaded, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_go: {
                    Intent intent = new Intent();
                    intent.setClassName("net.nqlab.simple", "net.nqlab.simple.controller.GoActivity");
                    intent.putExtra("TourPlan", mTourPlan.getId());
                    startActivity(intent);
                     return true;
                }
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
