package direction123.calendar.adapters;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import direction123.calendar.ViewPagerFragment;
import direction123.calendar.interfaces.DayGridOnClickHandler;

/**
 * Created by fangxiangwang on 9/19/17.
 */


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ViewPagerFragment> mViewPagerFragments = new ArrayList<>();
    private Cursor mCursor;
    private DayGridOnClickHandler mDayGridOnClickHandler;
    // date
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        buildAllViewPagerFragments(mCursor);
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm, DayGridOnClickHandler dayGridOnClickHandler,
                            int selectedYear, int selectedMonth, int selectedDay) {
        super(fm);
        mDayGridOnClickHandler = dayGridOnClickHandler;
        mSelectedYear = selectedYear;
        mSelectedMonth = selectedMonth;
        mSelectedDay = selectedDay;
    }

    @Override
    public Fragment getItem(int position) {
        return mViewPagerFragments.get(position);
    }

    @Override
    public int getCount() {
        if (mViewPagerFragments == null) return 0;
        return mViewPagerFragments.size();
    }

    public void setData(List<ViewPagerFragment> fragments) {
        mViewPagerFragments = fragments;
        notifyDataSetChanged();
    }

    private void buildAllViewPagerFragments(Cursor cursor) {
        if( cursor != null && cursor.moveToFirst() ) {
            while (!cursor.isAfterLast()) {
                String MonthId = cursor.getString(cursor.getColumnIndex("MonthId"));
                String Month = cursor.getString(cursor.getColumnIndex("Month"));
                String Year = cursor.getString(cursor.getColumnIndex("Year"));
                String daysInMonth = cursor.getString(cursor.getColumnIndex("DaysInMonth"));

                if (mSelectedYear == Integer.parseInt(Year) && mSelectedMonth == Integer.parseInt(Month)) {
                    mViewPagerFragments.add(new ViewPagerFragment(MonthId, Month, Year, daysInMonth, mSelectedDay, mDayGridOnClickHandler));
                } else {
                    mViewPagerFragments.add(new ViewPagerFragment(MonthId, Month, Year, daysInMonth, 1, mDayGridOnClickHandler));
                }

                cursor.moveToNext();
            }
        }
    }
}