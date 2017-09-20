package direction123.calendar.data;

/**
 * Created by fangxiangwang on 9/11/17.
 */

public class DayModel {
    private String mDispTop;
    private String mDispShortENG;
    private String mDispShortCH;
    private String mYearDispENG;
    private String mYearDispCH;
    private String mMonthLuna;
    private String mDayLunar;
    private String mDispLongENG;
    private String mDispLongCH;

    public DayModel(String dispTop, String dispShortENG, String dispShortCH,
            String yearDispENG, String yearDispCH, String monthLuna,
            String dayLunar, String dispLongENG, String dispLongCH) {
        this.mDispTop = dispTop;
        this.mDispShortENG = dispShortENG;
        this.mDispShortCH = dispShortCH;
        this.mYearDispENG = yearDispENG;
        this.mYearDispCH = yearDispCH;
        this.mMonthLuna = monthLuna;
        this.mDayLunar = dayLunar;
        this.mDispLongENG = dispLongENG;
        this.mDispLongCH = dispLongCH;
    }

    public String getDispTop() {
        return mDispTop;
    }

    public String getDispShort(String language) {
        if (language.equals("English")) {
            return mDispShortENG;
        }
        return mDispShortCH;
    }

    public String getDispYear(String language) {
        if (language.equals("English")) {
            return mYearDispENG;
        }
        return mYearDispCH + " " + mMonthLuna + " " + mDayLunar;
    }
    public String getDispLong(String language) {
        if (language.equals("English")) {
            return mDispLongENG;
        }
        return mDispLongCH;
    }
}
