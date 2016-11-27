package com.naturecurly.zimuzu.Utils;

import android.content.Context;
import android.content.res.Resources;

import com.naturecurly.zimuzu.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leveyleonhardt on 11/26/16.
 */

public class AccessUtils {
    private static final String ACCESSKEY = "f1a1a3a891ccdcfd08038c8678dcab53";
    private static final String CID = "11";
    private static final String CLIENT = "2";

    public static Map<String, String> generateAccessKey(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put(context.getString(R.string.access_cid), CID);
        map.put(context.getString(R.string.access_client), CLIENT);

        String timestamp = String.valueOf(System.currentTimeMillis());
        map.put(context.getString(R.string.access_timestamp), timestamp);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CID).append("$$").append(ACCESSKEY).append("&&").append(timestamp);
        String accesskey = stringBuilder.toString();

        map.put(context.getString(R.string.access_key), md5(accesskey));
        return map;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
