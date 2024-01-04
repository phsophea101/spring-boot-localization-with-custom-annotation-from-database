package com.sample.spring.common.util;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

@UtilityClass
public class MapperUtils {

    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String getJsonStringify(Object object) {
        return gson.toJson(object);
    }

    public <T> T map(Object object, Class<T> classOfT) {
        return gson.fromJson(getJsonStringify(object), classOfT);
    }

    public <T> T map(String jsonStringify, Class<T> classOfT) {
        return gson.fromJson(jsonStringify, classOfT);
    }

    public Map<String, Object> getJsonKeyValue(Object object) {
        return getJsonKeyValue(object, Strings.EMPTY);
    }

    public Map<String, Object> getJsonKeyValue(String jsonString) {
        return getJsonKeyValue(jsonString, Strings.EMPTY);
    }

    public Map<String, Object> getJsonKeyValue(Object object, String prefix) {
        return getJsonKeyValue(gson.toJson(object), prefix);
    }    

    public Map<String, Object> getJsonKeyValue(String jsonString, String prefix) {
        return getJsonKeyValue(jsonString, prefix, false);
    }

    public Map<String, Object> getJsonKeyValue(String json, String prefix, boolean getSubChildKV) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json.trim());
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.get(key) instanceof JSONObject && getSubChildKV)
                result.putAll(getJsonKeyValue(jsonObject.get(key).toString(), prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key) + ".", true));
            else if (jsonObject.get(key) instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) jsonObject.get(key);
                result.put(prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key), jsonArray.toList());
            }
            else
                result.put(prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key), jsonObject.get(key));
        }
        return result;
    }

    public List<String> arrayList(String... values) {
        if (Objects.isNull(values)) return new ArrayList<>();
        return Arrays.asList(values);
    }

    public List<String> splitString(String source, String regex) {
        if (Objects.isNull(source)) return new ArrayList<>();
        return arrayList(source.split(regex));
    }

}
