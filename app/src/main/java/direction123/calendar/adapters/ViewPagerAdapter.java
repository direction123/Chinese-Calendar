package direction123.calendar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import direction123.calendar.ViewPagerFragment;
/**
 * Created by fangxiangwang on 9/19/17.
 */


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ViewPagerFragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if (mFragments == null) return 0;
        return mFragments.size();
    }

    public void setData(List<ViewPagerFragment> fragments) {
        mFragments = fragments;
        notifyDataSetChanged();
    }
}