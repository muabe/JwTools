package com.markjmind.jwtools.util;

import java.util.Date;
import java.util.Locale;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2015-12-29
 */
public class JwDateUtils {

    public static String getFormat(Date date,  String format, Locale locale)
    {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, locale);
        return formatter.format(date);
    }

    public static String getFormat(Date date,  String format)
    {
        return JwDateUtils.getFormat(date, format, Locale.KOREA);
    }


}
