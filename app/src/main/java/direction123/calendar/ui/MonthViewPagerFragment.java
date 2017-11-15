package direction123.calendar.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import direction123.calendar.R;

public class MonthViewPagerFragment extends Fragment {
    @BindView(R.id.position)
    TextView mPositionTextView;

    private static final String ARG_POSITION = "position";

    private int mPosition;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_month_view_pager, container, false);
        ButterKnife.bind(this, rootView);

        mPositionTextView.setText(String.valueOf(mPosition));
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

}
