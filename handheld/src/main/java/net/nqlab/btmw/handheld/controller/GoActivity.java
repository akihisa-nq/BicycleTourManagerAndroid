package net.nqlab.btmw.handheld.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.model.WearProtocol;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.BtmwApi;
import net.nqlab.btmw.handheld.model.TourGoPassPoint;
import net.nqlab.btmw.handheld.view.GoSelectListViewAdapter;
import net.nqlab.btmw.handheld.model.TourGo;
import net.nqlab.btmw.handheld.model.TourPlanScheduleStore;
import net.nqlab.btmw.handheld.model.BtmwWear;
import net.nqlab.btmw.handheld.view.GoListViewAdapter;
import net.nqlab.btmw.handheld.view.GoSetPointListViewAdapter;
import net.nqlab.btmw.handheld.view.GoSetRouteListViewAdapter;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoActivity extends AppCompatActivity {
    private TourPlanSchedule mTourPlan;
    private GoListViewAdapter mAdapter;
	private BtmwWear mWear;
    private Timer mTimer;
    private TourGo mGo;
    private boolean mSendFirstData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go);

        mSendFirstData = false;
        mGo = null;

        Intent intent = getIntent();
        int tourPlanId = intent.getIntExtra("TourPlan", 0);
        mTourPlan = new TourPlanScheduleStore(
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getSecureSaveData(),
                getBtmwApplication().getApi()
        ).load(tourPlanId);

        if (getBtmwApplication().getSaveData().getTourGoCount() == 0) {
            showSetStartTimeDialog();
        } else {
            showSelectStartTimeDialog();
        }

		mWear = new BtmwWear(this, getBtmwApplication().getApi(), new BtmwWear.BtmwWearListener() {
            @Override
            public void onGoNext() {
                GoActivity.this.succeedPoint();
            }

            @Override
            public void onConnect() {
                mWear.setBaseTime(mTourPlan.getStartTime());

                if (mGo != null) {
					GoActivity.this.sendPoint();
                    mSendFirstData = true;
                }
            }

            @Override
            public void onSoundRecorded(String date, byte[] data) {
                // FIXME
                Log.d("Sound", date + " : " + data.length);
            }
        });

        mAdapter = new GoListViewAdapter(GoActivity.this, getBtmwApplication().getApi(), mTourPlan);

        final ListView listView = (ListView)findViewById(R.id.listView);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return super.onSingleTapConfirmed(motionEvent);
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                GoActivity.this.succeedPoint();
                return super.onDoubleTap(motionEvent);
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                GoActivity.this.openContextMenu(listView);
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
        registerForContextMenu(listView);

        Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        mWear.connect();

        final Handler handler = new Handler();
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GoActivity.this.mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 30 * 1000, 30 * 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        int id = view.getId();
        switch (id) {
        case R.id.listView:
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_go, menu);
            break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_go_jump:
                showSetRouteDialog();
                return true;

        case R.id.menu_go_upload:
            getBtmwApplication().getApi().uploadTourGo(mGo, new BtmwApi.UploadTourGoListerner() {
                @Override
                public void onBegin() {
                    item.setTitle("アップロード中...");
                }

                @Override
                public void onError() {
                    item.setTitle("アップロード失敗");
                }

                @Override
                public void onDone() {
                    item.setTitle("アップロード");
                }
            });
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWear.disconnect();
        mTimer.cancel();

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

    private void showSetStartTimeDialog() {
        LayoutInflater layoutInflater = (LayoutInflater)GoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.dialog_go_time_picker, null, false);

        final DatePicker pickerDate = (DatePicker)convertView.findViewById(R.id.datePicker);
        final TimePicker pickerTime = (TimePicker)convertView.findViewById(R.id.timePicker);

        {
            DateTime nowDateTime = new DateTime(new Date());
            pickerDate.updateDate(nowDateTime.getYear(), nowDateTime.getMonthOfYear() - 1, nowDateTime.getDayOfMonth());
            pickerTime.setHour(nowDateTime.hourOfDay().get());
            pickerTime.setMinute(nowDateTime.minuteOfHour().get());
        }

        final ListView listView = (ListView)findViewById(R.id.listView);

        new AlertDialog.Builder(GoActivity.this)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("時刻設定")
                .setView(convertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Date custom = new DateTime(
                                pickerDate.getYear(),
                                pickerDate.getMonth() + 1,
                                pickerDate.getDayOfMonth(),
                                pickerTime.getHour(),
                                pickerTime.getMinute()
                                ).toDate();

                        mGo = new TourGo();
                        mGo.tour_go_id = null;
                        mGo.tour_plan_shedule_id = mTourPlan.getId();
                        mGo.start_time = getBtmwApplication().getApi().fromDateToString(custom);
                        getBtmwApplication().getSaveData().addTourGo(mGo);

                        mAdapter.setStartDate(mGo.start_time);
                        mWear.setStartTime(mGo.start_time);
                        listView.setAdapter(mAdapter);

                        if (!mSendFirstData && mWear.isConnected()) {
							GoActivity.this.sendPoint();
                            mSendFirstData = true;
                        }
                    }
                })
                .show();
    }

    private void showSelectStartTimeDialog() {
        LayoutInflater layoutInflater = (LayoutInflater)GoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View convertView = layoutInflater.inflate(R.layout.dialog_go_set, null, false);

        ListView listView = (ListView)convertView.findViewById(R.id.listView);
        final GoSelectListViewAdapter adapter = new GoSelectListViewAdapter(
                this,
                getBtmwApplication().getApi().getSerDes(),
                getBtmwApplication().getSaveData().getListTourGoOfSpecifiedTourPlan(mTourPlan.getId())
                );
        listView.setAdapter(adapter);
        listView.setSelection(0);

        final ListView listViewSchedule = (ListView)findViewById(R.id.listView);

        new AlertDialog.Builder(GoActivity.this)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Go の選択")
                .setView(convertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        GoActivity.this.mGo = adapter.getSelectedItem();
                        if (mGo == null) {
                            GoActivity.this.showSetStartTimeDialog();
                        } else {
                            mAdapter.setStartDate(mGo.start_time);
                            mWear.setStartTime(mGo.start_time);
                            listViewSchedule.setAdapter(mAdapter);

                            if (!mSendFirstData && mWear.isConnected()) {
								GoActivity.this.sendPoint();
                                mSendFirstData = true;
                            }
                        }
                    }
                })
                .show();
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
		GoActivity.this.sendPoint();
    }

    private void succeedPoint() {
        TourGoPassPoint passPoint = new TourGoPassPoint();
        passPoint.tour_go_id = mGo._id;
        passPoint.passed_on = getBtmwApplication().getApi().fromDateToString(new Date());
        passPoint.tour_plan_point_id = mAdapter.getCurrentPoint().getId();
        getBtmwApplication().getSaveData().addTourGoPassPoint(passPoint);

        mAdapter.succeed();
        goToPoint();
    }

	private void sendPoint() {
		int pointType = 0;
		if (mAdapter.getCurrentPosition() == 0) {
			if (mAdapter.getCurrentRoute() == 0) {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_START;
			} else {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_CONTROL_POINT_START;
			}
		} else if (mAdapter.getCurrentPosition() == mAdapter.getCount() - 1) {
			if (mAdapter.getCurrentRoute() == mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().size() - 1) {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_GOAL;
			} else {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_CONTROL_POINT_GOAL;
			}
		} else if (mAdapter.getCurrentPoint().getPass()) {
			if (mAdapter.getCurrentPoint().getComment().contains("▲")) {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_PASS;
			} else if (mAdapter.getCurrentPoint().getComment().contains("▼")) {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_BOTTOM;
			} else {
				pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_WAY;
			}
		} else {
			pointType = WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_WAY;
		}

        mWear.sendPoint(mAdapter.getPreviousPoint(), mAdapter.getCurrentPoint(), pointType);
	}
}
