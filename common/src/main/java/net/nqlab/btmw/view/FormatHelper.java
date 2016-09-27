package net.nqlab.btmw.view;

import net.nqlab.btmw.api.SerDes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatHelper
{
    public static String formatDistance(double val) {
        return String.format(Locale.JAPAN, "%.1f", val) + "km";
    }

    public static String formatDistane(int val) {
        return val + "km";
    }

    public static String formatElevation(double val) {
        return Math.round(val) + "m";
    }

    public static String formatElevation(int val) {
        return val + "m";
    }

    public static String formatSpeed(double targetSpeed) {
        return Math.round(targetSpeed) + "km/h";
    }

    public static String formatTimeAddition(int timeAddition) {
        return String.format(
                Locale.JAPAN,
                "%02d:%02d",
                timeAddition / 3600, (timeAddition % 3600) / 60
            );
    }

    public static String formatTime(SerDes serDes, String time) {
        Date date = serDes.fromStringToDate(time);
        return new SimpleDateFormat("HH:mm", Locale.JAPAN).format(date);
    }

    public static String formatRestTime(double time) {
        return Math.round(time * 60.0) + "min";
    }

    public static String formatRestComment(double time) {
        return (time > 0 ? " (" + formatRestTime(time) + ")" : "");
    }
}