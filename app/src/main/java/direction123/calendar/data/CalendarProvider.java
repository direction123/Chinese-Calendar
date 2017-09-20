package direction123.calendar.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by fangxiangwang on 9/19/17.
 */

public class CalendarProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CalendarDbHelper mOpenHelper;

    public static final int CODE_MONTH_ALL = 100;
    public static final int CODE_DAYS_ONE_MONTH = 101;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MonthContract.CONTENT_AUTHORITY, MonthContract.PATH_MONTH, CODE_MONTH_ALL);
        matcher.addURI(DayContract.CONTENT_AUTHORITY, DayContract.PATH_DAY, CODE_DAYS_ONE_MONTH);

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
        Log.v("xxxfdfa2", uri.toString());

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
