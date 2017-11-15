package direction123.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.R;
import direction123.calendar.adapters.DayGridAdapter;
import direction123.calendar.adapters.DaysGridAdapter;
import direction123.calendar.adapters.MonthViewPagerAdapter;
import direction123.calendar.adapters.ViewPagerAdapter;
import direction123.calendar.data.DayContract;
import direction123.calendar.data.MonthContract;
import direction123.calendar.interfaces.DatePickerFragmentListener;
import direction123.calendar.ui.MonthViewPagerFragment;
import direction123.calendar.utils.SyncUtils;

/**
 * Created by fangxiangwang on 9/7/17.
 */



public class MainActivity extends AppCompatActivity implements View.OnTouchListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{
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
    @BindView(R.id.main_toolbar_drop)
    ImageView mDropIcon;

    @BindView(R.id.grid_view_main)
    GridView mGridView;

    final static String DEBUG_TAG = "DEBUG_TAG xxdd";
    private ActionBarDrawerToggle mDrawerToggle;
    private FirebaseAnalytics mFirebaseAnalytics;

    private int mSelectedMonthIndex;

    private MonthViewPagerAdapter mMonthViewPagerAdapter;
    GestureDetector gestureDetector;

    private static final String ARG_POSITION = "position";
    private static final int ID_MONTH_DAYS_LOADER = 35;

    private DaysGridAdapter mDaysGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Sync Widget
        // SyncUtils.TriggerRefresh();
        SyncUtils.CreateSyncAccount(this);

        // DrawLayout, Navigation Drawer
       /* setupDrawerContent(mDrawerView);
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
        updateMenuTitles();*/
        setupToolbar();

        gestureDetector=new GestureDetector(this,new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (direction==Direction.left){
                    //do your stuff
                    Log.d(DEBUG_TAG, "onSwipe: to left");

                }

                if (direction==Direction.right){
                    //do your stuff
                    Log.d(DEBUG_TAG, "onSwipe: to right");
                }
                return true;
            }


        });

        // ViewPager
      /*  mMonthViewPagerAdapter = new MonthViewPagerAdapter(getSupportFragmentManager());
        mMonthViewViewPager.setAdapter(mMonthViewPagerAdapter);
        int id = getCurrentMonthItemID();
        mMonthViewViewPager.setCurrentItem(getCurrentMonthItemID()); */
        mSelectedMonthIndex = getCurrentMonthItemID();

       /* FragmentManager fragmentManager = getSupportFragmentManager();
        MonthViewPagerFragment monthViewPagerFragment = MonthViewPagerFragment.newInstance(mSelectedMonthIndex);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, monthViewPagerFragment)
                .commit(); */

        mMonthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMonthIndex--;
                Log.v("xxdd", mSelectedMonthIndex + "");
              //  getSupportLoaderManager().initLoader(ID_MONTH_DAYS_LOADER, null, MainActivity.this);
                getSupportLoaderManager().restartLoader(ID_MONTH_DAYS_LOADER, null, MainActivity.this);


               /* FragmentManager fragmentManager = getSupportFragmentManager();
                MonthViewPagerFragment monthViewPagerFragment = MonthViewPagerFragment.newInstance(mSelectedMonthIndex);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, monthViewPagerFragment)
                        .commit(); */
            }
        });

        //grid view
        mDaysGridAdapter = new DaysGridAdapter(this);
        mGridView.setAdapter(mDaysGridAdapter);

        // Loader
        getSupportLoaderManager().initLoader(ID_MONTH_DAYS_LOADER, null, this);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(DEBUG_TAG, "onTouch: ");
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MONTH_DAYS_LOADER:
                Log.v("xx create loader d", getYear() + ", " + getMonth());

                Uri getAllDaysUri = MonthContract.MonthEntry
                        .buildMonthUriWithYearMonth(getYear(), getMonth());
                Log.v("xx create loader", getAllDaysUri.toString());
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
                Log.v("xxxx onLoadFinished", (data == null) + ":" );

                mDaysGridAdapter.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Menu
        updateMenuTitles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }





    private void updateMenuTitles() {
      /*  MenuItem settingItem = mDrawerView.getMenu().findItem(R.id.nav_setting_fragment);
        MenuItem aboutItem = mDrawerView.getMenu().findItem(R.id.nav_about_fragment);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String langPref = sharedPref.getString(getResources().getString(R.string.pref_lang_key), "");
        if (langPref.equals(getResources().getString(R.string.pref_language_ch_value))) {
            settingItem.setTitle(getResources().getString(R.string.setting_menu_ch));
            aboutItem.setTitle(getResources().getString(R.string.about_menu_ch));
        } else {
            settingItem.setTitle(getResources().getString(R.string.setting_menu_en));
            aboutItem.setTitle(getResources().getString(R.string.about_menu_en));
        } */
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

    private void setupToolbar(){
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
        mJumpToday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        mDropIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    public void setActionBarTitle(String title){
        mTitleTextView.setText(title);
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
        switch(menuItem.getItemId()) {
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
        //mDrawerLayout.closeDrawers();
    }

    public void showDatePickerDialog() {

    }

    private int getCurrentMonthItemID() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return (year - 1901) * 12 + month;
    }

    private int getYear() {
        return mSelectedMonthIndex/12 + 1901;
    }
    private int getMonth() {
        return mSelectedMonthIndex - 12 * (mSelectedMonthIndex/12);
    }
}



