package net.nqlab.btmw.handheld.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.view.ListDownloadedListViewAdapter;

public class ListDownloadedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_downloaded);
        getBtmwApplication().setupNavigation(this, R.id.drawer_layout_list_downloaded);

        ListView listView = (ListView)findViewById(R.id.listView);
        final ListDownloadedListViewAdapter adapter = new ListDownloadedListViewAdapter(
                ListDownloadedActivity.this,
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getSecureSaveData(),
                getBtmwApplication().getApi()
            );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListView listView = (ListView)adapterView;
                TourPlanSchedule item = (TourPlanSchedule)listView.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.setClassName("net.nqlab.simple", "net.nqlab.simple.controller.ShowDownloadedActivity");
                intent.putExtra("TourPlan", item.getId().intValue());
                startActivity(intent);
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
