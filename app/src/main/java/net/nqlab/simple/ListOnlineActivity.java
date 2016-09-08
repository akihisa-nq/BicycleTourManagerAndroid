package net.nqlab.simple;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import net.nqlab.btmw.TourPlan;

import net.nqlab.simple.BtmwApplication;

public class ListOnlineActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        ListView listView = (ListView)findViewById(R.id.listView);

        ArrayList<TourPlan> list = new ArrayList<>();
        ListOnlineListViewAdapter adapter = new ListOnlineListViewAdapter(ListOnlineActivity.this);
        adapter.setTourPlanList(list);
        listView.setAdapter(adapter);

		// Tweet tweet = new Tweet();
		// tweet.setName("HogeFuga");
		// tweet.setTweet("‚ ‚¢‚¤‚¦‚¨‚©‚«‚­‚¯‚±");
		// list.add(tweet);
		// adapter.notifyDataSetChanged();
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
