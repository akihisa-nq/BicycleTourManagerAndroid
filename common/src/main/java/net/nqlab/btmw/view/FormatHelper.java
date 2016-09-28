package net.nqlab.btmw.view;

import android.content.Context;

import net.nqlab.btmw.api.SerDes;
import net.nqlab.btmw.R;

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
		int timeAdditionAbs = Math.abs(timeAddition);
        return String.format(
                Locale.JAPAN,
                (timeAddition >= 0 ? "+" : "-") + "%02d:%02d",
                timeAdditionAbs / 3600, (timeAdditionAbs % 3600) / 60
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

	public static int getLeftTimeSeconds(SerDes serDes, String base, String plan, String start) {
        Date baseDate = serDes.fromStringToDate(base);
        Date planDate = serDes.fromStringToDate(plan);
        Date startDate = serDes.fromStringToDate(start);
		Date now = new Date();

		long addition = (planDate.getTime() - baseDate.getTime());
		long spent = (now.getTime() - startDate.getTime());

		return (int)((addition - spent) / 1000);
	}

	public static int getColorForLeftTime(Context context, int time) {
		if (time < 0) {
			return context.getResources().getColor(R.color.over_time);
		} else {
			return context.getResources().getColor(R.color.in_time);
		}
	}
}
