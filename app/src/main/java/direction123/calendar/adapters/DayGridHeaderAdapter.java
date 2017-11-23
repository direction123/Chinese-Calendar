package direction123.calendar.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
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
    private String[] mDays;
    private Context mContext;
    private LayoutInflater mInflater;


    public DayGridHeaderAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        buildDays();
    }

    public void refreshUI() {
        //language preference
        buildDays();
        notifyDataSetChanged();
    }

    private void buildDays() {
        //language preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String langPref = sharedPref.getString(mContext.getResources().getString(R.string.pref_lang_key), "");
        if (langPref.equals("Simplified Chinese")) {
            mDays = new String[]{"日","一","二","三","四","五","六"};
        } else {
            mDays = new String[]{"S","M","T","W","T","F","S"};
        }
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
