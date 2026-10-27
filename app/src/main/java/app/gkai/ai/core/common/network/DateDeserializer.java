package app.gkai.ai.core.common.network;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class DateDeserializer implements JsonDeserializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json != null) {
            final String jsonString = json.getAsString();
            try {
                return dateFormat.parse(jsonString);
            } catch (ParseException e) {
                Log.e("GSON---------","GSON日期反序列化失败");
            }
            final long jsonLong= json.getAsLong();
            try {
                return new Date(jsonLong);
            } catch (Exception e) {
                Log.e("GSON---------","GSON日期反序列化失败");
            }
        }
        return null;
    }
}