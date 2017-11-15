package direction123.calendar.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.R;
import direction123.calendar.adapters.DayGridAdapter;
import direction123.calendar.adapters.DaysGridAdapter;
import direction123.calendar.data.DayContract;
import direction123.calendar.data.DayModel;
import direction123.calendar.data.MonthContract;

public class MonthViewPagerFragment extends Fragment
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.position)
    TextView mPositionTextView;

    private static final String ARG_POSITION = "position";
    private static final int ID_MONTH_DAYS_LOADER = 35;

    private int mPosition;

    private DaysGridAdapter mDaysGridAdapter;

    public MonthViewPagerFragment() {
        // Required empty public constructor
    }

    public static MonthViewPagerFragment newInstance(int position) {
        MonthViewPagerFragment fragment = new MonthViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
        //grid view
        mDaysGridAdapter = new DaysGridAdapter(getContext());
        // Loader
        getActivity().getSupportLoaderManager().initLoader(ID_MONTH_DAYS_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_month_view_pager, container, false);
        ButterKnife.bind(this, rootView);

        mPositionTextView.setText(String.valueOf(mPosition));
        mGridView.setAdapter(mDaysGridAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MONTH_DAYS_LOADER:
                Uri getAllDaysUri = MonthContract.MonthEntry
                        .buildMonthUriWithYearMonth(getYear(), getMonth());
                Log.v("xx create loader", getAllDaysUri.toString());
                return new CursorLoader(getContext(),
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

    private int getYear() {
        return mPosition/12 + 1901;
    }
    private int getMonth() {
        return mPosition - 12 * (mPosition/12);
    }
}
