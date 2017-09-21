package direction123.calendar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.adapters.DayGridAdapter;
import direction123.calendar.adapters.DayGridHeaderAdapter;
import direction123.calendar.interfaces.DayGridOnClickHandler;
import direction123.calendar.data.DayModel;
import direction123.calendar.utils.GridBackground;

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
    private int mSelectedDay;
    private int mFirstDay;
    private int mLastDay;
    private DayGridAdapter mGridAdapter;
    private DayGridHeaderAdapter mGridHeaderAdapter;
    private DayGridOnClickHandler mClickHandler;

    public ViewPagerFragment (String monthId, String month, String year, String dayItems, int selectedDay, DayGridOnClickHandler clickHandler) {
        mMonthId = monthId;
        mMonth = month;
        mYear = year;
        mDayItems = dayItems.split(",");
        mSelectedDay = selectedDay;
        mClickHandler = clickHandler;
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
        mGridAdapter = new DayGridAdapter(getContext(), mMonth, mYear, mSelectedDay, mFirstDay, mLastDay);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, final View view,
                                    int position, long id) {
                List<DayModel> dayModels = mGridAdapter.getDayModels();
                mClickHandler.onClick(dayModels.get(position));

                GridBackground gridBackground = new GridBackground(getContext());
                if (isCurMonth()) {
                    if (position == (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + mFirstDay - 1)) {
                        for (int i = 0; i < dayModels.size(); i++) {
                            if (i != (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + mFirstDay - 1)) {
                                gridBackground.setNoSelectedBackground(mGridView.getChildAt(i));
                            }
                        }
                        gridBackground.setPrimaryColorBackground(view);
                    } else {
                        for (int i = 0; i < dayModels.size(); i++) {
                            if (i == (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + mFirstDay - 1)) {
                                gridBackground.setGreyColorBackground(mGridView.getChildAt(i));
                            } else {
                                gridBackground.setNoSelectedBackground(mGridView.getChildAt(i));
                            }
                        }
                        gridBackground.setPrimaryColorBorder(view);
                    }
                } else {
                    for (int i = 0; i < dayModels.size(); i++) {
                        if (i != position) {
                            gridBackground.setNoSelectedBackground(mGridView.getChildAt(i));
                        }
                    }
                    gridBackground.setPrimaryColorBorder(view);
                }
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

    public boolean isCurMonth() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1; //Keep in mind that months values start from 0, so October is actually month number 9.
        if (year == Integer.parseInt(mYear) && month == Integer.parseInt(mMonth)) {
            return true;
        }
        return false;
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
