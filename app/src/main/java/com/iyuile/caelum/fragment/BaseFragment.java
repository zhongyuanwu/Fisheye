package com.iyuile.caelum.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fastaccess.permission.base.PermissionFragmentHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.iyuile.alertdialog.AlertDialog;
import com.iyuile.caelum.R;
import com.iyuile.caelum.utils.Log;
import com.iyuile.caelum.utils.MyApplication;
import com.iyuile.caelum.utils.ScreenUtils;
import com.iyuile.caelum.view.HeaderLayout;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * Fragmenet 基类
 *
 * @author smile
 * @ClassName: FragmentBase
 * @date 2015-07-16 20:04:42
 */
public abstract class BaseFragment extends SupportFragment implements OnPermissionCallback {

    protected HeaderLayout mHeaderLayout;
    protected MyApplication mApplication;

    protected View contentView;

    public LayoutInflater mInflater;

    private Handler handler = new Handler();

    protected PermissionFragmentHelper permissionFragmentHelper;

    public void runOnWorkThread(Runnable action) {
        new Thread(action).start();
    }

    public void runOnUiThread(Runnable action) {
        handler.post(action);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mApplication = MyApplication.getInstance();
        mInflater = LayoutInflater.from(getActivity());
        permissionFragmentHelper = PermissionFragmentHelper.getInstance(this);
    }

    /**
     * 自定义状态栏显示
     *
     * @param activity
     * @return
     */
    protected View setStatusBar(Activity activity) {
        View statusBar = activity.findViewById(R.id.v_status_bar);
        return setStatusBar(activity, statusBar);
    }

