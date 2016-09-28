package net.nqlab.btmw.handheld.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import net.nqlab.btmw.api.SerDes;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.TourGo;
import net.nqlab.btmw.view.FormatHelper;

import java.util.List;

public class GoSelectListViewAdapter extends BaseAdapter {
    private Context mContext;
    private SerDes mSerDes;
    private List<TourGo> mGos;
    private int mSelectedIndex;
    private RadioButton mSelectedRadioButton;

    public GoSelectListViewAdapter(Context context, SerDes serDes, List<TourGo> gos) {
        mContext = context;
        mSerDes = serDes;
        mGos = gos;
    }

    public TourGo getSelectedItem() {
        if (mSelectedIndex == 0) {
            return null;
        }
        return mGos.get(mSelectedIndex - 1);
    }

    @Override
    public int getCount() {
        return mGos.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (i == 0) {
            return null;
        } else {
            return mGos.get(i - 1);
        }
    }

    @Override
    public long getItemId(int i) {
        return mGos.get(i)._id;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.list_item_go_set, viewGroup, false);

        if (i == 0) {
            ((TextView) convertView.findViewById(R.id.name)).setText("New");
        } else {
            String formated = FormatHelper.formatTime(mSerDes, mGos.get(i - 1).start_time);
            ((TextView) convertView.findViewById(R.id.name)).setText(formated);
        }

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
