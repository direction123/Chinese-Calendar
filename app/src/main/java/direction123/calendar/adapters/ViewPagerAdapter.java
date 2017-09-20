package direction123.calendar.adapters;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import direction123.calendar.ViewPagerFragment;
/**
 * Created by fangxiangwang on 9/19/17.
 */


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ViewPagerFragment> mViewPagerFragments = new ArrayList<>();
    private Cursor mCursor;
    private DayGridOnClickHandler mDayGridOnClickHandler;

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        buildAllViewPagerFragments(mCursor);
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm, DayGridOnClickHandler dayGridOnClickHandler) {
        super(fm);
        this.mDayGridOnClickHandler = dayGridOnClickHandler;
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
                mViewPagerFragments.add(new ViewPagerFragment(MonthId, Month, Year, daysInMonth, mDayGridOnClickHandler));

                cursor.moveToNext();
            }
        }
    }
}