package direction123.calendar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import direction123.calendar.R;
import direction123.calendar.data.DayModel;

/**
 * Created by fangxiangwang on 9/20/17.
 */

public class DayGridHeaderAdapter extends BaseAdapter {
    private String[] mDays = new String[]{"S","M","T","W","T","F","S"};
    private LayoutInflater mInflater;

    public DayGridHeaderAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mDays != null) {
            return mDays.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDays != null && position >= 0 && position < getCount()) {
            return mDays[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView dayHeaderTextView;

        if (view == null) {
            view = mInflater.inflate(R.layout.day_header_grid, parent, false);
            dayHeaderTextView = (TextView) view.findViewById(R.id.day_header);
            if (mDays != null && position >= 0 && position < getCount()) {
                dayHeaderTextView.setText(mDays[position]);
            }
        }
        return view;
    }
}
