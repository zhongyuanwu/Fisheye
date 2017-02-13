package com.iyuile.caelum.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuile.caelum.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author WangYao
 * @version 1
 * @Description 小米-MIUI
 * @ProjectName Apus
 * @ClassName {@link MIUIUtils}
 * @Date 2016-5-11 下午2:09:24
 */
public class MIUIUtils {

    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 是否小米MIUI系统
     *
     * @return
     */
    public static boolean isMIUI() {
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;

        return isMIUI;
    }

    /**
     * 是否开启了悬浮窗权限
     * <p>
     * 4.4 以上可以直接判断准确
     * <p>
     * 4.4 以下非MIUI直接返回true
     * <p>
     * 4.4 以下MIUI 可 判断 上一次打开app 时 是否开启了悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isMIUIFloatWindowOpAllowed(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return checkOp(context, 24); // 自己写就是24 为什么是24?看AppOpsManager //AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class managerClass = manager.getClass();
                Method method = managerClass.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int isAllowNum = (Integer) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());

                if (AppOpsManager.MODE_ALLOWED == isAllowNum) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @param context
     * @return
     */
    public static Uri getUri(android.content.Intent intent, Context context) throws Exception {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    /**
     * 跳转到应用详情，可以通过应用详情跳转到权限界面(6.0系统测试可用)
     *
     * @param context
     */
    public static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    private static Snackbar snackbar;

    /**
     * 小米系统悬浮框权限提示
     *
     * @param context
     * @param layoutView 显示在那个view上
     */
    public static void isMIUISuspensionWindowPermission(final Context context, View layoutView) {
        try {
            if (MIUIUtils.isMIUI() && !MIUIUtils.isMIUIFloatWindowOpAllowed(context)) {
                snackbar = Snackbar.make(layoutView, context.getString(R.string.miui_float_window_snackbar_prompt), Snackbar.LENGTH_INDEFINITE);
//                snackbar.setAction("开启", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        MIUIUtils.getAppDetailSettingIntent(MainActivity.this);
//                    }
//                });
                View view = snackbar.getView();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, context.getString(R.string.miui_float_window_open_prompt), Toast.LENGTH_LONG).show();
                        MIUIUtils.getAppDetailSettingIntent(context);
                    }
                });
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_yellow));
                TextView tvMessage = (TextView) view.findViewById(R.id.snackbar_text);
                tvMessage.setTextColor(Color.WHITE);
                Drawable d = ContextCompat.getDrawable(context, R.drawable.mark_warning);
                d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
                tvMessage.setCompoundDrawables(d, null, null, null);   // 给TextView左边添加图标
                tvMessage.setCompoundDrawablePadding(PixelUtil.dp2px(6f));
                tvMessage.setGravity(Gravity.CENTER);  // 让文字居中
                Button btnAction = (Button) view.findViewById(R.id.snackbar_action);
                snackbar.show();
            } else if (MIUIUtils.isMIUI() && MIUIUtils.isMIUIFloatWindowOpAllowed(context) && snackbar != null) {
                //开启悬浮框关闭snackbar
                snackbar.dismiss();
            }
        } catch (Exception e) {
        }
    }

}
