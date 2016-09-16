package net.nqlab.simple.controller;

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

import net.nqlab.btmw.TourPlanSchedule;
import net.nqlab.simple.controller.BtmwApplication;
import net.nqlab.simple.R;
import net.nqlab.simple.model.TourPlanScheduleStore;
import net.nqlab.simple.view.GoListViewAdapter;
import net.nqlab.simple.view.GoSetPointListViewAdapter;
import net.nqlab.simple.view.GoSetRouteListViewAdapter;

import java.util.List;

public class GoActivity extends AppCompatActivity {
    private TourPlanSchedule mTourPlan;
    private GoListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        Intent intent = getIntent();
        int tourPlanId = intent.getIntExtra("TourPlan", 0);
        mTourPlan = new TourPlanScheduleStore(
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getSecureSaveData(),
                getBtmwApplication().getApi()
            ).load(tourPlanId);

        final ListView listView = (ListView)findViewById(R.id.listView);
        mAdapter = new GoListViewAdapter(GoActivity.this, getBtmwApplication().getApi(), mTourPlan);
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
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        Window window = getWindow();
        window.clearFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
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
    }

}
