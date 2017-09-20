package direction123.calendar.adapters;

/**
 * Created by fangxiangwang on 9/8/17.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import direction123.calendar.R;
import direction123.calendar.data.DayModel;

public class DayGridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<DayModel> mDays;

    public DayGridAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mDays != null) {
            Log.v("sfefafa", mDays.size() + "");
            return mDays.size();
        }
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (mDays != null && position >= 0 && position < getCount()) {
            return mDays.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mDays != null && position >= 0 && position < getCount()) {
            return 0;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView dayTextView;
        TextView dayLunarTextView;

        if (view == null) {
            view = mInflater.inflate(R.layout.day_grid, parent, false);
            dayTextView = (TextView) view.findViewById(R.id.day);
            dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);
            DayModel dayModel = mDays.get(position);
            if (dayModel != null) {
                dayTextView.setText(dayModel.getDispTop());
                dayLunarTextView.setText(dayModel.getDispShort("English"));
            }
        }
        return view;
    }

    public void setData(List<DayModel> dayModels){
        this.mDays = dayModels;
        notifyDataSetChanged();
    }
}
