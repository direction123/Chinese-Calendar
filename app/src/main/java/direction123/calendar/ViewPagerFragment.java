package direction123.calendar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.app.Activity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import java.util.Calendar;
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

                if (isToday() && position == (mFirstDay + mSelectedDay -1)) {
                    setPrimaryColorBackground(view);
                } else {
                    for (int i = 0; i < dayModels.size(); i++) {
                        if (isToday() && i == (mFirstDay + mSelectedDay -1)) {
                            setGreyColorBackground(mGridView.getChildAt(i));
                        } else {
                            setNoSelectedBackground(mGridView.getChildAt(i));
                        }
                    }
                    setPrimaryColorBorder(view);
                }

                /*for (int i = 0; i < dayModels.size(); i++) {
                    View childView = mGridView.getChildAt(i);
                    TextView dayTextView = (TextView) childView.findViewById(R.id.day);
                    TextView dayLunarTextView = (TextView) childView.findViewById(R.id.day_lunar);

                    if (isToday() && i == (mFirstDay + mSelectedDay -1)) {
                        childView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        dayTextView.setTextColor(Color.WHITE);
                        dayLunarTextView.setTextColor(Color.WHITE);
                    } else {
                        childView.setBackgroundColor(Color.WHITE);
                        dayTextView.setTextColor(Color.BLACK);
                        dayLunarTextView.setTextColor(Color.BLACK);
                    }
                }

                if(isToday() && position == (mFirstDay + mSelectedDay -1)) {
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    GradientDrawable border = new GradientDrawable();
                    border.setColor(Color.WHITE);
                    border.setCornerRadius(10);
                    border.setStroke(3, ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    view.setBackground(border);
                }*/

             /*   if ((isToday() && position != (mFirstDay + mSelectedDay -1))
                        || !isToday()) {
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    final TextView dayTextView = (TextView) view.findViewById(R.id.day);
                    final TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);
                    dayTextView.setTextColor(Color.WHITE);
                    dayLunarTextView.setTextColor(Color.WHITE);

                    new CountDownTimer(1000, 10) {
                        @Override
                        public void onTick(long arg0) {
                        }

                        @Override
                        public void onFinish() {
                            view.setBackgroundColor(Color.WHITE);
                            dayTextView.setTextColor(Color.BLACK);
                            dayLunarTextView.setTextColor(Color.BLACK);
                        }
                    }.start();
                } */
            }
        });
        mGridView.setSelection(10);

        mGridHeaderAdapter = new DayGridHeaderAdapter(getContext());
        mGridViewHeader.setAdapter(mGridHeaderAdapter);

        return rootView;
    }

    private void setPrimaryColorBackground(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        GradientDrawable border = new GradientDrawable();
        border.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        border.setCornerRadius(10);
        border.setStroke(3, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        view.setBackground(border);

        dayTextView.setTextColor(Color.WHITE);
        dayLunarTextView.setTextColor(Color.WHITE);
    }

    private void setPrimaryColorBorder(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE);
        border.setCornerRadius(10);
        border.setStroke(3, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        view.setBackground(border);

        dayTextView.setTextColor(Color.BLACK);
        dayLunarTextView.setTextColor(Color.BLACK);
    }

    private void setGreyColorBackground(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        GradientDrawable border = new GradientDrawable();
        border.setColor(ContextCompat.getColor(getContext(), R.color.colorGreyDarkLight));
        border.setCornerRadius(10);
        border.setStroke(3, ContextCompat.getColor(getContext(), R.color.colorGreyDarkLight));
        view.setBackground(border);

        dayTextView.setTextColor(Color.WHITE);
        dayLunarTextView.setTextColor(Color.WHITE);
    }

    private void setNoSelectedBackground(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        view.setBackgroundColor(Color.WHITE);
        dayTextView.setTextColor(Color.BLACK);
        dayLunarTextView.setTextColor(Color.BLACK);
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

    private boolean isToday() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (year == Integer.parseInt(mYear) && month == Integer.parseInt(mMonth)
                && day == mSelectedDay) {
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
