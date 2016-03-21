package com.feedInstagram.utilities;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by santiagomedina on 15/03/16.
 */
public class UtilImages {


    public static final String URL = "https://api.instagram.com/v1/media/popular?client_id=05132c49e9f148ec9b8282af33f88ac7";
    public static final String INTENT_URL = "intent_url";
    public static final String INTENT_POSITION = "intent_position";
    public static final String PREFERENCE_KEY_JSON = "preference_json";
    public static String PREFERENCE_NAME = "com.kogui";

    public static String printPublishDate(long along) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(along * 1000);
        return DateFormat.getDateTimeInstance().format(cal.getTime());
    }

    public static String printTags(String[] tags) {
        StringBuilder result = new StringBuilder();
        for (String tag : tags) {
            result.append(tag).append(",");
        }
        return result.toString();
    }


}
