package direction123.calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Map;

import direction123.calendar.data.DayContract;
import direction123.calendar.utils.CalendarUtils;

/**
 * Implementation of App Widget functionality.
 */
public class CalendarWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        Uri CUR_DAY_URI = DayContract.DayEntry.buildCurrentDay(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        Cursor cursor = context.getContentResolver().query(
                CUR_DAY_URI,
                null,
                null,
                null,
                null
        );
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String langPref = sharedPref.getString(
                context.getResources().getString(R.string.pref_lang_key), ""
        );
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar_widget);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (langPref.equals("Simplified Chinese")) {
                views.setTextViewText(R.id.widget_year_month,
                        cursor.getString(cursor.getColumnIndex("MonthGG")) + "月");
                views.setTextViewText(R.id.widget_day,
                        cursor.getString(cursor.getColumnIndex("DispTop")));
                views.setTextViewText(R.id.widget_lunar_day,
                        cursor.getString(cursor.getColumnIndex("MonthLuna")) + " " +
                                cursor.getString(cursor.getColumnIndex("DayLuna")));
            } else {
                Map<String, String> hashMap = new CalendarUtils().getShortMonthMapping();
                views.setTextViewText(R.id.widget_year_month,
                        hashMap.get(cursor.getString(cursor.getColumnIndex("MonthGG"))));
                views.setTextViewText(R.id.widget_day,
                        cursor.getString(cursor.getColumnIndex("DispTop")));
                views.setTextViewText(R.id.widget_lunar_day,
                        cursor.getString(cursor.getColumnIndex("DispShortENG")));
            }
            cursor.close();
        }

        // Open calendar when click
        Intent homeIntent = new Intent(context, MainActivity.class);
        PendingIntent homePendingIntent = PendingIntent.getActivity(context, 0, homeIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_year_month, homePendingIntent);
        views.setOnClickPendingIntent(R.id.widget_day, homePendingIntent);
        views.setOnClickPendingIntent(R.id.widget_lunar_day, homePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CalendarWidgetProvider.class));
            //Trigger data update to handle the GridView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_year_month);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_year_month);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_year_month);
            //Now update all widgets
            for (int appWidgetId : appWidgetIds) {
                CalendarWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }
}

