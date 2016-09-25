package net.nqlab.btmw.wear.controller;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.TextView;

import net.nqlab.btmw.wear.R;
import net.nqlab.btmw.wear.model.BtmwHandheld;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {
    private BtmwHandheld mHandheld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mHandheld = new BtmwHandheld(this);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandheld.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandheld.disconnect();
    }
}
