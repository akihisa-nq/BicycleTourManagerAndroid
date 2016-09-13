package net.nqlab.simple.view;

import android.content.Context;
import android.media.Image;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.nqlab.btmw.TourPlanSchedule;
import net.nqlab.btmw.TourPlanSchedulePoint;
import net.nqlab.btmw.TourPlanScheduleRoute;
import net.nqlab.simple.model.BtmwApi;
import net.nqlab.simple.R;

public class ShowDownloadedExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private TourPlanSchedule mTourPlan;

    public ShowDownloadedExpandableListViewAdapter(
            Context context,
            TourPlanSchedule plan) {
        mContext = context;
        mTourPlan = plan;
    }

    private TourPlanScheduleRoute getRoute(int position) {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(position);
    }

    @Override
    public int getGroupCount() {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getRoute(groupPosition).getTourPlanSchedulePoints().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return getRoute(groupPosition).getTourPlanSchedulePoints().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getRoute(groupPosition).getTourPlanSchedulePoints().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.list_item_show_downloaded_route, parent, false);

        TourPlanScheduleRoute route = mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(groupPosition);
        ((TextView)convertView.findViewById(R.id.name)).setText(route.getName());
        // ((TextView)convertView.findViewById(R.id.distance)).setText(plan.getDistance().toString() + "km");
        // ((TextView)convertView.findViewById(R.id.elevation)).setText(plan.getElevation().toString() + "m");

        ImageView expandableIcon = (ImageView)convertView.findViewById(R.id.imageView);
        if (isExpanded) {
            expandableIcon.setImageResource(R.drawable.ic_expand_less_white_24dp);
        } else {
            expandableIcon.setImageResource(R.drawable.ic_expand_more_white_24dp);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.list_item_show_downloaded_point, parent, false);

        TourPlanSchedulePoint point = getRoute(groupPosition).getTourPlanSchedulePoints().get(childPosition);
        ((TextView)convertView.findViewById(R.id.name)).setText(point.getName());
        ((TextView)convertView.findViewById(R.id.distance)).setText(
                "+" + FormatHelper.formatDistance(point.getDistanceAddition())
            );
        ((TextView)convertView.findViewById(R.id.elevation)).setText(
                FormatHelper.formatElevation(point.getElevation())
            );

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return groupPosition < getGroupCount() && childPosition < getChildrenCount(groupPosition);
    }
}
