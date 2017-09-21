package direction123.calendar.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fangxiangwang on 9/21/17.
 */

public class CalendarUtils {

    public Map<String, String> getMonthMapping() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "January");
        hashMap.put("2", "February");
        hashMap.put("3", "March");
        hashMap.put("4", "April");
        hashMap.put("5", "May");
        hashMap.put("6", "June");
        hashMap.put("7", "July");
        hashMap.put("8", "Auguest");
        hashMap.put("9", "September");
        hashMap.put("10", "October");
        hashMap.put("11", "November");
        hashMap.put("12", "December");
        return hashMap;
    }
    public Map<String, String> getShortMonthMapping() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "Jan");
        hashMap.put("2", "Feb");
        hashMap.put("3", "March");
        hashMap.put("4", "April");
        hashMap.put("5", "May");
        hashMap.put("6", "June");
        hashMap.put("7", "July");
        hashMap.put("8", "Aug");
        hashMap.put("9", "Sep");
        hashMap.put("10", "Oct");
        hashMap.put("11", "Nov");
        hashMap.put("12", "Dec");
        return hashMap;
    }
}
