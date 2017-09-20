package direction123.calendar;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridOnClickHandler;
import direction123.calendar.adapters.ViewPagerAdapter;
import direction123.calendar.data.DayContract;
import direction123.calendar.data.DayModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import direction123.calendar.data.MonthContract;
import direction123.calendar.utils.WrapContentHeightViewPager;


public class MonthFragment extends Fragment implements ViewPager.OnPageChangeListener,
        DayGridOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DAYS_ARGS = "daysArgs";

    @BindView(R.id.viewpager)
    WrapContentHeightViewPager mViewPager;
    @BindView(R.id.disp_year)
    TextView mDispYearView;
    @BindView(R.id.disp_long)
    TextView mDsipLongView;
    @BindView(R.id.disp_fortune)
    TextView mDispFortuneView;

    // ViewaPager
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPagerFragment mCurViewPagerFragment;

    private static final int ID_MONTH_LOADER = 33;
    private static final int ID_DAYS_LOADER = 34;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month, container, false);
        ButterKnife.bind(this, rootView);

        //ViewPager
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        // Loader
        getActivity().getSupportLoaderManager().initLoader(ID_MONTH_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_MONTH_LOADER:
                Uri getAllDaysUri = MonthContract.MonthEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        getAllDaysUri,
                        null,
                        null,
                        null,
                        null);

            case ID_DAYS_LOADER:
                Uri getDaysOneMonthUri = DayContract.DayEntry.CONTENT_URI;
                String arguments[]= args.getStringArray(DAYS_ARGS);
                return new CursorLoader(getContext(),
                        getDaysOneMonthUri,
                        null,
                        null,
                        arguments,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_MONTH_LOADER: {
                mViewPagerAdapter.swapCursor(data);
                mViewPager.setCurrentItem(getCurrentMonthId() - 1, false);
                break;
            }
            case ID_DAYS_LOADER: {
                if (mCurViewPagerFragment != null)
                    mCurViewPagerFragment.getAdapter().swapCursor(data);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurViewPagerFragment = (ViewPagerFragment) mViewPagerAdapter.getItem(position);
        if (mCurViewPagerFragment != null) {
            ((MainActivity) getActivity()).setActionBarTitle(
                    getTitle(mCurViewPagerFragment.getCurMonth(),
                    mCurViewPagerFragment.getCurYear()));

            String firstDayId = mCurViewPagerFragment.getFirstDayId();
            String lastDayId = mCurViewPagerFragment.getLastDayId();

            String[] args = new String[]{firstDayId, lastDayId};
            Bundle bundle = new Bundle();
            bundle.putStringArray(DAYS_ARGS, args);
            getActivity().getSupportLoaderManager().restartLoader(ID_DAYS_LOADER, bundle, this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int getCurrentMonthId() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; //Keep in mind that months values start from 0, so October is actually month number 9.
        return (year - 1901) * 12 + month;
    }

    @Override
    public void onClick(DayModel dayModel) {
        mDispYearView.setText(dayModel.getDispYear("Chinese"));
        mDsipLongView.setText(dayModel.getDispLong("Chinese"));
    }

    private String getTitle (String month, String year) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "January");
        hashMap.put("2", "February");
        hashMap.put("3", "March");
        hashMap.put("4", "April");
        hashMap.put("5", "May");
        hashMap.put("6", "June");
        hashMap.put("7", "July");
        hashMap.put("8", "Auguest");
        hashMap.put("9", "September");
        hashMap.put("10", "October");
        hashMap.put("11", "November");
        hashMap.put("12", "December");

        return hashMap.get(month) + " " + year;
    }

}
