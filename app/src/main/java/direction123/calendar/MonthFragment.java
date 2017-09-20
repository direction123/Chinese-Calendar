package direction123.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridOnClickHandler;
import direction123.calendar.adapters.ViewPagerAdapter;
import direction123.calendar.data.DayModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment implements ViewPager.OnPageChangeListener,
        DayGridOnClickHandler {
    private static final String TAG = "MonthFragment";

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.disp_year)
    TextView mDispYearView;
    @BindView(R.id.disp_long)
    TextView mDsipLongView;
    @BindView(R.id.disp_fortune)
    TextView mDispFortuneView;

    // ViewaPager
    private List<ViewPagerFragment> mViewPagerFragments = new ArrayList<>();
    private ViewPagerAdapter mViewPagerAdapter;

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
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

        //firebase
        loadAllDaysFromFirebase();

        return rootView;
    }

    @Override
    public void onPageSelected(int position) {
        ViewPagerFragment viewPagerFragment = (ViewPagerFragment) mViewPagerAdapter.getItem(position);
        if (viewPagerFragment != null) {
            String title = viewPagerFragment.getCurMonth() + " " + viewPagerFragment.getCurYear();
            ((MainActivity) getActivity()).setActionBarTitle(title);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void loadAllDaysFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("month");
        Query allDaysQuery = ref.orderByKey();
        allDaysQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String MonthId = "", Month = "", Year = "", daysInMonth = "";
                    for (DataSnapshot single : singleSnapshot.getChildren()) {
                        if (single.getKey().equals("MonthId")) {
                            MonthId = single.getValue().toString();
                        }
                        if (single.getKey().equals("Month")) {
                            Month = single.getValue().toString();
                        }
                        if (single.getKey().equals("Year")) {
                            Year = single.getValue().toString();
                        }
                        if (single.getKey().equals("DaysInMonth")) {
                            daysInMonth = single.getValue().toString();
                        }
                    }
                    mViewPagerFragments.add(new ViewPagerFragment(MonthId, Month, Year, daysInMonth, getOuter()));
                }
                mViewPagerAdapter.setData(mViewPagerFragments);
                mViewPager.setCurrentItem(getCurrentMonthId() - 1, false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled", "onCancelled", databaseError.toException());
            }
        });
    }

    public MonthFragment getOuter() {
        return MonthFragment.this;
    }

    private int getCurrentMonthId() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; //Keep in mind that months values start from 0, so October is actually month number 9.
        return (year - 1901) * 12 + month;
    }

    @Override
    public void onClick(DayModel dayModel) {
        mDispYearView.setText(dayModel.getDispYear("English"));
    }

}
