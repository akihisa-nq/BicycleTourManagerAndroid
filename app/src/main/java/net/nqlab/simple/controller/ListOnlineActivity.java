package net.nqlab.simple.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import net.nqlab.btmw.TourPlan;
import net.nqlab.simple.controller.BtmwApplication;
import net.nqlab.simple.view.ListOnlineListViewAdapter;
import net.nqlab.simple.R;

import java.util.ArrayList;
import java.util.List;

public class ListOnlineActivity extends AppCompatActivity {
    private static final int MAX_READ_ITEMS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        ListView listView = (ListView)findViewById(R.id.listView);
        final ListOnlineListViewAdapter adapter = new ListOnlineListViewAdapter(ListOnlineActivity.this, getBtmwApplication().getApi());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    ListView listView = (ListView)adapterView;
                    TourPlan item = (TourPlan)listView.getItemAtPosition(position);

                    Intent intent = new Intent();
                    intent.setClassName("net.nqlab.simple", "net.nqlab.simple.controller.ShowOnlineActivity");
                    intent.putExtra("TourPlan", item.getId().intValue());
                    startActivity(intent);
                }
            });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                     @Override
                     public void onScrollStateChanged(AbsListView absListView, int i) {

                     }

                     @Override
                     public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                         if (adapter.getCount() == 0 ) {
                             return;
                         }

                         if ( adapter.getOffset() + adapter.getCacheCount() <= firstVisibleItem + visibleItemCount + 10) {
                             adapter.readMore(ListOnlineListViewAdapter.Direction.NEWER, MAX_READ_ITEMS, false);
                         } else if (firstVisibleItem <= adapter.getOffset() + 10) {
                             adapter.readMore(ListOnlineListViewAdapter.Direction.OLDER, MAX_READ_ITEMS, false);
                         }
                     }
                 }
            );

        adapter.readMore(ListOnlineListViewAdapter.Direction.NEWER, 10, true);
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
