package net.nqlab.btmw.wear.controller;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import net.nqlab.btmw.wear.R;
import net.nqlab.btmw.wear.model.BtmwHandheld;
import net.nqlab.btmw.api.TourPlanSchedulePoint;

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

        mHandheld = new BtmwHandheld(this, new BtmwHandheld.BtmwHandheldListener() {
			@Override
			public void onSetPoint(TourPlanSchedulePoint point) {
				MainActivity.this.setPoint(point);
			}
		});

        View view = findViewById(R.id.container);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return super.onSingleTapConfirmed(motionEvent);
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                MainActivity.this.mHandheld.requestNextPoint();
                return super.onDoubleTap(motionEvent);
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                super.onLongPress(motionEvent);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
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

	private void setPoint(TourPlanSchedulePoint point) {
        TextView text = (TextView)findViewById(R.id.text);
		text.setText(point.getName());
	}
}
