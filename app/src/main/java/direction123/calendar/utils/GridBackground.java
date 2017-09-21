package direction123.calendar.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import direction123.calendar.R;

/**
 * Created by fangxiangwang on 9/20/17.
 */

public class GridBackground {
    Context mContext;

    public GridBackground(Context context) {
        mContext = context;
    }

    public void setPrimaryColorBackground(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        GradientDrawable border = new GradientDrawable();
        border.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        border.setCornerRadius(10);
        border.setStroke(3, ContextCompat.getColor(mContext, R.color.colorPrimary));
        view.setBackground(border);

        dayTextView.setTextColor(Color.WHITE);
        dayLunarTextView.setTextColor(Color.WHITE);
    }

    public void setPrimaryColorBorder(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE);
        border.setCornerRadius(10);
        border.setStroke(3, ContextCompat.getColor(mContext, R.color.colorPrimary));
        view.setBackground(border);

        dayTextView.setTextColor(Color.BLACK);
        dayLunarTextView.setTextColor(Color.BLACK);
    }

    public void setGreyColorBackground(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        GradientDrawable border = new GradientDrawable();
        border.setColor(ContextCompat.getColor(mContext, R.color.colorGreyDarkLight));
        border.setCornerRadius(10);
        border.setStroke(3, ContextCompat.getColor(mContext, R.color.colorGreyDarkLight));
        view.setBackground(border);

        dayTextView.setTextColor(Color.WHITE);
        dayLunarTextView.setTextColor(Color.WHITE);
    }

    public void setNoSelectedBackground(View view) {
        TextView dayTextView = (TextView) view.findViewById(R.id.day);
        TextView dayLunarTextView = (TextView) view.findViewById(R.id.day_lunar);

        view.setBackgroundColor(Color.WHITE);
        dayTextView.setTextColor(Color.BLACK);
        dayLunarTextView.setTextColor(Color.BLACK);
    }
}
