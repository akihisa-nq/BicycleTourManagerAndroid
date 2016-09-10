package net.nqlab.simple.controller;
 
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.nqlab.simple.R;
import net.nqlab.simple.model.SecureSaveData;
import net.nqlab.simple.model.SaveData;
import net.nqlab.simple.model.BtmwApi;
 
public class BtmwApplication extends Application {
    private SecureSaveData mSecureSaveData = null;
    private SaveData mSaveData;
    private BtmwApi mApi;

    @Override
    public void onCreate() {
		super.onCreate();

        mSecureSaveData = new SecureSaveData();
        mSaveData = new SaveData(this);
        mApi = new BtmwApi(mSecureSaveData, mSaveData);
    }
 
    @Override
    public void onTerminate() {
		super.onTerminate();
    }

    public SecureSaveData getSecureSaveData()
    {
        return mSecureSaveData;
    }

    public SaveData getSaveData()
    {
        return mSaveData;
    }

    public BtmwApi getApi()
    {
        return mApi;
    }

    private NavigationView.OnNavigationItemSelectedListener createNavigationItemSelectedListener(final Activity activity)
    {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_login: {
                        Intent intent = new Intent();
                        intent.setClassName("net.nqlab.simple", "net.nqlab.simple.controller.LoginActivity");
                        activity.startActivity(intent);
                        return true;
                    }
                    case R.id.menu_list_online: {
                        Intent intent = new Intent();
                        intent.setClassName("net.nqlab.simple", "net.nqlab.simple.controller.ListOnlineActivity");
                        activity.startActivity(intent);
                        return true;
                    }
                    case R.id.menu_list_downloaded: {
                        Intent intent = new Intent();
                        intent.setClassName("net.nqlab.simple", "net.nqlab.simple.controller.ListDownloadedActivity");
                        activity.startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public void setupNavigation(AppCompatActivity activity)
    {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                // setTitle("");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                // setTitle("");
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navView = (NavigationView) activity.findViewById(R.id.navigation_header);
        navView.setNavigationItemSelectedListener(createNavigationItemSelectedListener(activity));
    }
}
