package direction123.calendar;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.settings_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.settings_toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        //setupToolbar()
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));
        getSupportActionBar().setTitle(null);

        //settings fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.settings_fragment, new SettingsFragment())
                .commit();

        updateUI();
    }

    public void updateUI() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String langPref = sharedPref.getString(getResources().getString(R.string.pref_lang_key), "");

        //toolbar title
        if (langPref.equals(getResources().getString(R.string.pref_language_ch_value))) {
            mToolbarTitle.setText(getResources().getString(R.string.setting_menu_ch));
        } else {
            mToolbarTitle.setText(getResources().getString(R.string.setting_menu_en));
        }
    }
}
