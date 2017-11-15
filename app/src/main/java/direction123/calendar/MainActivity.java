package direction123.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.R;
import direction123.calendar.adapters.MonthViewPagerAdapter;
import direction123.calendar.adapters.ViewPagerAdapter;
import direction123.calendar.interfaces.DatePickerFragmentListener;
import direction123.calendar.utils.SyncUtils;

/**
 * Created by fangxiangwang on 9/7/17.
 */


public class MainActivity extends AppCompatActivity {
    // DrawLayout, Navigation Drawer
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_view)
    NavigationView mDrawerView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_toolbar_title)
    TextView mTitleTextView;
    @BindView(R.id.main_toolbar_today)
    ImageView mJumpToday;
    @BindView(R.id.main_toolbar_drop)
    ImageView mDropIcon;
    @BindView(R.id.pager)
    ViewPager mMonthViewViewPager;

    private ActionBarDrawerToggle mDrawerToggle;
    private FirebaseAnalytics mFirebaseAnalytics;

    private MonthViewPagerAdapter mMonthViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMonthViewPagerAdapter = new MonthViewPagerAdapter(getSupportFragmentManager());
        mMonthViewViewPager.setAdapter(mMonthViewPagerAdapter);
        mMonthViewViewPager.setCurrentItem(1422);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Menu
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
}

