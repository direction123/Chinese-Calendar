package direction123.calendar.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fangxiangwang on 9/20/17.
 */

public class DayContract {
    public static final String CONTENT_AUTHORITY = "direction123.calendar";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DAY = "Days";

    public static final class DayEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_DAY)
                .build();

        public static final String TABLE_NAME = "Days";

        public static Uri buildCurrentDay(int year, int month, int day) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(year))
                    .appendPath(Integer.toString(month))
                    .appendPath(Integer.toString(day))
                    .build();
        }
    }
}
