package com.zhanchen.main.utils;

import com.google.gson.Gson;

public class JsonArrayUtil {

    public static String toJson(Object obj) {
            Gson gson = new Gson();
            String obj2 = gson.toJson(obj);
            return obj2;
    }
}
