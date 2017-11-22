package direction123.calendar.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import direction123.calendar.R;
import direction123.calendar.data.DayModel;
import direction123.calendar.utils.GridBackground;

/**
 * Created by fangxiangwang on 11/15/17.
 */

public class DaysGridAdapter extends BaseAdapter{
    private LayoutInflater mInflater;

    private Context mContext;

    private Cursor mCursor;
    private List<DayModel> mDayModels = new ArrayList<>();
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    private String mLangPref;

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        buildDayModels();
        notifyDataSetChanged();
    }

    public DaysGridAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mLangPref = sharedPref.getString(mContext.getResources().getString(R.string.pref_lang_key), "");
    }

    @Override
    public int getCount() {
        return mDayModels.size();
    }

    public void setDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        mSelectedDay = day;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView dayTextView;
        TextView dayLunarTextView;

        if (view == null) {
            view = mInflater.inflate(R.layout.day_grid, parent, false);
        }

        dayTextView = (TextView) view.findViewById(R.id.day);
        dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);
        if (mDayModels != null && position >= 0 && position < getCount() && mDayModels.get(position) != null) {
            DayModel dayModel = mDayModels.get(position);
            dayTextView.setText(dayModel.getDispTop());
            dayLunarTextView.setText(dayModel.getDispShort(mLangPref));
        } else {
            dayTextView.setText("");
            dayLunarTextView.setText("");
        }

        GridBackground gridBackground = new GridBackground(mContext);
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int curDay = c.get(Calendar.DAY_OF_MONTH);
        if(mSelectedYear == curYear && mSelectedMonth == curMonth) {
            if(position == (curDay + getFirstDayIndex() - 1)) {
                gridBackground.setPrimaryColorBackground(view);
            } else {
                gridBackground.setNoSelectedBackground(view);
            }
        } else {
            if (position == getFirstDayIndex()) {
                gridBackground.setPrimaryColorBorder(view);
            } else {
                gridBackground.setNoSelectedBackground(view);
            }
        }
        return view;
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
        return 0;
    }

    private void buildDayModels() {
        mDayModels = new ArrayList<>();
        if(mCursor != null && mCursor.moveToFirst() ) {
            while (!mCursor.isAfterLast()) {
                String daysInMonth = mCursor.getString(mCursor.getColumnIndex("DaysInMonth"));

                if(daysInMonth.equals("0")) {
                    mDayModels.add(null);
                } else {
                    String dispTop = mCursor.getString(mCursor.getColumnIndex("DispTop"));
                    String dispShortENG = mCursor.getString(mCursor.getColumnIndex("DispShortENG"));
                    String dispShortCH = mCursor.getString(mCursor.getColumnIndex("DispShortCH"));
                    String yearDispENG = mCursor.getString(mCursor.getColumnIndex("YearDispENG"));
                    String yearDispCH = mCursor.getString(mCursor.getColumnIndex("YearDispCH"));
                    String monthLuna = mCursor.getString(mCursor.getColumnIndex("MonthLuna"));
                    String dayLuna = mCursor.getString(mCursor.getColumnIndex("DayLuna"));
                    String dispLongENG = mCursor.getString(mCursor.getColumnIndex("DispLongENG"));
                    String dispLongCH = mCursor.getString(mCursor.getColumnIndex("DispLongCH"));
                    String fortuneId = mCursor.getString(mCursor.getColumnIndex("JcId"));
                    mDayModels.add(new DayModel(dispTop, dispShortENG, dispShortCH,
                            yearDispENG, yearDispCH, monthLuna,
                            dayLuna, dispLongENG, dispLongCH, fortuneId));
                }
                mCursor.moveToNext();
            }
        }
    }

    public int getFirstDayIndex() {
        if(mDayModels != null && mDayModels.size() != 0) {
            int start = 0;
            while (mDayModels.get(start) == null) {
                start++;
            }
            return start;
        }
        return -1;
    }

    public List<DayModel> getDayModel() {
        return mDayModels;
    }
}
