package com.iyuile.caelum.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 省市区json
 * <p/>
 * Created by WangYao on 2016/10/27.
 */
public class ChineseCitiesUtil {

    public static String getChineseCitiesJSON(Context context) {
        try {
            InputStream is = context.getAssets().open("ChineseCities.json");
            return readTextFromSDcard(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private static String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

}
