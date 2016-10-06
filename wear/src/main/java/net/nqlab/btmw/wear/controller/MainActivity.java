package net.nqlab.btmw.wear.controller;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.wearable.activity.WearableActivity;
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

import net.nqlab.btmw.model.WearProtocol;
import net.nqlab.btmw.wear.R;
import net.nqlab.btmw.wear.model.BtmwHandheld;
import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.wear.view.MainViewPagerAdapter;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity {
    private BtmwHandheld mHandheld;
    private MainViewPagerAdapter mAdapter;
    private Timer mTimer;
    private AudioRecord mRecorder;
    private ByteBuffer mAudioData;
    private int mAudioDataOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

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
            public boolean onDown(MotionEvent e) {
                return true;
            }

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
                return gestureDetector.onTouchEvent(motionEvent);
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
            // AudioRecord作成
            final int SAMPLE_RATE = 8000;
            final int bufferSize = AudioRecord.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            mRecorder = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
            );
			final int HEADER_SIZE = 44;
            mAudioData = ByteBuffer.allocate(HEADER_SIZE + SAMPLE_RATE * 5);
            mAudioDataOffset = HEADER_SIZE;

			byte[] header = {
				0x52, 0x49, 0x46, 0x46, // RIFF
				0x00, 0x00, 0x00, 0x00, // size (resrved)
				0x57, 0x41, 0x56, 0x45, // WAVE
				0x66, 0x6d, 0x74, 0x20, // 'fmt '
				0x0F, 0x00, 0x00, 0x00, // size(16 byte)
				0x01, 0x00,				//
				0x01, 0x00,				//
				0x40, 0x1f, 0x00, 0x00, // 8 kHz
				(byte)0x80, 0x3e, 0x00, 0x00, // 8000 * 2 bytes
				0x02, 0x00,				//
				0x0f, 0x00,				//
				0x64, 0x61, 0x74, 0x61, // 'data'
				0x00, 0x00, 0x00, 0x00, // size (reserved)
			};
			mAudioData.put(header);

            mRecorder.setRecordPositionUpdateListener(new AudioRecord.OnRecordPositionUpdateListener() {
                // フレームごとの処理
                @Override
                public void onPeriodicNotification(AudioRecord recorder) {
                    short[] tmp = new short[bufferSize / 2];
                    recorder.read(tmp, 0, bufferSize / 2);
                    mAudioData.asShortBuffer().put(tmp);
                    mAudioDataOffset += bufferSize;
                    if (mAudioDataOffset >= mAudioData.limit()) {
                        MainActivity.this.recordStop();
                    }
                }

                @Override
                public void onMarkerReached(AudioRecord recorder) {
                }
            });

            mRecorder.setPositionNotificationPeriod(bufferSize / 2);

            // 録音開始
            mRecorder.startRecording();

        } catch (Exception e) {
            mRecorder = null;
            Log.e("Sound", e.getMessage());
        }
    }

    private void recordStop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.setRecordPositionUpdateListener(null);
            mRecorder = null;

			final int totalSize = mAudioDataOffset - 8;
			mAudioData.position(4);
			byte[] headerSizeTotal = {
				(byte)((totalSize >>  0) & 0xFF),
				(byte)((totalSize >>  8) & 0xFF),
				(byte)((totalSize >> 16) & 0xFF),
				(byte)((totalSize >> 24) & 0xFF)
			};
			mAudioData.put(headerSizeTotal);

			final int waveSize = mAudioDataOffset - 44;
			mAudioData.position(40);
			byte[] headerSizeWave = {
				(byte)((waveSize >>  0) & 0xFF),
				(byte)((waveSize >>  8) & 0xFF),
				(byte)((waveSize >> 16) & 0xFF),
				(byte)((waveSize >> 24) & 0xFF)
			};
			mAudioData.put(headerSizeWave);

            if (mAudioDataOffset < mAudioData.limit()) {
                byte[] tmp = new byte[mAudioDataOffset];
                mAudioData.get(tmp, 0, mAudioDataOffset);
                mHandheld.sendSoundData(tmp);
            } else {
                mHandheld.sendSoundData(mAudioData.array());
            }

            mAudioData = null;
        }
    }
}
