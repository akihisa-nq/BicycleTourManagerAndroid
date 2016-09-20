package net.nqlab.btmw.handheld.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.api.TourPlanScheduleRoute;
import net.nqlab.btmw.handheld.R;

public class GoSetRouteListViewAdapter extends BaseAdapter {
    private Context mContext;
    private TourPlanSchedule mTourPlan;
    private int mSelectedIndex;
    private RadioButton mSelectedRadioButton;

    public GoSetRouteListViewAdapter(Context context, TourPlanSchedule plan, int index) {
        mContext = context;
        mTourPlan = plan;
        mSelectedIndex = index;
        mSelectedRadioButton = null;
    }

    public int getSelectedItem() {
        return mSelectedIndex;
    }

    @Override
    public int getCount() {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().size();
    }

    @Override
    public Object getItem(int i) {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(i);
    }

    @Override
    public long getItemId(int i) {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.list_item_go_set, viewGroup, false);

        TourPlanScheduleRoute route = (TourPlanScheduleRoute) getItem(i);

        ((TextView)convertView.findViewById(R.id.name)).setText(i + ": " + route.getName());

        RadioButton radioButton = (RadioButton)convertView.findViewById(R.id.radioButton);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i != mSelectedIndex && mSelectedRadioButton != null){
                    mSelectedRadioButton.setChecked(false);
                }

                mSelectedIndex = i;
                mSelectedRadioButton = (RadioButton)view;
            }
        });
        if (i == mSelectedIndex) {
            radioButton.setChecked(true);
            mSelectedRadioButton = radioButton;
        }

        return convertView;
    }
}
