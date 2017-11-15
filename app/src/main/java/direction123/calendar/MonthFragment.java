package direction123.calendar;

import android.os.AsyncTask;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;

import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridAdapter;
import direction123.calendar.interfaces.DayGridOnClickHandler;
import direction123.calendar.adapters.ViewPagerAdapter;
import direction123.calendar.data.DayContract;
import direction123.calendar.data.DayModel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import direction123.calendar.data.MonthContract;
import direction123.calendar.utils.CalendarUtils;
import direction123.calendar.utils.NetworkUtils;


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
    @BindView(R.id.disp_quote)
    TextView mDispQuoteView;
    @BindView(R.id.disp_quote_author)
    TextView mDispQuoteAuthorView;
    @BindView(R.id.adView)
    AdView mAdView;


    // date
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    // ViewaPager
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPagerFragment mCurViewPagerFragment;

    // Toolbar
    private ImageView mJumpToday;

    // language preferences
    private String mLangPref;

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

        //language preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        mLangPref = sharedPref.getString(getResources().getString(R.string.pref_lang_key), "");

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

        // load quote
        if (mLangPref.equals(getResources().getString(R.string.pref_language_en_value))) {
            new FetchQuoteTask().execute();
        }

        // load ad
        MobileAds.initialize(getContext(),
                "ca-app-pub-6007220938739284/1131306086");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
                    dayGridAdapter.resetSelectedDays();
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
           /* ((MainActivity) getActivity()).setActionBarTitle(
                    getTitle(mCurViewPagerFragment.getCurMonth(),
                    mCurViewPagerFragment.getCurYear())); */

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
        mDispYearView.setText(dayModel.getDispYear(mLangPref));
        mDsipLongView.setText(dayModel.getDispLong(mLangPref));
        mDispFortuneView.setText(dayModel.getFortune(mLangPref));
    }

    private String getTitle (String month, String year) {
        Map<String, String> hashMap = new CalendarUtils().getMonthMapping();

        if (mLangPref.equals(getResources().getString(R.string.pref_language_ch_value))) {
            return year + "年" + month + "月";
        } else {
            return hashMap.get(month) + " " + year;
        }
    }

    class FetchQuoteTask extends AsyncTask<String, Void, String> {

        public FetchQuoteTask () {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URL quoteOfDayUrl = NetworkUtils.buildQuoteOfDayUrl();
            try {
                String quoteOfDayResponse = NetworkUtils.getResponseFromHttpUrl(quoteOfDayUrl);
                return quoteOfDayResponse;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String quoteOfDayData) {
            if (quoteOfDayData != null) {
                try {
                    JSONObject quoteOfDayJson = new JSONObject(quoteOfDayData);
                    if (quoteOfDayJson.getJSONObject("success") != null) {
                        JSONArray quoteArray = quoteOfDayJson.getJSONObject("contents")
                                .getJSONArray("quotes");
                        mDispQuoteView.setText(quoteArray.getJSONObject(0).getString("quote"));
                        mDispQuoteAuthorView.setText("---- " + quoteArray.getJSONObject(0).getString("author"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
