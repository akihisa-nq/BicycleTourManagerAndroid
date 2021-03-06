package net.nqlab.btmw.handheld.view;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.api.TourPlanScheduleRoute;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.BtmwApi;
import net.nqlab.btmw.view.FormatHelper;
import net.nqlab.btmw.view.GoDirectionView;

public class GoListViewAdapter extends BaseAdapter {

    private Context mContext;
    private BtmwApi mApi;
    private TourPlanSchedule mTourPlan;
    private int mTourPlanRouteIndex;
    private int mTourPlanPointIndex;
	private String mStartDate;

    public GoListViewAdapter(Context context, BtmwApi api, TourPlanSchedule plan) {
        mContext = context;
        mApi = api;
        mTourPlan = plan;
        mTourPlanRouteIndex = 0;
        mTourPlanPointIndex = 0;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    private TourPlanScheduleRoute getRoute() {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(mTourPlanRouteIndex);
    }

    public void succeed() {
        if (mTourPlanPointIndex + 1 == getCount()) {
            if (mTourPlanRouteIndex + 1 == mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().size()) {
                return;
            }

            mTourPlanPointIndex = 0;
            ++ mTourPlanRouteIndex;
        } else {
            ++ mTourPlanPointIndex;
        }
        notifyDataSetChanged();
    }

    public int getCurrentRoute() {
        return mTourPlanRouteIndex;
    }

    public int getCurrentPosition() {
        return mTourPlanPointIndex;
    }

    public void setPosition(int positionRoute, int positionPoint) {
        mTourPlanRouteIndex = positionRoute;
        mTourPlanPointIndex = positionPoint;
        notifyDataSetChanged();
    }

	public TourPlanSchedulePoint getPreviousPoint() {
		TourPlanSchedulePoint prev = null;
		if (getCurrentPosition() == 0) {
			prev = getCurrentPoint();
		} else {
			prev = (TourPlanSchedulePoint)getItem(getCurrentPosition() - 1);
		}
		return prev;
	}

	public TourPlanSchedulePoint getCurrentPoint() {
        return getRoute().getTourPlanSchedulePoints().get(mTourPlanPointIndex);	
	}

    @Override
    public int getCount() {
        return getRoute().getTourPlanSchedulePoints().size();
    }

    @Override
    public TourPlanSchedulePoint getItem(int position) {
        return getRoute().getTourPlanSchedulePoints().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getRoute().getTourPlanSchedulePoints().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TourPlanSchedulePoint pointPrevious = getPreviousPoint();
        TourPlanSchedulePoint point = getCurrentPoint();
        if (point.getPass()) {
            convertView = layoutInflater.inflate(R.layout.list_item_go_pass,parent,false);
        } else {
            convertView = layoutInflater.inflate(R.layout.list_item_go_junction,parent,false);
        }

        TypedValue outValue = new TypedValue();
        if (position == mTourPlanPointIndex) {
            mContext.getTheme().resolveAttribute(
                    android.R.attr.colorControlHighlight,
                    outValue,
                    true
            );
        } else {
            mContext.getTheme().resolveAttribute(
                    android.R.attr.colorControlNormal,
                    outValue,
                    true
            );
        }
        convertView.setBackgroundColor(outValue.data);

        if (! point.getPass()) {
            // 道
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

        // 名前とコメント
        ((TextView)convertView.findViewById(R.id.name)).setText(point.getName());
        ((TextView)convertView.findViewById(R.id.comment)).setText(
                point.getComment() + FormatHelper.formatRestComment(point.getRestTime())
        );

        // 距離の加算と高度
        ((TextView)convertView.findViewById(R.id.distance_addition)).setText(
                "+" + FormatHelper.formatDistance(point.getDistanceAddition())
            );
        ((TextView)convertView.findViewById(R.id.elevation)).setText(
                FormatHelper.formatElevation(point.getElevation())
            );

        // 速度
        ((TextView)convertView.findViewById(R.id.target_speed)).setText(
                FormatHelper.formatSpeed(pointPrevious.getTargetSpeed())
            );
        ((TextView)convertView.findViewById(R.id.limit_speed)).setText(
                FormatHelper.formatSpeed(pointPrevious.getLimitSpeed())
            );

        // 時間の加算
		{
			int leftTimeTarget = FormatHelper.getLeftTimeSeconds(
				mApi.getSerDes(),
				mTourPlan.getStartTime(),
				point.getTotalTargetTime(),
				mStartDate
				);
			TextView textView1 = ((TextView)convertView.findViewById(R.id.target_time_addition));
			textView1.setText(FormatHelper.formatTimeAddition(leftTimeTarget));
			textView1.setTextColor(FormatHelper.getColorForLeftTime(mContext, leftTimeTarget));

			int leftTimeLimit = FormatHelper.getLeftTimeSeconds(
				mApi.getSerDes(),
				mTourPlan.getStartTime(),
				point.getTotalLimitTime(),
				mStartDate
				);
			TextView textView2 = ((TextView)convertView.findViewById(R.id.limit_time_addition));
			textView2.setText(FormatHelper.formatTimeAddition(leftTimeLimit));
			textView2.setTextColor(FormatHelper.getColorForLeftTime(mContext, leftTimeLimit));
		}

        if (! point.getPass()) {
            // PC からの総距離と時間のリミット
            ((TextView) convertView.findViewById(R.id.pc_total_distance)).setText(
                    FormatHelper.formatDistance(point.getPcTotalDistance())
                    );
            ((TextView) convertView.findViewById(R.id.total_limit_time)).setText(
                    FormatHelper.formatTime(mApi.getSerDes(), point.getTotalLimitTime())
                );
        }

        return convertView;
    }
}
