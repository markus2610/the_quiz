package com.thilek.android.qleneagles_quiz.util;

import android.content.Context;
import android.net.ParseException;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.R;


import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

    public static final int MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

    public static final String getFormattedDate(Calendar c) {
        String dateString = "";
        DateFormatSymbols dfs = new DateFormatSymbols(AppContext.LOCALE);
        String[] months = dfs.getMonths();
        String[] weekdays = dfs.getWeekdays();
        String month = months[c.get(Calendar.MONTH)];
        String weekday = weekdays[c.get(Calendar.DAY_OF_WEEK)];
        dateString = weekday + ", " + c.get(Calendar.DAY_OF_MONTH) + " "
                + month + " " + c.get(Calendar.YEAR);
        return dateString;
    }

    public static int diffDayPeriods(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis()
                + endDate.getTimeZone().getOffset(endDate.getTimeInMillis());
        long start = startDate.getTimeInMillis()
                + startDate.getTimeZone()
                .getOffset(startDate.getTimeInMillis());
        return (int) ((end - start) / MILLISECS_PER_DAY);
    }

    public static String getFormattedDate(long dateMillis) {
        DateFormat timeFormatter = new SimpleDateFormat("d.MM.yyyy", AppContext.LOCALE);
        return timeFormatter.format(dateMillis);
    }

    public static String getFormattedTime(long timeMillis, Locale locale) {
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm", locale);
        return timeFormatter.format(timeMillis);
    }

    public static String getFormattedTimeWithSeconds(long timeMillis,
                                                     Locale locale) {
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss", locale);
        return timeFormatter.format(timeMillis);
    }

    public static String getFormattedDateTime(long dateTimeMillis, Locale locale) {
        return DateUtil.getFormattedDate(dateTimeMillis) + " "
                + DateUtil.getFormattedTime(dateTimeMillis, locale);
    }

    public static String dateStringForEvent(long startAt, long endsAt,
                                            boolean beginDateValid, boolean endDateValid, Locale locale) {
        String dateString = "";
        String dateSeparator = "\n";

        if (!(0 == startAt)) {
            dateString = DateUtil.getFormattedDate(startAt);
            if (beginDateValid)
                dateString = dateString + " "
                        + DateUtil.getFormattedTime(startAt, locale);
            if (!(0 == endsAt)) {
                if (!(DateUtil.getFormattedDate(startAt).equals(DateUtil
                        .getFormattedDate(endsAt)))) {
                    dateString = dateString + " -" + dateSeparator
                            + DateUtil.getFormattedDate(endsAt);
                    if (endDateValid)
                        dateString = dateString + " "
                                + DateUtil.getFormattedTime(endsAt, locale);
                } else {
                    if (endDateValid && !(startAt == endsAt))
                        dateString = dateString + " -" + dateSeparator
                                + DateUtil.getFormattedTime(endsAt, locale);

                }
            }
        }

        return dateString;
    }

    public static String convertMilisToDateTime(String milis, String format,
                                                Locale local) {

        DateFormat dateFormatter = new SimpleDateFormat(format, local);
        Calendar cal = Calendar.getInstance(local);

        long time = Long.valueOf(milis);
        cal.setTimeInMillis(time);
        return dateFormatter.format(cal.getTime());

    }

    public static String getCurrentDateTime(String format) {

        DateFormat dateFormatter = new SimpleDateFormat(format, AppContext.LOCALE);
        Calendar cal = Calendar.getInstance(AppContext.LOCALE);

        long time = Long.valueOf(System.currentTimeMillis());
        cal.setTimeInMillis(time);
        return dateFormatter.format(cal.getTime());

    }

    public static String convertMilisToDateTime(long milis, String format,
                                                Locale local) {

        DateFormat dateFormatter = new SimpleDateFormat(format, local);
        Calendar cal = Calendar.getInstance(local);

        cal.setTimeInMillis(milis);

        return dateFormatter.format(cal.getTime());

    }

    public static Calendar convertMilisToCalender(long milis, Locale local) {

        Calendar calender = Calendar.getInstance(local);
        calender.setTimeInMillis(milis);
        return calender;
    }

    public static long convertToDateTimeToMilis(String dateString,
                                                String format, Locale local) {

        DateFormat dateFormatter = new SimpleDateFormat(format, local);
        long futureTime = 0;

        try {
            Date date = dateFormatter.parse(dateString);
            futureTime = date.getTime();
        } catch (ParseException e) {
            Logs.e("log", "" + e.getMessage());
        } catch (java.text.ParseException e) {
            Logs.e("log", "" + e.getMessage());
        }

        return futureTime;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static String convertTimePickerToString(int currentHour, int currentMinute) {

        String currentHourString = String.valueOf(currentHour);
        String currentMinuteString = String.valueOf(currentMinute);

        if (currentHourString.length() == 1) {
            currentHourString = "0" + currentHourString;
        }
        if (currentMinuteString.length() == 1) {
            currentMinuteString = "0" + currentMinuteString;
        }

        return (currentHourString + ":" + currentMinuteString + ":00");
    }


    public static String formatTimeDifferenceInHours(long time) {
        long diffSeconds = time % 60;
        long diffMinutes = time / (60) % 60;
        long diffHours = time / (60 * 60);


        Logs.d("DateUtil", +diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds.");

        String hour = "0", minute = "0", seconds = "0";

        if (diffHours < 10) {
            hour += String.valueOf(diffHours);
        } else {
            hour = String.valueOf(diffHours);
        }

        if (diffMinutes < 10) {
            minute += String.valueOf(diffMinutes);
        } else {
            minute = String.valueOf(diffMinutes);
        }

        if (diffSeconds < 10) {
            seconds += String.valueOf(diffSeconds);
        } else {
            seconds = String.valueOf(diffSeconds);
        }


        return (hour + ":" + minute + ":" + seconds);
    }


    public static String formatTimeDifferenceInHoursWithoutSeconds(long time,Context context) {
        long diffMinutes = time / (60) % 60;
        long diffHours = time / (60 * 60);


        Logs.d("DateUtil", +diffHours + " hours, " + diffMinutes + " minutes");

        String hour = "0", minute = "0";

        if (diffHours < 10) {
            hour += String.valueOf(diffHours);
        } else {
            hour = String.valueOf(diffHours);
        }

        if (diffMinutes < 10) {
            minute += String.valueOf(diffMinutes);
        } else {
            minute = String.valueOf(diffMinutes);
        }


        return (hour + ":" + minute + " " + context.getString(R.string.general_hours_string));
    }

    public static String formatTimeDifference(long time) {
        long diffSeconds = time % 60;
        long diffMinutes = time / (60) % 60;
        long diffHours = time / (60 * 60) % 24;
        long diffDays = time / (24 * 60 * 60);

        Logs.d("DateUtil", diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds.");

        String hour = "0", minute = "0", seconds = "0";

        if (diffHours < 10) {
            hour += String.valueOf(diffHours);
        } else {
            hour = String.valueOf(diffHours);
        }

        if (diffMinutes < 10) {
            minute += String.valueOf(diffMinutes);
        } else {
            minute = String.valueOf(diffMinutes);
        }

        if (diffSeconds < 10) {
            seconds += String.valueOf(diffSeconds);
        } else {
            seconds = String.valueOf(diffSeconds);
        }


        return (hour + ":" + minute + ":" + seconds);
    }


}
