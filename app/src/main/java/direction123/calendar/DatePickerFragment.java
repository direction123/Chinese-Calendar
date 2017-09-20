package direction123.calendar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import direction123.calendar.interfaces.DatePickerFragmentListener;

/**
 * A simple {@link Fragment} subclass.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerFragmentListener mDatePickerListener;

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        // Here we call the listener and pass the date back to it.
        notifyDatePickerListener(year, month, day);
    }


    public DatePickerFragmentListener getDatePickerListener() {
        return mDatePickerListener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        mDatePickerListener = listener;
    }

    protected void notifyDatePickerListener(int year, int month, int day) {
        if(mDatePickerListener != null) {
            mDatePickerListener.onDateSet(year, month, day);
        }
    }
}
