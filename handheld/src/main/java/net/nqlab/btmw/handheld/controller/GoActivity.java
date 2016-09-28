package net.nqlab.btmw.handheld.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RadioGroup;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.handheld.controller.BtmwApplication;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.TourPlanScheduleStore;
import net.nqlab.btmw.handheld.model.BtmwWear;
import net.nqlab.btmw.handheld.view.GoListViewAdapter;
import net.nqlab.btmw.handheld.view.GoSetPointListViewAdapter;
import net.nqlab.btmw.handheld.view.GoSetRouteListViewAdapter;

import java.util.List;
import java.util.Date;

public class GoActivity extends AppCompatActivity {
    private TourPlanSchedule mTourPlan;
    private GoListViewAdapter mAdapter;
	private BtmwWear mWear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

		final String start = getBtmwApplication().getApi().fromDateToString(new Date());

        Intent intent = getIntent();
        int tourPlanId = intent.getIntExtra("TourPlan", 0);
        mTourPlan = new TourPlanScheduleStore(
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getSecureSaveData(),
                getBtmwApplication().getApi()
            ).load(tourPlanId);

		mWear = new BtmwWear(this, getBtmwApplication().getApi(), new BtmwWear.BtmwWearListener() {
            @Override
            public void onGoNext() {
                mAdapter.succeed();
                GoActivity.this.goToPoint();
            }

            @Override
            public void onConnect() {
				mWear.setStartTime(start);
				mWear.setBaseTime(mTourPlan.getStartTime());
                mWear.sendPoint(mAdapter.getCurrentPoint());
            }
        });

        final ListView listView = (ListView)findViewById(R.id.listView);
        mAdapter = new GoListViewAdapter(GoActivity.this, getBtmwApplication().getApi(), mTourPlan, start);
        listView.setAdapter(mAdapter);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return super.onSingleTapConfirmed(motionEvent);
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                mAdapter.succeed();
                GoActivity.this.goToPoint();
                return super.onDoubleTap(motionEvent);
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                showSetRouteDialog();
                super.onLongPress(motionEvent);
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        mWear.connect();
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
        mWear.disconnect();

        Window window = getWindow();
        window.clearFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
    }

    @Override
    protected void onResume() {
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

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    private BtmwApplication getBtmwApplication() {
        return (BtmwApplication) getApplicationContext();
    }

    private void showSetRouteDialog() {
        LayoutInflater layoutInflater = (LayoutInflater)GoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View convertView = layoutInflater.inflate(R.layout.dialog_go_set, null, false);

        ListView listView = (ListView)convertView.findViewById(R.id.listView);
        final GoSetRouteListViewAdapter adapter = new GoSetRouteListViewAdapter(this, mTourPlan, mAdapter.getCurrentRoute());
        listView.setAdapter(adapter);
        listView.setSelection(mAdapter.getCurrentRoute());

        new AlertDialog.Builder(GoActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ルートの選択")
                .setView(convertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int positionRoute = adapter.getSelectedItem();
                        GoActivity.this.showSetPointDialog(positionRoute);
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void showSetPointDialog(final int positionRoute) {
        LayoutInflater layoutInflater = (LayoutInflater)GoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View convertView = layoutInflater.inflate(R.layout.dialog_go_set, null, false);

        int position = (positionRoute == mAdapter.getCurrentRoute() ? mAdapter.getCurrentPosition() : 0);
        ListView listView = (ListView)convertView.findViewById(R.id.listView);
        final GoSetPointListViewAdapter adapter = new GoSetPointListViewAdapter(this, mTourPlan, positionRoute, position);
        listView.setAdapter(adapter);
        listView.setSelection(position);

        new AlertDialog.Builder(GoActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ポイントの選択")
                .setView(convertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int positionPoint = adapter.getSelectedItem();
                        mAdapter.setPosition(positionRoute, positionPoint);
                        goToPoint();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void goToPoint() {
        int selection = mAdapter.getCurrentPosition();
        final ListView listView = (ListView)findViewById(R.id.listView);
        listView.setSelection(selection);
        mWear.sendPoint(mAdapter.getCurrentPoint());
    }

}