    protected View setStatusBar(Activity activity, View statusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = statusBar.getLayoutParams();
            lp.height = ScreenUtils.getStatusHeight(activity);
            statusBar.setLayoutParams(lp);
            return statusBar;
        }
        return null;
    }

    public BaseFragment() {

    }

    Toast mToast;
    Toast mToastImage;

    public void showToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }

    public void showToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getActivity(), resId,
                            Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    public void startAnimActivity(Class<?> cla) {
        getActivity().startActivity(new Intent(getActivity(), cla));
    }

    public void startAnimActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    /**
     * 设置actionBar背景
     *
     * @param color
     */
    public void setActionBarBackgroundColor(int color) {
        mHeaderLayout.setActionBarBackgroundColor(color);
    }

    /**
     * 设置actionBar背景
     *
     * @param resid
     */
    public void setActionBarBackgroundResource(int resid) {
        mHeaderLayout.setActionBarBackgroundResource(resid);
    }

    /**
     * 设置标题文本
     *
     * @param title
     */
    public void setActionBarTitle(CharSequence title) {
        mHeaderLayout.setDefaultTitle(title);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setActioJnBarTitleColor(int color) {
        mHeaderLayout.setTitleColor(color);
    }

    /**
     * 只有title initTopBarLayoutByTitle
     *
     * @throws
     * @Title: initTopBarLayoutByTitle
     */
    public void initTopBarForOnlyTitle(String titleName) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    /**
     * 初始化标题栏-带左右按钮
     *
     * @return void
     * @throws
     */
    public void initTopBarForBoth(String titleName, int rightDrawableId, HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName, R.drawable.base_action_bar_back_bg_selector, new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId, listener);
    }

    /**
     * :::
     * 初始化标题栏-带左右文字按钮
     *
     * @return void
     * @throws
     */
    public void initTopBarForBothTxt(String titleName, Typeface leftTypeFace, int rightTxtId, Typeface rightTypeFace, HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_TXTBUTTON);
        mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.drawable.base_action_bar_back_bg_selector, leftTypeFace, new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightTxtButton(titleName, rightTxtId, rightTypeFace, listener);
    }

    /**
     * :::
     * 初始化标题栏-带左右文字按钮::: version 2(副文本)
     *
     * @return void
     * @throws
     */
    public void initTopBarForBothSecondaryTxt(String titleName, Typeface leftTypeFace, Typeface leftSecondaryTypeFace, int rightTxtId, Typeface rightTypeFace, int rightSecondaryTxtId, Typeface rightSecondaryTypeFace, HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_TXTBUTTON);
        mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_icon, leftTypeFace, new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, R.string.head_layout_left_return_secondary, leftSecondaryTypeFace, new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightTxtButton(titleName, rightTxtId, rightTypeFace, listener);
        mHeaderLayout.setTitleAndRightSecondaryTxtButton(titleName, rightSecondaryTxtId, rightSecondaryTypeFace, listener);
    }

    /**
     * 初始化标题栏-带左右按钮(左右也重写事件):::version 1
     *
     * @param titleName       标题
     * @param leftDrawableId  左图标资源
     * @param leftListener    左事件
     * @param rightDrawableId 右图标资源
     * @param rightListener   右事件
     */
    public void initTopBarForBoth(String titleName, int leftDrawableId, HeaderLayout.onLeftImageButtonClickListener leftListener, int rightDrawableId, HeaderLayout.onRightImageButtonClickListener rightListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName, leftDrawableId, leftListener);
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId, rightListener);
    }

    /**
     * :::
     * 初始化标题栏-带左右文字icon按钮(左右也重写事件):::version 1
     *
     * @param titleName     标题
     * @param leftTxtId     左图标资源
     * @param leftTypeface
     * @param leftListener  左事件
     * @param rightTxtId
     * @param rightTypeface 右图标资源
     * @param rightListener 右事件
     */
    public void initTopBarForBothTxt(String titleName, int leftTxtId, Typeface leftTypeface, HeaderLayout.onLeftImageButtonClickListener leftListener, int rightTxtId, Typeface rightTypeface, HeaderLayout.onRightImageButtonClickListener rightListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_TXTBUTTON);
        mHeaderLayout.setTitleAndLeftTxtButton(titleName, leftTxtId, leftTypeface, leftListener);
        mHeaderLayout.setTitleAndRightTxtButton(titleName, rightTxtId, rightTypeface, rightListener);
    }

    /**
     * :::
     * 初始化标题栏-带左右文字icon按钮(左右也重写事件)::: version 2(副文本)
     */
    public void initTopBarForBothSecondaryTxt(String titleName, int leftTxtId, int leftSecondaryTxtId, Typeface leftTypeface, Typeface leftSecondaryTypeface, HeaderLayout.onLeftImageButtonClickListener leftListener, int rightTxtId, int rightSecondaryTxtId, Typeface rightTypeface, Typeface rightSecondaryTypeface, HeaderLayout.onRightImageButtonClickListener rightListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_TXTBUTTON);
        mHeaderLayout.setTitleAndLeftTxtButton(titleName, leftTxtId, leftTypeface, leftListener);
        mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, leftSecondaryTxtId, leftSecondaryTypeface, leftListener);
        mHeaderLayout.setTitleAndRightTxtButton(titleName, rightTxtId, rightTypeface, rightListener);
        mHeaderLayout.setTitleAndRightSecondaryTxtButton(titleName, rightSecondaryTxtId, rightSecondaryTypeface, rightListener);
    }

    /**
     * 只有左边按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    /**
     * :::
     * 只有左边文字按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeftTxt(String titleName, Typeface typeFace) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_TXTBUTTON);
        mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_text, typeFace, new OnLeftButtonClickListener());
    }

    /**
     * :::
     * 只有左边文字按钮和Title initTopBarLayout::: version 2(副文本)
     *
     * @throws
     */
    public void initTopBarForLeftSecondaryTxt(String titleName, Typeface typeFace, Typeface typeFaceSecondary) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_TXTBUTTON);
        mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_text, typeFace, new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, R.string.head_layout_left_return_secondary, typeFaceSecondary, new OnLeftButtonClickListener());
    }

    /**
     * 右边+title initTopBarForRight
     *
     * @return void
     * @throws
     */
    public void initTopBarForRight(String titleName, int rightDrawableId,
                                   HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    /**
     * 右边文字icon+title initTopBarForRight
     * ::
     *
     * @return void
     * @throws
     */
    public void initTopBarForRightTxt(String titleName, int rightTxtId, Typeface typeface, HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_RIGHT_TXTBUTTON);
        mHeaderLayout.setTitleAndRightTxtButton(titleName, rightTxtId, typeface, listener);
    }

    /**
     * 右边文字+title initTopBarForRight::: version 2(副文本)
     * ::
     *
     * @return void
     * @throws
     */
    public void initTopBarForRightSecondaryTxt(String titleName, int rightTxtId, Typeface typeface, int rightSecondaryTxtId, Typeface typefaceSecondary, HeaderLayout.onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_RIGHT_TXTBUTTON);
        mHeaderLayout.setTitleAndRightTxtButton(titleName, rightTxtId, typeface, listener);
        mHeaderLayout.setTitleAndRightSecondaryTxtButton(titleName, rightSecondaryTxtId, typefaceSecondary, listener);
    }

    /**
     * :::
     * 只有左边icon按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeftIcon(String titleName, int icon) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_TXTBUTTON);
        if (icon != 0)
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, icon, mApplication.getIconStyleFont(), new OnLeftButtonClickListener());
        else
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_icon, mApplication.getIconStyleFont(), new OnLeftButtonClickListener());
    }

    /**
     * :::
     * 只有左边icon按钮和Title initTopBarLayout::: version 2(副文本)
     *
     * @throws
     */
    public void initTopBarForLeftSecondaryIcon(String titleName, int icon, int leftSecondaryTxtId, Typeface typefaceSecondary) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_TXTBUTTON);
        if (icon != 0) {
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, icon, mApplication.getIconStyleFont(), new OnLeftButtonClickListener());
            mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, leftSecondaryTxtId, typefaceSecondary, new OnLeftButtonClickListener());
        } else {
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_icon, mApplication.getIconStyleFont(), new OnLeftButtonClickListener());
            mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, R.string.head_layout_left_return_secondary, typefaceSecondary, new OnLeftButtonClickListener());

        }
    }

    /**
     * :::
     * 只有左边icon按钮和Title(带事件) initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeftIconAndListener(String titleName, int icon, HeaderLayout.onLeftImageButtonClickListener leftListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_TXTBUTTON);
        if (icon != 0)
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, icon, mApplication.getIconStyleFont(), leftListener);
        else
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_icon, mApplication.getIconStyleFont(), leftListener);
    }

    /**
     * :::
     * 只有左边icon按钮和Title(带事件) initTopBarLayout::: version 2(副文本)
     *
     * @throws
     */
    public void initTopBarForLeftIconAndListener(String titleName, int icon, int leftSecondaryTxtId, Typeface typefaceSecondary, HeaderLayout.onLeftImageButtonClickListener leftListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LEFT_TXTBUTTON);
        if (icon != 0) {
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, icon, mApplication.getIconStyleFont(), leftListener);
            mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, leftSecondaryTxtId, typefaceSecondary, leftListener);
        } else {
            mHeaderLayout.setTitleAndLeftTxtButton(titleName, R.string.head_layout_left_return_icon, mApplication.getIconStyleFont(), leftListener);
            mHeaderLayout.setTitleAndLeftSecondaryTxtButton(titleName, R.string.head_layout_left_return_secondary, typefaceSecondary, leftListener);
        }
    }

    /**
     * 设置两边事件
     *
     * @param leftListener
     * @param rightListener
     */
    public void initTopBarForBothLinener(HeaderLayout.onLeftImageButtonClickListener leftListener, HeaderLayout.onRightImageButtonClickListener rightListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.setDoubleListener(leftListener, rightListener);
    }

    /**
     * 设置左事件
     *
     * @param leftListener
     */
    public void initTopBarForLeftLinener(HeaderLayout.onLeftImageButtonClickListener leftListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.setLeftListener(leftListener);
    }

    /**
     * 设置右事件
     *
     * @param rightListener
     */
    public void initTopBarForRightLinener(HeaderLayout.onRightImageButtonClickListener rightListener) {
        mHeaderLayout = (HeaderLayout) getActivity().findViewById(R.id.common_actionbar);
        mHeaderLayout.setRightListener(rightListener);
    }

    // 左边按钮的点击事件
    public class OnLeftButtonClickListener implements
            HeaderLayout.onLeftImageButtonClickListener {

        @Override
        public void onClick() {
            getActivity().finish();
        }
    }

    /**
     * 获取该activity所有view
     */
    public List<View> getAllChildViews(Activity activity) {
        View view = activity.getWindow().getDecorView();
        return getAllChildViews(view);
    }

    private List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    /**
     * 设置该activity所有icon(TextView)字体为iconfont.ttf
     *
     * @return
     */
    public void setAllIconTextViewsFont(Activity activity) {
        List<View> views = getAllChildViews(activity);
        for (View view : views) {
            if (view instanceof TextView) {
                try {
                    String viewIdName = getResources().getResourceEntryName(view.getId());
                    if (viewIdName.startsWith("icon_"))
                        ((TextView) view).setTypeface(mApplication.getIconStyleFont());
                } catch (Resources.NotFoundException e) {
                }
            }
        }
    }

    // ---------------permission---start------------

    /**
     * 允许
     *
     * @param permissionName
     */
    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        Log.i(":::onPermissionGranted", "允许");
    }

    /**
     * 拒绝
     *
     * @param permissionName
     */
    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        Log.i(":::onPermissionDeclined", "拒绝");
    }

    /**
     * 预授权
     *
     * @param permissionsName
     */
    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        Log.i(":::onPermissionPreGranted", "预授权");

    }

    private AlertDialog mDialogPermission;

    /**
     * 需求说明
     *
     * @param permissionName
     */
    @Override
    public void onPermissionNeedExplanation(@NonNull final String permissionName) {
        mDialogPermission = getAlertDialog(permissionName, false).create();
        mDialogPermission.setCanceledOnTouchOutside(false);// 按对话框以外的地方不起作用。按返回键还起作用
        mDialogPermission.setCancelable(false);// 按对话框以外的地方不起作用。按返回键也不起作用=false
        if (!mDialogPermission.isShowing())
            mDialogPermission.show();
    }

    /**
     * 只能从SettingsScreen中授予
     *
     * @param permissionName
     */
    @Override
    public void onPermissionReallyDeclined(@NonNull final String permissionName) {
        mDialogPermission = getAlertDialog(permissionName, true).create();
        mDialogPermission.setCanceledOnTouchOutside(false);// 按对话框以外的地方不起作用。按返回键还起作用
        mDialogPermission.setCancelable(false);// 按对话框以外的地方不起作用。按返回键也不起作用=false
        if (!mDialogPermission.isShowing())
            mDialogPermission.show();
    }

    private AlertDialog.Builder builder;

    /**
     * @param permissionName
     * @param isReallyDeclined 是否用户选择了不再提醒(只能从SettingsScreen中授予)
     * @return
     */
    public AlertDialog.Builder getAlertDialog(final String permissionName, final boolean isReallyDeclined) {
        if (builder == null) {
            builder = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.alert_dialog_permission_prompt_title));
        }
        builder.setPositiveButton(
                getString(R.string.alert_dialog_permission_prompt_request),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isReallyDeclined) {
                            permissionFragmentHelper.openSettingsScreen();
                        } else {
                            permissionFragmentHelper.requestAfterExplanation(permissionName);
                        }
                    }
                });

        if (permissionName.equals(Manifest.permission.READ_PHONE_STATE))
            builder.setMessage(getString(isReallyDeclined ? R.string.alert_dialog_permission_prompt_content_settings_screen_READ_PHONE_STATE : R.string.alert_dialog_permission_prompt_content_READ_PHONE_STATE));
        else if (permissionName.equals(Manifest.permission.ACCESS_COARSE_LOCATION))
            builder.setMessage(getString(isReallyDeclined ? R.string.alert_dialog_permission_prompt_content_settings_screen_ACCESS_COARSE_LOCATION : R.string.alert_dialog_permission_prompt_content_ACCESS_COARSE_LOCATION));
        else if (permissionName.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            builder.setMessage(getString(isReallyDeclined ? R.string.alert_dialog_permission_prompt_content_settings_screen_WRITE_EXTERNAL_STORAGE : R.string.alert_dialog_permission_prompt_content_WRITE_EXTERNAL_STORAGE));
        else
            builder.setMessage(getString(R.string.alert_dialog_permission_prompt_content_settings_screen));

        return builder;
    }

    /**
     * 不需要权限
     */
    @Override
    public void onNoPermissionNeeded() {
        Log.i(":::onNoPermissionNeeded", "不需要权限");
    }

    // ---------------permission---end------------

}
