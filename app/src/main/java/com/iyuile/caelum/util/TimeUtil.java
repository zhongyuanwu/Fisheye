package com.iyuile.caelum.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换
 * 相关参考{@links http://blog.csdn.net/fengyuzhengfan/article/details/40164721}
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_MONTH_DAY = "MM月dd日";
    public final static String FORMAT_MONTH_DAY_2 = "MM/dd";
    public final static String FORMAT_PEAR_MONTH = "yyyy/MM";

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm";

    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_DATE3_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE4_TIME = "yyyy 年 MM 月 dd 日 KK:mm a";
    //2016 年 12 月 22 日 06:15 下午 星期四:::(EEE=周几,EEEE=星期几)
    public final static String FORMAT_DATE5_TIME = "yyyy 年 MM 月 dd 日 KK:mm a EEE";
    public final static String FORMAT_DATE6_TIME = "yyyy-MM-dd HH";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";
    //:::
    public final static String FORMAT_DATE_TIME_SECOND_FILE_NAME = "yyyy_MM_dd_HH_mm_ss_SSS";

    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp2(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
//		System.out.println("timeGap: " + timeGap);
        if (timeGap < 0)
            return longToString(currentTime, FORMAT_YEAR);//FORMAT_DATE
        String timeStr = null;

        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";//
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";//
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "天前";//
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";//
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";//
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
//			timeStr = timeGap+"s";//秒前
        }
        return timeStr;
    }

    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
//		System.out.println("timeGap: " + timeGap);
        if (timeGap < 0)
            return longToString(currentTime, FORMAT_YEAR);//FORMAT_DATE
        String timeStr = null;

        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "y";//年前
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "M";//个月前
        } else if (timeGap > DAY) {// 1天以上
            timeStr = timeGap / DAY + "d";//天前
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "h";//小时前
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "m";//分钟前
        } else {// 1秒钟-59秒钟
//			timeStr = "刚刚";
            timeStr = timeGap + "s";//秒前
        }
        return timeStr;
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        String strTime = "";
        Date date = longToDate(currentTime, formatType);// long类型转成Date类型
        strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为String类型
    public static String stringToString(String currentTime, String currentFormatType, String formatType) {
        String strTime = "";
        Date date = stringToDate(currentTime, currentFormatType); // String类型转成date类型
        strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
     *
     * @param @param  timesamp
     * @param @return
     * @return String
     * @throws
     * @Title: getChatTime
     */
    public static String getChatTime(long timesamp) {
        long clearTime = timesamp * 1000;
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;

            default:
                result = getTime(clearTime);
                break;
        }

        return result;
    }

    /**
     * 日期操作
     *
     * @param date
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String dateOperate(Date date, int year, int month, int day) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.YEAR, year);// 日期减1年
            rightNow.add(Calendar.MONTH, month);// 日期加3个月
            rightNow.add(Calendar.DAY_OF_YEAR, day);// 日期加10天
            Date dt1 = rightNow.getTime();
            String reStr = sdf.format(dt1);
            return reStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}