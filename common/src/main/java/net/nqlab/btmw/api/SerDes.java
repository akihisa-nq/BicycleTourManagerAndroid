package net.nqlab.btmw.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.Gson;

import org.apache.commons.lang3.time.FastDateFormat;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SerDes {
    private Gson mGson;
    private FastDateFormat mFormat;

	public SerDes() {
        mGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();
        mFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.JAPANESE);
	}

	public Gson getGson() {
		return mGson;
	}

    public String toJson(Object obj)
    {
        return mGson.toJson(obj);
    }

    public Object fromJson(String str, Type type)
    {
        return mGson.fromJson(str, type);
    }

    public Date fromStringToDate(String strDate) {
        try {
            return mFormat.parse(strDate);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String fromDateToString(Date date) {
        return mFormat.format(date);
    }
}
