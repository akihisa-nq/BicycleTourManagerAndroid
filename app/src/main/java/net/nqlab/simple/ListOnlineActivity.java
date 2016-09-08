package net.nqlab.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.nqlab.btmw.TourPlan;
import net.nqlab.btmw.TourPlanList;
import net.nqlab.btmw.TourPlanSchedule;
import net.nqlab.simple.BtmwApplication;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListOnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        ListView listView = (ListView)findViewById(R.id.listView);
        final ListOnlineListViewAdapter adapter = new ListOnlineListViewAdapter(ListOnlineActivity.this);
        adapter.setTourPlanList(new ArrayList<TourPlan>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListView listView = (ListView)adapterView;
                TourPlan item = (TourPlan)listView.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.setClassName("net.nqlab.simple", "net.nqlab.simple.ShowOnlineActivity");
                intent.putExtra("TourPlan", item.getId().intValue());
                startActivity(intent);
            }
        });

        getBtmwApplication().getApi().getTourPlanApi().list()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<TourPlanList>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(TourPlanList tourPlanList) {
                    adapter.setTourPlanList(tourPlanList.getTourPlans());
                    adapter.notifyDataSetChanged();
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
