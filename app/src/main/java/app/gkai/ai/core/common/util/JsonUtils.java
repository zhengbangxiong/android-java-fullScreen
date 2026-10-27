package app.gkai.ai.core.common.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class JsonUtils {

    static Gson gson = new GsonBuilder()
            //.serializeNulls()
            .create();

    public static String toJsonStr(Object object) {
        Log.e("OKHttp-----", gson.toJson(object));
        return gson.toJson(object);
    }

    /**
     * json串拼接
     */
    public static String jsonString(Map<String, String> map) {
        String jsonk = "{";
        String jsons = "";
        int i = 1;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            {
                if (1 == i) {
                    i++;
                    jsons += '\"' + entry.getKey() + '\"' + ":" + '\"' + entry.getValue() + '\"';
                } else {
                    jsons += "," + '\"' + entry.getKey() + '\"' + ":" + '\"' + entry.getValue() + '\"';
                }

            }
        }
        jsonk += jsons + "}";
        return jsonk;
    }

    /**
     * 转换body
     */
    public static RequestBody getJsonBody(String jsonString) {
//        val mediaType = "application/json; charset=utf-8".toMediaType();
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
    }
}
