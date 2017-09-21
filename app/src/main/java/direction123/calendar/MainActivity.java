package direction123.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.R;
import direction123.calendar.interfaces.DatePickerFragmentListener;

/**
 * Created by fangxiangwang on 9/7/17.
 */


public class MainActivity extends AppCompatActivity implements DatePickerFragmentListener{
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

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        updateMenuTitles();
        setupToolbar();

        // Fragment
        Calendar c = Calendar.getInstance();
        MonthFragment mFragment = MonthFragment.newInstance(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Menu
        updateMenuTitles();
        // Fragment
        Calendar c = Calendar.getInstance();
        MonthFragment mFragment = MonthFragment.newInstance(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();
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

    private void updateMenuTitles() {
        MenuItem settingItem = mDrawerView.getMenu().findItem(R.id.nav_setting_fragment);
        MenuItem aboutItem = mDrawerView.getMenu().findItem(R.id.nav_about_fragment);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String langPref = sharedPref.getString(getResources().getString(R.string.pref_lang_key), "");
        if (langPref.equals(getResources().getString(R.string.pref_language_ch_value))) {
            settingItem.setTitle(getResources().getString(R.string.setting_menu_ch));
            aboutItem.setTitle(getResources().getString(R.string.about_menu_ch));
        } else {
            settingItem.setTitle(getResources().getString(R.string.setting_menu_en));
            aboutItem.setTitle(getResources().getString(R.string.about_menu_en));
        }
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
                Calendar c = Calendar.getInstance();
                MonthFragment mFragment = MonthFragment.newInstance(
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DAY_OF_MONTH));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mFragment)
                        .commit();
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
        mDrawerLayout.closeDrawers();
    }

    public void showDatePickerDialog() {
        DatePickerFragment dFragment = DatePickerFragment.newInstance(this);
        dFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        MonthFragment mFragment = MonthFragment.newInstance(year, month + 1, day);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mFragment)
                .commit();
    }
}

