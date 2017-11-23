package direction123.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridHeaderAdapter;
import direction123.calendar.adapters.DaysGridAdapter;
import direction123.calendar.data.DayModel;
import direction123.calendar.data.MonthContract;
import direction123.calendar.interfaces.DatePickerFragmentListener;
import direction123.calendar.utils.CalendarUtils;
import direction123.calendar.utils.GridBackground;
import direction123.calendar.utils.NetworkUtils;
import direction123.calendar.utils.SyncUtils;

/**
 * Created by fangxiangwang on 9/7/17.
 */



public class MainActivity extends AppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        DatePickerFragmentListener {
    //DrawLayout, Navigation Drawer
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_view)
    NavigationView mDrawerView;

    //Toolbar
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_toolbar_back)
    ImageView mMonthBack;
    @BindView(R.id.main_toolbar_title)
    TextView mTitleTextView;
    @BindView(R.id.main_toolbar_next)
    ImageView mMonthNext;
    @BindView(R.id.main_toolbar_today)
    ImageView mJumpToday;

    //days grid
    @BindView(R.id.grid_view_main)
    GridView mGridView;
    @BindView(R.id.grid_view_header)
    GridView mGridViewHeader;

    //bottom display
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


    // language preferences
    private String mLangPref;

    final static String DEBUG_TAG = "DEBUG_TAG xxdd";
    private ActionBarDrawerToggle mDrawerToggle;
    private FirebaseAnalytics mFirebaseAnalytics;

    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;
    private int mSelectedMonthIndex;

    private static final String ARG_POSITION = "position";
    private static final int ID_MONTH_DAYS_LOADER = 35;
    private static final int MONTH_COUNT = 2388;
    private DaysGridAdapter mDaysGridAdapter;
    private DayGridHeaderAdapter mGridHeaderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //init current date
        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mSelectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mSelectedMonthIndex = (mSelectedYear - 1901) * 12 + mSelectedMonth;

        //get language preference
        PreferenceManager.setDefaultValues(this, R.xml.preference_settings, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mLangPref = sharedPref.getString(getResources().getString(R.string.pref_lang_key), "");

        // Sync Widget
        // SyncUtils.TriggerRefresh();
        SyncUtils.CreateSyncAccount(this);

        // DrawLayout, Navigation Drawer
        setupDrawerContent(mDrawerView);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //toolbar
        setupToolbar();
        //grid view header
        mGridHeaderAdapter = new DayGridHeaderAdapter(this);
        mGridViewHeader.setAdapter(mGridHeaderAdapter);

        //grid view
        mDaysGridAdapter = new DaysGridAdapter(this);
        mGridView.setAdapter(mDaysGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayModel dayModel = (DayModel) mDaysGridAdapter.getItem(position);
                if (dayModel != null) {
                    mDispYearView.setText(dayModel.getDispYear(mLangPref));
                    mDsipLongView.setText(dayModel.getDispLong(mLangPref));
                    mDispFortuneView.setText(dayModel.getFortune(mLangPref));

                    GridBackground gridBackground = new GridBackground(getApplicationContext());
                    List<DayModel> dayModels = mDaysGridAdapter.getDayModel();
                    int firstDay = mDaysGridAdapter.getFirstDayIndex();
                    if (isCurrentMonth()) {
                        if (position == (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + firstDay - 1)) {
                            if (dayModels != null) {
                                for (int i = 0; i < dayModels.size(); i++) {
                                    if (i != (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + firstDay - 1)) {
                                        gridBackground.setNoSelectedBackground(mGridView.getChildAt(i));
                                    }
                                }
                            }
                            gridBackground.setPrimaryColorBackground(view);
                        } else {
                            if (dayModels != null) {
                                for (int i = 0; i < dayModels.size(); i++) {
                                    if (i == (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + firstDay - 1)) {
                                        gridBackground.setGreyColorBackground(mGridView.getChildAt(i));
                                    } else {
                                        gridBackground.setNoSelectedBackground(mGridView.getChildAt(i));
                                    }
                                }
                            }
                            gridBackground.setPrimaryColorBorder(view);
                        }
                    } else {
                        if (dayModels != null) {
                            for (int i = 0; i < dayModels.size(); i++) {
                                if (i != position) {
                                    gridBackground.setNoSelectedBackground(mGridView.getChildAt(i));
                                }
                            }
                            gridBackground.setPrimaryColorBorder(view);
                        }
                    }
                }
            }
        });

        // Loader
        getSupportLoaderManager().initLoader(ID_MONTH_DAYS_LOADER, null, this);

        // init UI
        //updateUI();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mMonthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMonthIndex++;
                mSelectedYear = (mSelectedMonthIndex - 1) / 12 + 1901;
                mSelectedMonth = mSelectedMonthIndex - 12 * (mSelectedYear - 1901);
                if (!isCurrentMonth()) {
                    mSelectedDay = 1;
                }
                if (mSelectedMonthIndex <= MONTH_COUNT) {
                    getSupportLoaderManager().restartLoader(ID_MONTH_DAYS_LOADER, null, MainActivity.this);
                }
            }
        });
        mMonthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMonthIndex--;
                mSelectedYear = (mSelectedMonthIndex - 1) / 12 + 1901;
                mSelectedMonth = mSelectedMonthIndex - 12 * (mSelectedYear - 1901);
                if (!isCurrentMonth()) {
                    mSelectedDay = 1;
                }
                if (mSelectedMonthIndex > 0) {
                    getSupportLoaderManager().restartLoader(ID_MONTH_DAYS_LOADER, null, MainActivity.this);
                }
            }
        });
        mJumpToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
                mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                mSelectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                mSelectedMonthIndex = (mSelectedYear - 1901) * 12 + mSelectedMonth;
                getSupportLoaderManager().restartLoader(ID_MONTH_DAYS_LOADER, null, MainActivity.this);
            }
        });
    }

    private boolean isCurrentMonth() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return mSelectedYear == year && mSelectedMonth == month;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MONTH_DAYS_LOADER:
                Uri getAllDaysUri = MonthContract.MonthEntry
                        .buildMonthUriWithYearMonth(mSelectedYear, mSelectedMonth);
                return new CursorLoader(this,
                        getAllDaysUri,
                        null,
                        null,
                        null,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_MONTH_DAYS_LOADER: {
                mDaysGridAdapter.swapCursor(data);
                mDaysGridAdapter.setDate(mSelectedYear, mSelectedMonth, mSelectedDay);
                mDaysGridAdapter.notifyDataSetChanged();
                //update ui
                updateUI();
                break;
            }
        }
    }

    public void updateUI() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mLangPref = sharedPref.getString(getResources().getString(R.string.pref_lang_key), "");
        //day grid
        mDaysGridAdapter.refreshUI();
        mGridHeaderAdapter.refreshUI();
        //toolbar title
        String toolBarTitle;
        Map<String, String> hashMap = new CalendarUtils().getMonthMapping();
        if (mLangPref.equals(getResources().getString(R.string.pref_language_ch_value))) {
            toolBarTitle = mSelectedYear + "年" + mSelectedMonth + "月";
        } else {
            toolBarTitle = hashMap.get(String.valueOf(mSelectedMonth)) + " " + mSelectedYear;
        }
        mTitleTextView.setText(toolBarTitle);
        //bottom text
        int position = mSelectedDay + mDaysGridAdapter.getFirstDayIndex() - 1;
        DayModel dayModel = (DayModel) mDaysGridAdapter.getItem(position);
        if (dayModel != null) {
            mDispYearView.setText(dayModel.getDispYear(mLangPref));
            mDsipLongView.setText(dayModel.getDispLong(mLangPref));
            mDispFortuneView.setText(dayModel.getFortune(mLangPref));
        }
        //jump to today
        if (isCurrentMonth()) {
            mJumpToday.setVisibility(View.INVISIBLE);
        } else {
            mJumpToday.setVisibility(View.VISIBLE);
        }
        //update menu item
        MenuItem settingItem = mDrawerView.getMenu().findItem(R.id.nav_setting_fragment);
        MenuItem aboutItem = mDrawerView.getMenu().findItem(R.id.nav_about_fragment);
        if (mLangPref.equals(getResources().getString(R.string.pref_language_ch_value))) {
            settingItem.setTitle(getResources().getString(R.string.setting_menu_ch));
            aboutItem.setTitle(getResources().getString(R.string.about_menu_ch));
        } else {
            settingItem.setTitle(getResources().getString(R.string.setting_menu_en));
            aboutItem.setTitle(getResources().getString(R.string.about_menu_en));
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // updateUI
        updateUI();
        if (mLangPref.equals(getResources().getString(R.string.pref_language_en_value))) {
            new FetchQuoteTask().execute();
        }
        // close drawerlayout
        /*if(mDrawerLayout.isDrawerOpen(mDrawerView)) {
            mDrawerLayout.closeDrawer(mDrawerView);
        } */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Handling the touch event of app icon
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_setting_fragment:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_about_fragment:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
        }
        // Close the navigation drawer
        // mDrawerLayout.closeDrawers();
    }

    public void showDatePickerDialog() {
        DatePickerFragment dFragment = DatePickerFragment.newInstance(this);
        dFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month + 1;
        mSelectedDay = day;
        mSelectedMonthIndex = (mSelectedYear - 1901) * 12 + mSelectedMonth;
        getSupportLoaderManager().restartLoader(ID_MONTH_DAYS_LOADER, null, MainActivity.this);
    }

    class FetchQuoteTask extends AsyncTask<String, Void, String> {

        public FetchQuoteTask() {
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



