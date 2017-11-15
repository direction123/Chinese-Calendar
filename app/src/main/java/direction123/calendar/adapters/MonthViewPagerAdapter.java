package direction123.calendar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import direction123.calendar.ui.MonthViewPagerFragment;

/**
 * Created by fangxiangwang on 11/14/17.
 */

public class MonthViewPagerAdapter extends FragmentStatePagerAdapter {
    static final int ITEMS = 2388;
    List<MonthViewPagerFragment> fragmentArray = new ArrayList<>();

    public MonthViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        for(int i = 0; i < ITEMS; i++) {
            fragmentArray.add(null);
        }
    }

    @Override
    public int getCount() {
        return ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return MonthViewPagerFragment.newInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthViewPagerFragment fragment = (MonthViewPagerFragment) super.instantiateItem(container, position);
        fragmentArray.add(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragmentArray.remove(position);
        super.destroyItem(container, position, object);
    }
}
