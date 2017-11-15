package direction123.calendar.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fangxiangwang on 9/19/17.
 */

public class MonthContract {

    public static final String CONTENT_AUTHORITY = "direction123.calendar";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MONTH = "Month";

    public static final class MonthEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MONTH)
                .build();

        public static final String TABLE_NAME = "Month";

        public static Uri buildMonthUriWithYearMonth(int year, int month) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(year))
                    .appendPath(String.valueOf(month))
                    .build();
        }
    }
}
