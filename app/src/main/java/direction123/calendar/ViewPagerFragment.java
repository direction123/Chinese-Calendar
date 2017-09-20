package direction123.calendar;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.app.Activity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridAdapter;
import direction123.calendar.data.DayModel;

public class ViewPagerFragment extends Fragment {
    @BindView(R.id.gridView)
    GridView mGridView;

    private String mMonthId;
    private String mMonth;
    private String mYear;
    private String[] mDayItems;
    private int mFirstDay;
    private int mLastDay;
    private List<DayModel> mDayModels = new ArrayList<>();
    private DayGridAdapter mGridAdapter;


    public static ViewPagerFragment newInstance(String monthId, String month, String year, String dayItems) {
        Bundle args = new Bundle();
        args.putString("monthId", monthId);
        args.putString("month", month);
        args.putString("year", year);
        args.putString("dayItems", dayItems);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.bind(this, rootView);

        //arguments
        mMonthId = getArguments().getString("monthId");
        mMonth = getArguments().getString("month");
        mYear = getArguments().getString("year");
        mDayItems = getArguments().getString("dayItems").split(",");
        getFirstDay();

        //gridview
        mGridAdapter = new DayGridAdapter(getContext());
        mGridView.setAdapter(mGridAdapter);

        //firebase
        loadDaysOneMonthFromFirebase();

        return rootView;
    }

    private void loadDaysOneMonthFromFirebase() {
        for(int i = 0; i < mFirstDay; i++) {
            mDayModels.add(null);
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("days");
        Query allDaysQuery = ref.orderByKey()
                .startAt(String.valueOf(Integer.parseInt(mDayItems[mFirstDay])- 1))
                .endAt(String.valueOf(Integer.parseInt(mDayItems[mLastDay])- 1));
        allDaysQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String dispTop = "", dispShortENG = "", dispShortCH = "", yearDispENG = "", yearDispCH = "";
                    String monthLuna = "", dayLunar = "", dispLongENG = "", dispLongCH = "";
                    for (DataSnapshot single : singleSnapshot.getChildren()) {
                        if (single.getKey().equals("DispTop")) {
                            dispTop = single.getValue().toString();
                        }
                        if (single.getKey().equals("DispShortENG")) {
                            dispShortENG = single.getValue().toString();
                        }
                        if (single.getKey().equals("DispShortCH")) {
                            dispShortCH = single.getValue().toString();
                        }
                        if (single.getKey().equals("YearDispENG")) {
                            yearDispENG = single.getValue().toString();
                        }
                        if (single.getKey().equals("YearDispCH")) {
                            yearDispCH = single.getValue().toString();
                        }
                        if (single.getKey().equals("MonthLuna")) {
                            monthLuna = single.getValue().toString();
                        }
                        if (single.getKey().equals("DayLunar")) {
                            dayLunar = single.getValue().toString();
                        }
                        if (single.getKey().equals("DispLongENG")) {
                            dispLongENG = single.getValue().toString();
                        }
                        if (single.getKey().equals("DispLongCH")) {
                            dispLongCH = single.getValue().toString();
                        }
                    }
                    mDayModels.add(new DayModel(dispTop, dispShortENG, dispShortCH,
                            yearDispENG, yearDispCH, monthLuna,
                            dayLunar, dispLongENG, dispLongCH));
                }
                for(int i = mLastDay; i < 42; i++) {
                    mDayModels.add(null);
                }
                mGridAdapter.setData(mDayModels);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCancelled", "onCancelled", databaseError.toException());
            }
        });
    }

    private void getFirstDay() {
        int start = 0;
        int end = mDayItems.length - 1;
        while (mDayItems[start].equals("0")) {
            start++;
        }
        while (mDayItems[end].equals("0")) {
            end--;
        }
        mFirstDay = start;
        mLastDay = end;
    }

    public String getCurMonth() {
        return mMonth;
    }

    public String getCurYear() {
        return mYear;
    }
}
