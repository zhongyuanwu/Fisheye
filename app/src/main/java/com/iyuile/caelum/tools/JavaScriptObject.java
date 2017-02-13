package com.iyuile.caelum.tools;

import android.os.Message;
import android.webkit.JavascriptInterface;

import com.iyuile.caelum.activity.MallWareDetailedActivity;

/**
 * @author WangYao
 * @version 1
 * @Description js调用android的方法
 * @ClassName {@link JavaScriptObject}
 * @Date 2016-12-16 16:40:40
 * TODO :::warning 这个类不能混淆(js去调用postMessage(String)方法)
 */
public class JavaScriptObject {

    public JavaScriptObject() {
    }

    @JavascriptInterface
    public boolean postMessage(String modelIdStr) {
        try {
            long modelId = Long.valueOf(modelIdStr);
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = modelId;
            MallWareDetailedActivity.mInstance.handler.sendMessage(msg);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
