package net.nqlab.simple;

import net.nqlab.btmw.TourPlan;

public class ListOnlineListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<TourPlan> mTourPlanList;

    public ListOnlineListViewAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTourPlanList(ArrayList<TourPlan> tourPlanList) {
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
        convertView = layoutInflater.inflate(R.layout.list_item_list_online,parent,false);

        ((TextView)convertView.findViewById(R.id.name)).setText(mTourPlanList.get(position).getName());
        // ((TextView)convertView.findViewById(R.id.tweet)).setText(mTourPlanList.get(position).getTweet());

        return convertView;
    }
}
