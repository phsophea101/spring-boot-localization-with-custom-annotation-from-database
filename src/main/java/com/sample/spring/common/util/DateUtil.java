package com.sample.spring.common.util;


import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtil extends DateUtils {

    public static final String DATE_WITH_TIME_1 = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_WITH_TIME_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_WITH_TIME_3 = "yyyy-MM-dd HH:mm:ss.S";
    public static final String DATE_NO_TIME = "yyyy-MM-dd";
    public static final String DATE_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static Date nowWithoutTime() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

    public static Date withoutMilliseconds(Date data) {
        return DateUtils.truncate(data, Calendar.SECOND);
    }

    public static Date withoutMinute(Date data) {
        return DateUtils.truncate(data, Calendar.MINUTE);
    }

    public static String format(Date date) {
        return format(date, DATE_WITH_TIME_1);
    }

    public static String format(Date date, String format) {
        return format(date, format, null);
    }

    public static String format(Date date, String format, String defaultValue) {
        return date == null ? defaultValue : new SimpleDateFormat(format).format(date);
    }

    public static Date getToday() {
        return getToday(DATE_NO_TIME);
    }

    public static Date getToday(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2014-08-06
        try {
            return dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateTime() {
        return getToday(DATE_WITH_TIME_2);
    }

    public static String getNow() {
        return getNow(DATE_WITH_TIME_2);
    }

    public static String getNow(String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2014-08-06 15:59:48
        return dateFormat.format(date);
    }

    public static String dayOfToday() {
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //31
        return dateFormat.format(date);
    }

    public static String getNowAsString() {
        return getNowAsString(DATE_WITH_TIME_2);
    }

    public static String getNowAsString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);//2014-08-06 15:59:48
        return dateFormat.format(new Date());
    }

    public static String getExpireDateByNday(int nDay) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, nDay);
        Date day = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat(DATE_NO_TIME);
        System.out.println(dateFormat.format(day)); //2014-08-06 15:59:48
        return dateFormat.format(day);
    }

    public static String getDateTimeWithTimeZone(Date now) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_WITH_TIMEZONE) {
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length() - 2, ':');
            }

        };
        return dateFormat.format(new Date()).toString();
    }

    public static String getFormatDate(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_NO_TIME);
        return sdfDate.format(date);
    }

    public static String getFormatDateTime(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_WITH_TIME_2);
        return sdfDate.format(date);
    }

    public static Date getDrawCloseDate(String drawCloseDate) {
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat(DATE_NO_TIME);
        try {
            date = dateFormat.parse(drawCloseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateTrackTime(String timeTrack) {
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat(DATE_WITH_TIME_3);
        try {
            date = dateFormat.parse(timeTrack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMonthAndYear() {
        Date date = new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM-yyyy");
        return sdfDate.format(date);
    }

    public static String getLastDayOfMonth() {
        // getValue a calendar object
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = new Date();
        SimpleDateFormat fmonth = new SimpleDateFormat("MM");
        String month = fmonth.format(date);
        SimpleDateFormat fyear = new SimpleDateFormat("yyyy");
        String year = fyear.format(date);
        // convert the year and month to integers
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);

        // adjust the month for a zero based index
        monthInt = monthInt - 1;

        // set the date of the calendar to the date provided
        calendar.set(yearInt, monthInt, 1);

        int dayInt = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return Integer.toString(dayInt);
    } // end getLastDay method

    public static String getFormatDateTime24(Date date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfDate.format(date);
    }

    public static Date getDateStartOfWeek() {
        Calendar calWeekly = Calendar.getInstance();
        Integer dayOfStartOfWeek = Calendar.SUNDAY;
        Integer dayOfCurrentOfWeek = calWeekly.get(Calendar.DAY_OF_WEEK);
        Integer leftDaysOfWeek = dayOfStartOfWeek - dayOfCurrentOfWeek;
        calWeekly.add(Calendar.DATE, leftDaysOfWeek);
        return calWeekly.getTime();
    }

    public static Date getDateEndOfWeek() {
        Calendar calWeekly = Calendar.getInstance();
        Integer dayOfEndOfWeek = Calendar.SATURDAY;
        Integer dayOfCurrentOfWeek = calWeekly.get(Calendar.DAY_OF_WEEK);
        Integer leftDaysOfWeek = dayOfEndOfWeek - dayOfCurrentOfWeek;
        calWeekly.add(Calendar.DATE, leftDaysOfWeek);
        return calWeekly.getTime();
    }

    public static Date getDateStartOfMonth() {
        Calendar calMonthly = Calendar.getInstance();
        Integer dayOfStartOfMonth = calMonthly.getActualMinimum(Calendar.DAY_OF_MONTH);
        Integer dayOfCurrentOfMonth = calMonthly.get(Calendar.DAY_OF_MONTH);
        Integer leftDaysOfWeek = dayOfStartOfMonth - dayOfCurrentOfMonth;
        calMonthly.add(Calendar.DATE, leftDaysOfWeek);
        return calMonthly.getTime();
    }

    public static Date getDateEndOfMonth() {
        Calendar calMonthly = Calendar.getInstance();
        Integer dayOfEndOfMonth = calMonthly.getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer dayOfCurrentOfMonth = calMonthly.get(Calendar.DAY_OF_MONTH);
        Integer leftDaysOfWeek = dayOfEndOfMonth - dayOfCurrentOfMonth;
        calMonthly.add(Calendar.DATE, leftDaysOfWeek);
        return calMonthly.getTime();
    }

    public static Timestamp asTimeStamp(LocalDate localDate) {
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String getAnyValueFromDate(Date date, String format) {
        return date == null ? null : new SimpleDateFormat(format).format(date);
    }

    public static String getDay(Date date) {
        return getAnyValueFromDate(date, "EEEE");
    }

    public static String getDateNumber(Date date) {
        return getAnyValueFromDate(date, "dd");
    }

    public static String getMonth(Date date) {
        return getAnyValueFromDate(date, "MMMM");
    }

    public static String getYear(Date date) {
        return getAnyValueFromDate(date, "YYYY");
    }

    public static Long betweenDates(Date firstDate, Date secondDate) {
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }

    public static int getYear() {
        SimpleDateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        return Integer.parseInt(df.format(Calendar.getInstance().getTime()));
    }

    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }
}
