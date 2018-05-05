package com.beaconify.detect.beaconify.Helper;

import com.google.gson.Gson;

public class JsonHelper {
    private static JsonHelper instance = null;
    private Gson gson;

    private JsonHelper() {
        gson = new Gson();
    }

    public static JsonHelper getInstance() {
        if (instance == null) {
            instance = new JsonHelper();
        }
        return instance;
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

    public Object toObject(String string, Class type) {
        return gson.fromJson(string, type);
    }
}
