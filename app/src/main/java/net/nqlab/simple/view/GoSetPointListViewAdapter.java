package net.nqlab.simple.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import net.nqlab.btmw.TourPlan;
import net.nqlab.btmw.TourPlanSchedule;
import net.nqlab.btmw.TourPlanSchedulePoint;
import net.nqlab.btmw.TourPlanScheduleRoute;
import net.nqlab.simple.R;

public class GoSetPointListViewAdapter extends BaseAdapter {
    private Context mContext;
    private TourPlanSchedule mTourPlan;
    private int mTourPlanRouteIndex;
    private int mSelectedIndex;
    private RadioButton mSelectedRadioButton;

    public GoSetPointListViewAdapter(Context context, TourPlanSchedule plan, int indexRoute, int indexPoint) {
        mContext = context;
        mTourPlan = plan;
        mTourPlanRouteIndex = indexRoute;
        mSelectedIndex = indexPoint;
        mSelectedRadioButton = null;
    }

    private TourPlanScheduleRoute getRoute() {
        return mTourPlan.getTourPlanSchedules().getTourPlanScheduleRoutes().get(mTourPlanRouteIndex);
    }

    public int getSelectedItem() {
        return mSelectedIndex;
    }

    @Override
    public int getCount() {
        return getRoute().getTourPlanSchedulePoints().size();
    }

    @Override
    public Object getItem(int i) {
        return getRoute().getTourPlanSchedulePoints().get(i);
    }

    @Override
    public long getItemId(int i) {
        return getRoute().getTourPlanSchedulePoints().get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.list_item_go_set, viewGroup, false);

        TourPlanSchedulePoint point = (TourPlanSchedulePoint) getItem(i);

        ((TextView)convertView.findViewById(R.id.name)).setText(i + ": " + point.getName());

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
        }

        return convertView;
    }
}
