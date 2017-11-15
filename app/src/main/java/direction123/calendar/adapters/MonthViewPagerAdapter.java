package direction123.calendar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import direction123.calendar.ui.MonthViewPagerFragment;

/**
 * Created by fangxiangwang on 11/14/17.
 */

public class MonthViewPagerAdapter extends FragmentStatePagerAdapter {
    static final int ITEMS = 2388;

    public MonthViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return MonthViewPagerFragment.newInstance(position);
        }
    }
}
