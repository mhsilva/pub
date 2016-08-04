package com.pub.pubcustomer.utils;

import com.google.gson.Gson;

/**
 * Created by Fernando Santiago on 03/08/2016.
 */
public class PubGsonUtils {

    public static String objectToJson(Object object){

        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
