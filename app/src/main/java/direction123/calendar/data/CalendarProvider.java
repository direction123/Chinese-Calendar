package direction123.calendar.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by fangxiangwang on 9/19/17.
 */

public class CalendarProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CalendarDbHelper mOpenHelper;

    public static final int CODE_MONTH_ALL = 100;
    public static final int CODE_DAY_MODELS_MONTH = 103;

    public static final int CODE_MONTH_DAY_MODELS= 104;


    public static final int CODE_DAYS_ONE_MONTH = 101;
    public static final int CODE_CURRENT_DAY = 102;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MonthContract.CONTENT_AUTHORITY, MonthContract.PATH_MONTH, CODE_MONTH_ALL);
        matcher.addURI(MonthContract.CONTENT_AUTHORITY, MonthContract.PATH_MONTH + "/#", CODE_DAY_MODELS_MONTH);

        matcher.addURI(MonthContract.CONTENT_AUTHORITY, MonthContract.PATH_MONTH + "/#/#", CODE_MONTH_DAY_MODELS);


        matcher.addURI(DayContract.CONTENT_AUTHORITY, DayContract.PATH_DAY, CODE_DAYS_ONE_MONTH);
        matcher.addURI(DayContract.CONTENT_AUTHORITY, DayContract.PATH_DAY + "/#" + "/#" + "/#", CODE_CURRENT_DAY);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CalendarDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_MONTH_ALL: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MonthContract.MonthEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_DAYS_ONE_MONTH: {
                String queryString =
                        "SELECT * FROM Days WHERE DayId >= ? AND DayID <= ?";
                cursor = mOpenHelper.getReadableDatabase().rawQuery(queryString, selectionArgs);

                break;
            }
            case CODE_CURRENT_DAY: {
                List<String> calendarString = uri.getPathSegments();
                String[] selectionArguments = new String[]{
                        calendarString.get(1), calendarString.get(2), calendarString.get(3)
                };
                String where = "YearGG = ? AND MonthGG = ? AND DayGG = ?";
                cursor = mOpenHelper.getReadableDatabase().query(
                        DayContract.DayEntry.TABLE_NAME,
                        projection,
                        where,
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_MONTH_DAY_MODELS: {
                List<String> dates = uri.getPathSegments();
                String year = dates.get(1);
                String month = dates.get(2);

                String queryString =
                        "SELECT * FROM Month LEFT JOIN Days ON Month.DaysInMonth = Days.DayId" +
                                " WHERE Month.Year = " + year +" AND Month.Month = " + month;
                cursor = mOpenHelper.getReadableDatabase().rawQuery(queryString, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
