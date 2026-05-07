package utils.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;

import utils.log.Log;

public class DateUtils {

    // Different date formats in every place
    public enum DateFormatType {
        TEXT, // Format: Feb 21, 2026
        NUMERIC // Format: 21/02/2026
    }

    // Returns date after adding the number of days
    // Input: 12. Output: Day 12 of following month
    // Used to set expiration dates in links and space members
    public static String dateInDaysAndroidFormat(String days) {
        Log.log(Level.FINE, "Starts: Turns days in date");
        int d = Integer.parseInt(days.trim());
        LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(d);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault());
        String dateAfterDays = date.format(fmt);
        Log.log(Level.FINE, "Date formatted: " + dateAfterDays);
        return dateAfterDays;
    }

    // Returns date after adding the number of days
    // Input: 12. Output: 2026-02-12 23:59:59
    // Used to assert expiration dates in the server
    public static String dateInDaysWithServerFormat(String days) {
        Log.log(Level.FINE, "Starts: Turns days in date with server response format");
        int d = Integer.parseInt(days.trim());
        LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(d);
        Log.log(Level.FINE, "Date to format: " + date);
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateAfterDays = endOfDay.format(fmt);
        Log.log(Level.FINE, "Date formatted: " + dateAfterDays);
        return dateAfterDays;
    }

    // Returns date corresponding day in the following month
    // using the available enum types
    // Used to assert expiration dates in the app
    public static String formatDate(String days, DateFormatType format) {
        Log.log(Level.FINE, "Starts: Build shortDate string");
        int d = Integer.parseInt(days.trim());
        LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(d);
        Log.log(Level.FINE, "Date: " + date);
        String result = "";
        if (format == DateFormatType.TEXT) {
            DateTimeFormatter textFmt =
                    DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            result = date.format(textFmt);
        } else if (format == DateFormatType.NUMERIC) {
            DateTimeFormatter numFmt =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            result = date.atTime(23, 59).format(numFmt);
        }
        Log.log(Level.FINE, "Short Date: " + result);
        return result;
    }

    public static String daysToUTCForExpiration (String days) {
        int d = Integer.parseInt(days.trim());
        LocalDate targetDate = LocalDate.now().plusMonths(1).withDayOfMonth(d);
        Instant expirationInstant = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return DateTimeFormatter.ISO_INSTANT.format(expirationInstant);
    }
}
