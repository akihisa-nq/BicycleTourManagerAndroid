package net.nqlab.btmw.handheld.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import net.nqlab.btmw.api.TourPlan;
import net.nqlab.btmw.api.TourPlanList;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.BtmwApi;
import net.nqlab.btmw.view.FormatHelper;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListOnlineListViewAdapter extends BaseAdapter {

    private Context mContext;
    private BtmwApi mApi;
    private boolean bBusy;
    private List<TourPlan> mTourPlanList;
    private int mOffset;
    private int mTotalCount;

    public enum Direction
    {
        NEWER,
        OLDER
    }

    public ListOnlineListViewAdapter(Context context, BtmwApi api) {
        mContext = context;
        mApi = api;
        mTotalCount = 0;
        mOffset = 0;
        mTourPlanList = new ArrayList<TourPlan>();
    }

    @Override
    public int getCount() {
        return mTotalCount;
    }

    @Override
    public Object getItem(int position) {
        checkItemAt(position);
        int positionList = position - mOffset;
        if (positionList >= mTourPlanList.size()) {
            return -1;
        }
        return mTourPlanList.get(positionList);
    }

    @Override
    public long getItemId(int position) {
        checkItemAt(position);
        int positionList = position - mOffset;
        if (positionList >= mTourPlanList.size()) {
            return -1;
        }
        return mTourPlanList.get(positionList).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        checkItemAt(position);

        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_item_list_online,parent,false);

        TourPlan plan;
        int positionList = position - mOffset;
        if (positionList >= mTourPlanList.size()) {
            plan = new TourPlan();
            plan.setName("");
            plan.setDistance(0);
            plan.setElevation(0);
        } else {
            plan = mTourPlanList.get(positionList);
        }

        ((TextView)convertView.findViewById(R.id.name)).setText(plan.getName());
        ((TextView)convertView.findViewById(R.id.distance)).setText(
                FormatHelper.formatDistance(plan.getDistance())
            );
        ((TextView)convertView.findViewById(R.id.elevation)).setText(
                FormatHelper.formatElevation(plan.getElevation())
            );

        return convertView;
    }

    public int getOffset()
    {
        return mOffset;
    }

    public int getCacheCount()
    {
        return mTourPlanList.size();
    }

    public void readMore(final Direction dir, int count, boolean force) {
        if (!force) {
            if ((dir == Direction.OLDER && mOffset == 0)
                || (dir == Direction.NEWER && mOffset + mTourPlanList.size() == mTotalCount)
                )
            {
                return;
            }
        }

        if (bBusy) {
            return;
        }
        bBusy = true;

        int offset = 0;
        if ( dir == Direction.OLDER ) {
            offset = mOffset - count;
        } else {
            offset = mOffset + mTourPlanList.size();
        }

        mApi.getTourPlanApi().list(offset, count)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<TourPlanList>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    bBusy = false;
                }

                @Override
                public void onNext(TourPlanList tourPlanList) {
                    if (mOffset > tourPlanList.getOffset()) {
                        mTourPlanList.addAll(
                            0,
                            tourPlanList.getTourPlans()
                            );
                    } else {
                        mTourPlanList.addAll(tourPlanList.getTourPlans());
                    }

                    mTotalCount = tourPlanList.getTotalCount();

                    ListOnlineListViewAdapter.this.notifyDataSetChanged();

                    bBusy = false;
                }
            });
    }

    private void checkItemAt(int position)
    {
        if (position < mOffset) {
            readMore(Direction.OLDER, 10, false);
        } else if (mOffset + mTourPlanList.size() < position) {
            readMore(Direction.NEWER, 10, false);
        }
    }
}
