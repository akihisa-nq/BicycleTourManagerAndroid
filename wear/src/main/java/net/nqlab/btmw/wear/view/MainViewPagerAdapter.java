package net.nqlab.btmw.wear.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.nqlab.btmw.api.SerDes;
import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.view.FormatHelper;
import net.nqlab.btmw.view.GoDirectionView;
import net.nqlab.btmw.wear.R;

public class MainViewPagerAdapter extends PagerAdapter {
    public static final int PAGE_DIRECTION = 0;
    public static final int PAGE_LABELS = 1;
    public static final int PAGE_DISTANCE_ELEVATION_LIMIT = 2;
    public static final int PAGE_SPEED_LEFT = 3;
    public static final int PAGE_COUNT = 4;

    /** コンテキスト. */
    private Context mContext;
    private TourPlanSchedulePoint mPointPrevious;
    private TourPlanSchedulePoint mPoint;
    private SerDes mSerDes;
	private String mBaseTime;
	private String mStartTime;

    /**
     * コンストラクタ.
     */
    public MainViewPagerAdapter(Context context, SerDes serDes) {
        mContext = context;
        mPoint = null;
        mSerDes = serDes;
    }

    public void setPoint(TourPlanSchedulePoint pointPrevious, TourPlanSchedulePoint pointCurrent) {
        mPointPrevious = pointPrevious;
        mPoint = pointCurrent;
        notifyDataSetChanged();
    }

    public void setBaseTime(String time) {
		mBaseTime = time;
    }

    public void setStartTime(String time) {
		mStartTime = time;
    }

    private void setDirectionView(View convertView, TourPlanSchedulePoint point) {
        ((TextView)convertView.findViewById(R.id.text_view_nw)).setText(point.getRoadNw());
        ((TextView)convertView.findViewById(R.id.text_view_n )).setText(point.getRoadN() );
        ((TextView)convertView.findViewById(R.id.text_view_ne)).setText(point.getRoadNe());
        ((TextView)convertView.findViewById(R.id.text_view_w )).setText(point.getRoadW() );
        ((TextView)convertView.findViewById(R.id.text_view_e )).setText(point.getRoadE() );
        ((TextView)convertView.findViewById(R.id.text_view_sw)).setText(point.getRoadSw());
        ((TextView)convertView.findViewById(R.id.text_view_s )).setText(point.getRoadS() );
        ((TextView)convertView.findViewById(R.id.text_view_se)).setText(point.getRoadSe());

        GoDirectionView direction = ((GoDirectionView)convertView.findViewById(R.id.direction));
        direction.setRoadNw(!point.getRoadNw().isEmpty());
        direction.setRoadN( !point.getRoadN().isEmpty() );
        direction.setRoadNe(!point.getRoadNe().isEmpty());
        direction.setRoadW( !point.getRoadW().isEmpty() );
        direction.setRoadE( !point.getRoadE().isEmpty() );
        direction.setRoadSw(!point.getRoadSw().isEmpty());
        direction.setRoadS( !point.getRoadS().isEmpty() );
        direction.setRoadSe(!point.getRoadSe().isEmpty());
        direction.setSource(point.getSource());
        direction.setDestination(point.getDestination());
    }

    private void setDirectionViewPass(View convertView) {
        ((TextView)convertView.findViewById(R.id.text_view_nw)).setText("");
        ((TextView)convertView.findViewById(R.id.text_view_n )).setText("通過");
        ((TextView)convertView.findViewById(R.id.text_view_ne)).setText("");
        ((TextView)convertView.findViewById(R.id.text_view_w )).setText("");
        ((TextView)convertView.findViewById(R.id.text_view_e )).setText("");
        ((TextView)convertView.findViewById(R.id.text_view_sw)).setText("");
        ((TextView)convertView.findViewById(R.id.text_view_s )).setText("通過");
        ((TextView)convertView.findViewById(R.id.text_view_se)).setText("");

        GoDirectionView direction = ((GoDirectionView)convertView.findViewById(R.id.direction));
        direction.setRoadNw(false);
        direction.setRoadN(true);
        direction.setRoadNe(false);
        direction.setRoadW(false);
        direction.setRoadE(false);
        direction.setRoadSw(false);
        direction.setRoadS(true);
        direction.setRoadSe(false);
        direction.setSource("N");
        direction.setDestination("S");
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mPoint == null) {
            return null;
        }

        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView;
        if (position == PAGE_DIRECTION) {
            convertView = layoutInflater.inflate(R.layout.page_main_direction, container, false);
            if (mPoint.getPass()) {
				setDirectionViewPass(convertView);			
			} else {
                setDirectionView(convertView, mPoint);
            }
        } else if (position == PAGE_LABELS) {
            convertView = layoutInflater.inflate(R.layout.page_main_texts, container, false);
            TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);

            textView1.setText(mPoint.getName());
            textView2.setText(mPoint.getComment() + FormatHelper.formatRestComment(mPoint.getRestTime()));

        } else {
            convertView = layoutInflater.inflate(R.layout.page_main_texts4, container, false);
            TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
            TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
            TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);

            switch (position) {
            case PAGE_DISTANCE_ELEVATION_LIMIT:
                textView1.setText("+" + FormatHelper.formatDistance(mPoint.getDistanceAddition()));
                textView2.setText(FormatHelper.formatElevation(mPoint.getElevation()));
                textView3.setText(FormatHelper.formatDistance(mPoint.getPcTotalDistance()));
                textView4.setText(FormatHelper.formatTime(mSerDes, mPoint.getTotalLimitTime()));
                break;

            case PAGE_SPEED_LEFT:
                {
                    textView1.setText(FormatHelper.formatSpeed(mPointPrevious.getTargetSpeed()));

                    int leftTimeTarget = FormatHelper.getLeftTimeSeconds(
                            mSerDes,
                            mBaseTime,
                            mPoint.getTotalTargetTime(),
                            mStartTime
                    );
                    textView2.setText(FormatHelper.formatTimeAddition(leftTimeTarget));
                    textView2.setTextColor(FormatHelper.getColorForLeftTime(mContext, leftTimeTarget));

                    textView3.setText(FormatHelper.formatSpeed(mPointPrevious.getLimitSpeed()));

                    int leftTimeLimit = FormatHelper.getLeftTimeSeconds(
                            mSerDes,
                            mBaseTime,
                            mPoint.getTotalLimitTime(),
                            mStartTime
                    );
                    textView4.setText(FormatHelper.formatTimeAddition(leftTimeLimit));
                    textView4.setTextColor(FormatHelper.getColorForLeftTime(mContext, leftTimeLimit));
                }
                break;

            }
        }
 
        // コンテナに追加
        container.addView(convertView);
 
        return convertView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // コンテナから View を削除
        container.removeView((View) object);
    }
 
    @Override
    public int getCount() {
        if (mPoint == null) {
            return 0;
        } else {
            return PAGE_COUNT;
        }
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // Object 内に View が存在するか判定する
        return view == (View) object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
