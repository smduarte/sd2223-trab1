package utils;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


final public class JSON {
    private static final Gson gson = new Gson();

    synchronized public static final String encode(Object obj) {
        return gson.toJson(obj);
    }

    synchronized public static final <T> T decode(String json, Class<T> classOf) {
        return gson.fromJson(json, classOf);
    }

    @SuppressWarnings("unchecked")
    synchronized public static <T> T decode(String key, TypeToken<?> typeOf) {
        return (T) gson.fromJson(key, typeOf.getType());
    }

    @SuppressWarnings("unchecked")
    synchronized public static Map<String, Object> toMap(Object x) {
        return decode(encode(x), Map.class);
    }
}
