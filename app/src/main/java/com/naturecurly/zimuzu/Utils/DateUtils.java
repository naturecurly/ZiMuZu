package com.naturecurly.zimuzu.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class DateUtils {
    public static String timestamp2String(String timestamp) {
        Date date = new Date(Integer.valueOf(timestamp) * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }
}
