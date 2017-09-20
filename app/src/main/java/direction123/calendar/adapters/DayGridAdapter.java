package direction123.calendar.adapters;

/**
 * Created by fangxiangwang on 9/8/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import direction123.calendar.R;
import direction123.calendar.ViewPagerFragment;
import direction123.calendar.data.DayModel;

public class DayGridAdapter extends BaseAdapter {
    private static final int DAYS_LENGTH = 42;
    private LayoutInflater mInflater;
    private Cursor mCursor;
    private int mFirstDay;
    private int mLastDay;
    private List<DayModel> mDayModels = new ArrayList<>();

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        buildDayModels();
        notifyDataSetChanged();
    }
    public DayGridAdapter(Context context, int firstDay, int lastDay) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFirstDay = firstDay;
        mLastDay = lastDay;
    }

    @Override
    public int getCount() {
        if (mDayModels != null) {
            return mDayModels.size();
        }
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (mDayModels != null && position >= 0 && position < getCount()) {
            return mDayModels.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mDayModels != null && position >= 0 && position < getCount()) {
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
            if (mDayModels != null && position >= 0 && position < getCount() && mDayModels.get(position) != null) {
                DayModel dayModel = mDayModels.get(position);
                dayTextView.setText(dayModel.getDispTop());
                dayLunarTextView.setText(dayModel.getDispShort("English"));
            }
        }
        return view;
    }

    public void setData(List<DayModel> dayModels){
        this.mDayModels = dayModels;
        notifyDataSetChanged();
    }

    private void buildDayModels() {
        mDayModels = new ArrayList<>(DAYS_LENGTH);
        for(int i = 0; i < mFirstDay; i++) {
            mDayModels.add(null);
        }
        if( mCursor != null && mCursor.moveToFirst() ) {
            while (!mCursor.isAfterLast()) {
                String dispTop = mCursor.getString(mCursor.getColumnIndex("DispTop"));
                String dispShortENG = mCursor.getString(mCursor.getColumnIndex("DispShortENG"));
                String dispShortCH = mCursor.getString(mCursor.getColumnIndex("DispShortCH"));
                String yearDispENG = mCursor.getString(mCursor.getColumnIndex("YearDispENG"));
                String yearDispCH = mCursor.getString(mCursor.getColumnIndex("YearDispCH"));
                String monthLuna = mCursor.getString(mCursor.getColumnIndex("MonthLuna"));
                String dayLuna = mCursor.getString(mCursor.getColumnIndex("DayLuna"));
                String dispLongENG = mCursor.getString(mCursor.getColumnIndex("DispLongENG"));
                String dispLongCH = mCursor.getString(mCursor.getColumnIndex("DispLongCH"));
                mDayModels.add(new DayModel(dispTop, dispShortENG, dispShortCH,
                        yearDispENG, yearDispCH, monthLuna,
                        dayLuna, dispLongENG, dispLongCH));

                mCursor.moveToNext();
            }
        }
        for(int i = mLastDay; i < 42; i++) {
            mDayModels.add(null);
        }
    }

    public List<DayModel> getDayModels() {
        return mDayModels;
    }
}
