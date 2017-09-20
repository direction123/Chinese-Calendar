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
import direction123.calendar.adapters.ViewPagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MonthFragment extends Fragment {
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

        //firebase
        loadAllDaysFromFirebase();

        return rootView;
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
                    mViewPagerFragments.add(ViewPagerFragment.newInstance(MonthId, Month, Year, daysInMonth));
                }
                mViewPagerAdapter.setData(mViewPagerFragments);
                mViewPager.setCurrentItem(1, false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled", "onCancelled", databaseError.toException());
            }
        });
    }
}
