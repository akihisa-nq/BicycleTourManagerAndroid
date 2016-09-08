package net.nqlab.simple.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import net.nqlab.btmw.TourPlan;
import net.nqlab.simple.R;

public class ListOnlineListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater = null;
    private List<TourPlan> mTourPlanList;

    public ListOnlineListViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTourPlanList(List<TourPlan> tourPlanList) {
        mTourPlanList = tourPlanList;
    }

    @Override
    public int getCount() {
        return mTourPlanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTourPlanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTourPlanList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.list_item_list_online,parent,false);

        ((TextView)convertView.findViewById(R.id.name)).setText(mTourPlanList.get(position).getName());
        ((TextView)convertView.findViewById(R.id.distance)).setText(mTourPlanList.get(position).getDistance().toString() + "km");
        ((TextView)convertView.findViewById(R.id.elevation)).setText(mTourPlanList.get(position).getElevation().toString() + "m");

        return convertView;
    }
}
