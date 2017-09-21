package direction123.calendar;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridAdapter;
import direction123.calendar.adapters.DayGridOnClickHandler;
import direction123.calendar.adapters.ViewPagerAdapter;
import direction123.calendar.data.DayContract;
import direction123.calendar.data.DayModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import direction123.calendar.data.MonthContract;
import direction123.calendar.interfaces.DatePickerFragmentListener;


public class MonthFragment extends Fragment implements ViewPager.OnPageChangeListener,
        DayGridOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String DAYS_ARGS = "daysArgs";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.disp_year)
    TextView mDispYearView;
    @BindView(R.id.disp_long)
    TextView mDsipLongView;
    @BindView(R.id.disp_fortune)
    TextView mDispFortuneView;

    // date
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    // ViewaPager
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPagerFragment mCurViewPagerFragment;

    // Toolbar
    private ImageView mJumpToday;

    private static final int ID_MONTH_LOADER = 33;
    private static final int ID_DAYS_LOADER = 34;


    public static MonthFragment newInstance(int year, int month, int day) {
        MonthFragment mFragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putInt(DAY, day);
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month, container, false);
        ButterKnife.bind(this, rootView);

        //args
        Bundle bundle = getArguments();
        mSelectedYear = bundle.getInt(YEAR);
        mSelectedMonth = bundle.getInt(MONTH);
        mSelectedDay = bundle.getInt(DAY);

        //ViewPager
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), this,
                mSelectedYear, mSelectedMonth, mSelectedDay);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        // Loader
        getActivity().getSupportLoaderManager().initLoader(ID_MONTH_LOADER, null, this);

        // Toolbar jumpTpday
        mJumpToday = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.main_toolbar_today);
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
                mViewPager.setCurrentItem(getSelectedMonthId() - 1, false);
                break;
            }
            case ID_DAYS_LOADER: {
                if (mCurViewPagerFragment != null) {
                    DayGridAdapter dayGridAdapter = mCurViewPagerFragment.getAdapter();
                    dayGridAdapter.swapCursor(data);
                    int position = dayGridAdapter.getSelectedPosition();
                    displayBottomText(dayGridAdapter.getDayModels().get(position));
                    if (dayGridAdapter.isCurMonth()) {
                        mJumpToday.setVisibility(View.INVISIBLE);
                    } else {
                        mJumpToday.setVisibility(View.VISIBLE);
                    }
                }
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

    private int getSelectedMonthId() {
        return (mSelectedYear - 1901) * 12 + mSelectedMonth;
    }

    @Override
    public void onClick(DayModel dayModel) {
        displayBottomText(dayModel);
    }


    private void displayBottomText(DayModel dayModel) {
        mDispYearView.setText(dayModel.getDispYear("English"));
        mDsipLongView.setText(dayModel.getDispLong("English"));
        mDispFortuneView.setText(dayModel.getFortune("English"));
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
