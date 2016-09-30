package net.nqlab.btmw.handheld.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.nqlab.btmw.api.SerDes;
import net.nqlab.btmw.api.TourPlanSchedule;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.SaveData;
import net.nqlab.btmw.handheld.model.TourGo;
import net.nqlab.btmw.view.FormatHelper;

import org.w3c.dom.Text;

import java.util.List;

public class ListLocalGoListViewAdapter extends BaseAdapter{
    private final Context mContext;
    private final SaveData mSaveData;
    private final SerDes mSerDes;
    private List<TourGo> mListTourGo;
    private OnButtonListener mOnButtonListener;

    public interface OnButtonListener {
        void onUploadClicked(View v, TourGo go);
        void onDeleteClicked(View v, TourGo go);
    }

    public ListLocalGoListViewAdapter(Context context, SaveData saveData, SerDes serDes) {
        mContext = context;
        mListTourGo = saveData.getListTourGo();
        mSaveData = saveData;
        mSerDes = serDes;
        mOnButtonListener = null;
    }

    public void setOnButtonListener(OnButtonListener listener) {
        mOnButtonListener = listener;
    }

    public void updateList() {
        mListTourGo = mSaveData.getListTourGo();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListTourGo.size();
    }

    @Override
    public Object getItem(int position) {
        return mListTourGo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListTourGo.get(position)._id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_item_list_local_go, parent, false);

        final TourGo go = mListTourGo.get(position);
        String name = mSaveData.getTourPlanScheduleName((int)go.tour_plan_shedule_id);

        TextView text = (TextView)convertView.findViewById(R.id.textView);
        text.setText(name + " " + FormatHelper.formatTime(mSerDes, go.start_time));

        Button upload = (Button)convertView.findViewById(R.id.buttonUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonListener != null) {
                    mOnButtonListener.onUploadClicked(v, go);
                }
            }
        });

        Button delete = (Button)convertView.findViewById(R.id.buttonDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonListener != null) {
                    mOnButtonListener.onDeleteClicked(v, go);
                }
            }
        });

        return convertView;
    }
}
