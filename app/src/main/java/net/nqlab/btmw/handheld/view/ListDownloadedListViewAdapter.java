package net.nqlab.btmw.handheld.view;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.handheld.model.SaveData;
import net.nqlab.btmw.handheld.model.SecureSaveData;
import net.nqlab.btmw.handheld.model.TourPlanScheduleStore;
import net.nqlab.btmw.handheld.model.BtmwApi;
import net.nqlab.btmw.handheld.R;

public class ListDownloadedListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<TourPlanSchedule> mTourPlanList;
    private TourPlanScheduleStore mStore;

    public ListDownloadedListViewAdapter(Context context, SaveData saveData, SecureSaveData secureSaveData, BtmwApi api) {
        mContext = context;
        mStore = new TourPlanScheduleStore(saveData, secureSaveData, api);
        mTourPlanList = mStore.getList();
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
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_item_list_downloaded, parent, false);

        TourPlanSchedule plan = mTourPlanList.get(position);
        ((TextView)convertView.findViewById(R.id.name)).setText(plan.getName());
        /*
        ((TextView)convertView.findViewById(R.id.distance)).setText(
                FormatHelper.formatDistance(plan.getDistance())
            );
        ((TextView)convertView.findViewById(R.id.elevation)).setText(
                FormatHelper.formatElevation(plan.getElevation())
            );
        */

        return convertView;
    }
}
