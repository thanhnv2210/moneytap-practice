package com.mtvn.common.utilities;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class DateUtil {

    public static final String DATE_ISO_8601 = "yyyy-MM-dd";
    public static final String DATE_TIME_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static LocalDate getDate(String date, String format){
        if(StringUtils.hasText(date) && StringUtils.hasText(format))
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        return null;
    }

    public static LocalDateTime getDateTime(String date, String format){
        if(StringUtils.hasText(date) && StringUtils.hasText(format))
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
        return null;
    }

    public static int differenceInMinutes(Date d1, Date d2) {
        return (int)Math.abs(ChronoUnit.MINUTES.between(d1.toInstant(), d2.toInstant()));
    }

    public static int differenceInSeconds(Date d1, Date d2) {
        return (int)Math.abs(ChronoUnit.SECONDS.between(d1.toInstant(), d2.toInstant()));
    }

    public static Object differenceInMilliSeconds(Date d1, Date d2) {
        return (int)Math.abs(ChronoUnit.MILLIS.between(d1.toInstant(), d2.toInstant()));
    }

    public static int differenceInDays(Date d1, Date d2){
        return (int)Math.abs(ChronoUnit.DAYS.between(d1.toInstant(), d2.toInstant()));
    }

    public static int differenceInMinutes(Temporal ld1, Temporal ld2) {
        return (int)Math.abs(ChronoUnit.MINUTES.between(ld1, ld2));
    }

    public static int differenceInSeconds(Temporal ld1, Temporal ld2) {
        return (int)Math.abs(ChronoUnit.MILLIS.between(ld1, ld2));
    }

    public static int differenceInDays(Temporal ld1, Temporal ld2) {
        return (int)Math.abs(ChronoUnit.DAYS.between(ld1, ld2));
    }

    public static int differenceInMonths(Temporal ld1, Temporal ld2) {
        return (int)Math.abs(ChronoUnit.MONTHS.between(ld1, ld2));
    }

    public static int differenceInYears(Temporal ld1, Temporal ld2) {
        return (int)Math.abs(ChronoUnit.YEARS.between(ld1, ld2));
    }

    public static LocalDateTime beginningOfDay(LocalDateTime dateTime){
        return dateTime.truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime beginningOfDay(ZonedDateTime zonedDateTime) {
        return zonedDateTime.truncatedTo(ChronoUnit.DAYS);
    }

    public static long daysTillNextDayOfMonthInTimeZone(LocalDateTime date, int dayOfMonth) {
        if(date.getDayOfMonth() == dayOfMonth)
            return 0;
        int days = dayOfMonth - date.getDayOfMonth();
        if(days > 0)
            return days;
        LocalDateTime nextMonth = date.plusMonths(1).withDayOfMonth(dayOfMonth);
        return DateUtil.differenceInDays(nextMonth, date);
    }

    public static ZonedDateTime convertUTCToIST(LocalDateTime dateTime) {
        ZonedDateTime utc = dateTime.atZone(ZoneId.of("UTC"));
        return utc.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
    }

    public static ZonedDateTime convertUTCToIST(ZonedDateTime utc) {
        return utc.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
    }

    public static ZonedDateTime convertISTToUTC(LocalDateTime dateTime) {
        ZonedDateTime utc = dateTime.atZone(ZoneId.of("Asia/Kolkata"));
        return utc.withZoneSameInstant(ZoneId.of("UTC"));
    }

    public static ZonedDateTime convertISTToUTC(ZonedDateTime ist) {
        return ist.withZoneSameInstant(ZoneId.of("UTC"));
    }

    public static String formatDate(String inputDate,String formatFrom,String formatTo) {
        LocalDate date=getDate(inputDate, formatFrom);
        if(date!=null && StringUtils.hasText(formatTo)) {
            return date.format(DateTimeFormatter.ofPattern(formatTo));
        }
        return null;
    }

    public static String getDateAsString(ZonedDateTime zoneDateTime, String format) {
        if (zoneDateTime != null && StringUtils.hasText(format)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return zoneDateTime.format(formatter);
        }
        return null;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

    public static String toEnglishDateString(Date date) {
        LocalDate localDate = asLocalDate(date);
        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        return buildEnglishDateString(day, month, year);
    }

    public static String toEnglishDateString(LocalDate date) {
        return buildEnglishDateString(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    private static String[] monthStrings = new String[] {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
    private static String buildEnglishDateString(int day, int month, int year) {
        StringBuilder res = new StringBuilder();
        res.append(day);
        res.append(getOrdinal(day));
        res.append(' ');
        res.append(WordUtils.capitalize(monthStrings[month-1]));
        res.append(" '");
        res.append(year%100);
        return res.toString();
    }

    private static String getOrdinal(int n) {
        if (isTeen(n)) {
            return "th";
        } else if (n%10 == 1){
            return "st";
        } else if (n%10 == 2) {
            return "nd";
        } else if (n%10 == 3) {
            return "rd";
        } else {
            return "th";
        }
    }

    private static boolean isTeen(int n) {
        if (n%100 < 20 && n%100 > 10) {
            return true;
        }
        return false;
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getAsString(LocalDate localDate,String format) {
        if(localDate!=null && StringUtils.hasText(format)) {
            return localDate.format(DateTimeFormatter.ofPattern(format));
        }
        return null;
    }

    public static String convertDateFormat(String date, String from, String to) {
        LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern(from));
        return d.format(DateTimeFormatter.ofPattern(to));
    }

    public static String format(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

}
