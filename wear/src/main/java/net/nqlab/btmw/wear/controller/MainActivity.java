package net.nqlab.btmw.wear.controller;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.MemoryFile;
import android.support.v4.view.ViewPager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import net.nqlab.btmw.model.MemoryFileUtil;
import net.nqlab.btmw.model.WearProtocol;
import net.nqlab.btmw.wear.R;
import net.nqlab.btmw.wear.model.BtmwHandheld;
import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.wear.view.MainViewPagerAdapter;

import java.io.FileDescriptor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity {
    private BtmwHandheld mHandheld;
    private MainViewPagerAdapter mAdapter;
    private Timer mTimer;
    private MediaRecorder mRecorder;
    private MemoryFile mRecordFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mRecorder = null;
        mRecordFile = null;

        mHandheld = new BtmwHandheld(this, new BtmwHandheld.BtmwHandheldListener() {
			@Override
			public void onSetPoint(String base, String start, TourPlanSchedulePoint pointPrevious, TourPlanSchedulePoint pointCurrent, int pointType) {
                MainActivity.this.mAdapter.setBaseTime(base);
                MainActivity.this.mAdapter.setStartTime(start);
                MainActivity.this.mAdapter.setPoint(pointPrevious, pointCurrent);

                int id = 0;
                switch (pointType) {
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_START:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_start;
                        break;
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_GOAL:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_goal;
                        break;
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_CONTROL_POINT_START:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_control_point_start;
                        break;
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_CONTROL_POINT_GOAL:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_control_point_goal;
                        break;
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_PASS:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_pass;
                        break;
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_BOTTOM:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_bottom;
                        break;
                    case WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE_WAY:
                        id = net.nqlab.btmw.R.mipmap.ic_nav_way_point;
                        break;
                }

                ImageView bg = (ImageView) MainActivity.this.findViewById(R.id.imageView);
                bg.setImageResource(id);
			}
		});
        mAdapter = new MainViewPagerAdapter(this, mHandheld.getSerDes());

        final ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                MainActivity.this.recordStop();
                return super.onSingleTapConfirmed(motionEvent);
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                MainActivity.this.mHandheld.requestNextPoint();
                return super.onDoubleTap(motionEvent);
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                MainActivity.this.openContextMenu(pager);
                super.onLongPress(motionEvent);
            }
        });
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        registerForContextMenu(pager);

        pager.setAdapter(mAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                TextView view = (TextView)MainActivity.this.findViewById(R.id.textView);

                String text = "";
                for (int i = 0; i < position; i++) {
                    text += "○";
                }

                text += "●";

                for (int i = position + 1; i < mAdapter.getCount(); i++) {
                    text += "○";
                }

                view.setText(text);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Window window = getWindow();
        window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        );

        final Handler handler = new Handler();
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 30 * 1000, 30 * 1000);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Window window = getWindow();
        window.clearFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        );

        mTimer.cancel();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        int id = view.getId();
        switch (id) {
            case R.id.viewPager:
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_record_sound:
                recordStart();
                return true;

            case R.id.menu_main_remember_text:
                return true;

            case R.id.menu_main_mark_point:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void recordStart() {
        try {
            mRecordFile = new MemoryFile(null, 512 * 1024);

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setOutputFile(MemoryFileUtil.getFileDescriptor(mRecordFile));

            //録音準備＆録音開始
            mRecorder.prepare();
            mRecorder.start();   //録音開始

        } catch (Exception e) {
            if (mRecordFile != null) {
                mRecordFile.close();
                mRecordFile = null;
            }
            mRecorder = null;

            Log.e("Sound", e.getMessage())
        }
    }

    private void recordStop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();   //オブジェクトのリセット
            //release()前であればsetAudioSourceメソッドを呼び出すことで再利用可能
            mRecorder.release(); //Recorderオブジェクトの解放
            mRecorder = null;

            try {
                byte[] data = new byte[mRecordFile.length()];
                mRecordFile.readBytes(data, 0, 0, data.length);
                mRecordFile.close();

                mHandheld.sendSoundData(data);
            } catch (Exception e) {
            }

            mRecordFile = null;
        }
    }
}
