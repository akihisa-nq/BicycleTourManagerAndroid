package net.nqlab.simple.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.nqlab.btmw.TourPlanSchedule;
import net.nqlab.btmw.TourPlanSchedulePoint;
import net.nqlab.btmw.TourPlanScheduleRoute;
import net.nqlab.simple.R;
import net.nqlab.simple.model.BtmwApi;

public class GoListViewAdapter extends BaseAdapter {

    private Context mContext;
    private TourPlanSchedule mTourPlan;
    private int mTourPlanRouteIndex;
    private int mTourPlanPointIndex;

    public GoListViewAdapter(Context context, TourPlanSchedule plan) {
        mContext = context;
        mTourPlan = plan;
        mTourPlanRouteIndex = 0;
        mTourPlanPointIndex = 0;
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

    @Override
    public int getCount() {
        return getRoute().getTourPlanSchedulePoints().size();
    }

    @Override
    public Object getItem(int position) {
        return getRoute().getTourPlanSchedulePoints().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getRoute().getTourPlanSchedulePoints().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TourPlanSchedulePoint point = getRoute().getTourPlanSchedulePoints().get(position);
        if (point.getPass()) {
            convertView = layoutInflater.inflate(R.layout.list_item_go_pass,parent,false);
        } else {
            convertView = layoutInflater.inflate(R.layout.list_item_go_junction,parent,false);
        }

        if (position == mTourPlanPointIndex) {
            convertView.setBackgroundColor(mContext.getColor(R.color.go_current_point));
        } else {
            convertView.setBackgroundColor(mContext.getColor(R.color.go_not_current_point));
        }

        ((TextView)convertView.findViewById(R.id.name)).setText(point.getName());
        ((TextView)convertView.findViewById(R.id.distance)).setText("+" + point.getDistanceAddition().toString() + "km");
        ((TextView)convertView.findViewById(R.id.elevation)).setText(point.getElevation().toString() + "m");

        return convertView;
    }
}
