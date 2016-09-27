package net.nqlab.btmw.wear.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.nqlab.btmw.api.TourPlanSchedulePoint;

public class MainViewPagerAdapter extends PagerAdapter {
    public static final int PAGE_DIRECTION = 0;
    public static final int PAGE_LABELS = 1;
    public static final int PAGE_DISTANCE = 2;
    public static final int PAGE_ELEVATION = 3;
    public static final int PAGE_SPEED = 4;
    public static final int PAGE_TIME = 5;
    public static final int PAGE_LEFT = 6;
    public static final int PAGE_COUNT = 7;

    /** コンテキスト. */
    private Context mContext;
    private TourPlanSchedulePoint mPoint;
     
    /**
     * コンストラクタ.
     */
    public CustomPagerAdapter(Context context, TourPlanSchedulePoint point) {
        mContext = context;
        mPoint = point;
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TourPlanSchedulePoint point = mPoint;
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView;
        if (position == PAGE_DIRECTION) {
            convertView = layoutInflater.inflate(R.layout.list_item_go_pass,parent,false);

            if (! point.getPass()) {
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

        } else {
            convertView = layoutInflater.inflate(R.layout.list_item_go_junction,parent,false);
            TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);

            switch (position) {
            case PAGE_LABELS:
                textView1.setText(mPoint.getName());
                textView2.setText(mPoint.getComment());
                break;

            case PAGE_DISTANCE:
                break;

            case PAGE_ELEVATION:
                break;

            case PAGE_SPEED:
                break;

            case PAGE_TIME:
                break;

            case PAGE_LEFT:
                break;
            }
        }
 
        // コンテナに追加
        container.addView(textView);
 
        return convertView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // コンテナから View を削除
        container.removeView((View) object);
    }
 
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // Object 内に View が存在するか判定する
        return view == (TextView) object;
    }
}
