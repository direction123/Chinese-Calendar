package direction123.calendar.data;

import java.util.HashMap;
import java.util.Map;

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
    private String mDayLuna;
    private String mDispLongENG;
    private String mDispLongCH;
    private String mFortuneId;
    private String[] mFortuneCh = {
            "今日建日，为一岁之君之义，主健壮、旺相，万物生育、强健、健壮的日子",
            "今日除日，为除旧布新之义，扫除之意，扫除恶煞、去旧迎新的日子",
            "今日满日，为丰收圆满之义，意丰足充盈，丰收、美满的日子",
            "今日平日，为平常平稳之义，意普通的日子",
            "今日定日，为死气不动之义，非专指安定事，也有主死气沉沉欠活力之意",
            "今日执日，为固执之义，固执、执着；品格操守，重视权威",
            "今日破日，为刚旺破败之义，凶日，破败，忌办吉事",
            "今日危日，为危险之义，危机、危险，诸事不宜的日子",
            "今日成日，为成就之义，主成就、成功、完成，万物成就的大吉日子，凡事皆顺",
            "今日收日，为收成之义，收成、收获之意，办事的好日子",
            "今日开日，为开放之义，意开始、开展的日子，适合各种各样事情，办之可成",
            "今日闭日，为坚固之义，关闭、紧闭之意，是日事务宜闭不宜开，宜收不宜放"};
    private String[]  mFortuneEn = {
            "This is the 'building' day, which means it is a day everything is growing stronger.",
            "This is the 'removing' day, which means it is a good day to get rid of old and bad stuff.",
            "This is the 'happy' day, which means it is a good day to gain joy and happiness.",
            "This is the 'common' day, which means it is better to stay quiet today.",
            "This is the 'stagnant' day, which means it is a day lacking of energy and vigour.",
            "This is the 'mataining' day, which means it is better to keep unchanged today.",
            "This is the 'broken' day, which means it is not a good day perform happy event.",
            "This is the 'dangerous' day, which means it is a day nothing is good to do.",
            "This is the 'acomplishing' day, which means it is a day everything is good to do.",
            "This is the 'harvesting' day, which means it is a good day to pursue a happy event.",
            "This is the 'opening' day, which means it is good day to start something new.",
            "This is the 'closing' day, which means it is a good day to finish something."
    };

    public DayModel(String dispTop, String dispShortENG, String dispShortCH,
            String yearDispENG, String yearDispCH, String monthLuna,
            String dayLuna, String dispLongENG, String dispLongCH, String fortuneId) {
        this.mDispTop = dispTop;
        this.mDispShortENG = dispShortENG;
        this.mDispShortCH = dispShortCH;
        this.mYearDispENG = yearDispENG;
        this.mYearDispCH = yearDispCH;
        this.mMonthLuna = monthLuna;
        this.mDayLuna = dayLuna;
        this.mDispLongENG = dispLongENG;
        this.mDispLongCH = dispLongCH;
        this.mFortuneId = fortuneId;
    }

    public String getDispTop() {
        return mDispTop;
    }

    public String getDispShort(String language) {
        if (language.equals("Simplified Chinese")) {
            return mDispShortCH;
        } else {
            return mDispShortENG;
        }
    }

    public String getDispYear(String language) {
        if (language.equals("Simplified Chinese")) {
            return mYearDispCH + " " + mMonthLuna + " " + mDayLuna;
        } else {
            return "The year of " + mYearDispENG;
        }
    }
    public String getDispLong(String language) {
        if (language.equals("Simplified Chinese")) {
            return mDispLongCH;
        } else {
            return mDispLongENG;
        }
    }

    public String getFortune(String language) {
        if (language.equals("Simplified Chinese")) {
            return mFortuneCh[Integer.parseInt(mFortuneId) - 1];
        } else {
            return mFortuneEn[Integer.parseInt(mFortuneId) - 1];
        }
    }
}
