package direction123.calendar.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import direction123.calendar.CalendarWidgetProvider;

/**
 * Created by fangxiangwang on 9/22/17.
 */

public class SyncUtils {

    public static void CreateDailyRefresh(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // intent to sync widget

        Intent intent = new Intent(context, CalendarWidgetProvider.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Set the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23); // pm
        calendar.set(Calendar.MINUTE, 59);
        // setRepeating() lets you specify a precise custom interval--in this case, 1 day
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
