package com.naturecurly.zimuzu.Utils;


import android.util.Base64;

/**
 * Created by leveyleonhardt on 11/30/16.
 */

public class Base64Utils {
    public static String encode(String s) {
        byte[] coded = Base64.encode(s.getBytes(), Base64.NO_WRAP);
        String result = new String(coded);
        return result;
    }
}
