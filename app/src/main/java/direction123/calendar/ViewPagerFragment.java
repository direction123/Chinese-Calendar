package direction123.calendar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import direction123.calendar.adapters.DayGridHeaderAdapter;
import direction123.calendar.adapters.DayGridOnClickHandler;
import direction123.calendar.data.DayContract;
import direction123.calendar.data.DayModel;
import direction123.calendar.data.MonthContract;

@SuppressLint("ValidFragment")
public class ViewPagerFragment extends Fragment {
    @BindView(R.id.gridView)
    GridView mGridView;
    @BindView(R.id.gridView_header)
    GridView mGridViewHeader;

    private String mMonthId;
    private String mMonth;
    private String mYear;
    private String[] mDayItems;
    private int mFirstDay;
    private int mLastDay;
    private DayGridAdapter mGridAdapter;
    private DayGridHeaderAdapter mGridHeaderAdapter;
    private DayGridOnClickHandler mClickHandler;

    public ViewPagerFragment (String monthId, String month, String year, String dayItems, DayGridOnClickHandler clickHandler) {
        this.mMonthId = monthId;
        this.mMonth = month;
        this.mYear = year;
        this.mDayItems = dayItems.split(",");
        this.mClickHandler = clickHandler;
        getFirstAndLastDay();
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

        //gridview
        mGridAdapter = new DayGridAdapter(getContext(), mFirstDay, mLastDay);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                List<DayModel> dayModels = mGridAdapter.getDayModels();
                mClickHandler.onClick(dayModels.get(position));
            }
        });
        mGridHeaderAdapter = new DayGridHeaderAdapter(getContext());
        mGridViewHeader.setAdapter(mGridHeaderAdapter);

        return rootView;
    }

    public void getFirstAndLastDay() {
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

    public String getFirstDayId() {
        return mDayItems[mFirstDay];
    }

    public String getLastDayId() {
        return mDayItems[mLastDay];
    }

    public DayGridAdapter getAdapter() {
        return mGridAdapter;
    }
}
