package com.iyuile.caelum.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.alertdialog.AlertDialog;
import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.FeedbackActivity;
import com.iyuile.caelum.activity.WebActivity;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.contants.AppConstants;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.util.AppInfo;
import com.iyuile.caelum.util.ButtonUtils;
import com.iyuile.caelum.util.SDFileOperate;
import com.iyuile.caelum.util.update.MyApplicationUpdateDownloadCB;
import com.iyuile.caelum.util.update.SkipIgnoreChecker;
import com.iyuile.caelum.util.update.UpdateNeedUpdateCreator;
import com.iyuile.caelum.util.update.UserCallsUpdateStrategy;
import com.iyuile.caelum.view.HeaderLayout;
import com.iyuile.caelum.view.toast.SuperToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.io.File;

/**
 * 设置
 *
 * @author WangYao
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initHeader();
        initView();
        setCacheSize();
    }

    private void initHeader() {
        LinearLayout mHeaderLinearLayout = (LinearLayout) getActivity().findViewById(R.id.common_actionbar_fragment_setting);
        View statusBar = mHeaderLinearLayout.findViewById(R.id.v_status_bar);
        setStatusBar(getActivity(), statusBar);
        HeaderLayout mHeaderLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(getString(R.string.main_toolbar_tv_setting));
        mHeaderLayout.setTitleTypeface(mApplication.getWoodBodyStyleFont());

        final View tvLabel = mHeaderLinearLayout.findViewById(R.id.tv_label);
        final TextView tvTitle = mHeaderLayout.getTitleCenter();
        ViewTreeObserver vto = tvTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvLabel.getLayoutParams();
                layoutParams.width = tvTitle.getWidth();
                tvLabel.setLayoutParams(layoutParams);
            }
        });
    }

    private LinearLayout llVersion, llCache, llSuggestion, llAboutUs, llPrivacyClause;
    private TextView tvVersionSecondary;
    private TextView tvCacheSecondary;
    private Button btnLogout;

    private void initView() {
        setAllIconTextViewsFont(getActivity());
        llVersion = (LinearLayout) getActivity().findViewById(R.id.ll_version);
        llCache = (LinearLayout) getActivity().findViewById(R.id.ll_cache);
        llSuggestion = (LinearLayout) getActivity().findViewById(R.id.ll_suggestion);
        llAboutUs = (LinearLayout) getActivity().findViewById(R.id.ll_about_us);
        llPrivacyClause = (LinearLayout) getActivity().findViewById(R.id.ll_privacy_clause);

        llVersion.setOnClickListener(this);
        llCache.setOnClickListener(this);
        llSuggestion.setOnClickListener(this);
        llAboutUs.setOnClickListener(this);
        llPrivacyClause.setOnClickListener(this);

        tvVersionSecondary = (TextView) getActivity().findViewById(R.id.wb_tv_version_secondary);
        tvCacheSecondary = (TextView) getActivity().findViewById(R.id.wb_tv_cache_secondary);

        btnLogout = (Button) getActivity().findViewById(R.id.wb_btn_logout);
        btnLogout.setOnClickListener(this);

        try {
            String versionName = AppInfo.getVersionName(getActivity());
            if (versionName != null && !versionName.equals(""))
                tvVersionSecondary.setText(getString(R.string.current_version_param, versionName));
        } catch (Exception e) {
        }
    }

    /**
     * 设置显示缓存大小
     */
    private void setCacheSize() {
        try {
            String cacheSize = "0.0M";
            try {
                //            File imageCacheFile = ImageLoader.getInstance().getDiskCache().getDirectory();
                File cacheFile = getActivity().getExternalCacheDir();
                File appCacheFile = new File(SDCardTools.get_ProjectPath());
                cacheSize = SDFileOperate.FormetFileSizeM(SDFileOperate.getFolderSize(cacheFile) + SDFileOperate.getFolderSize(appCacheFile));
            } catch (Exception e) {
            }
            // 设置缓存大小
            tvCacheSecondary.setText(cacheSize);
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionFragmentHelper.onActivityForResult(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionFragmentHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isSeriesDoubleClick())
            return;

        switch (v.getId()) {
            case R.id.ll_version:
                if (permissionFragmentHelper.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (MyApplicationUpdateDownloadCB.isDownload()) {
                        //正在下载
                        SuperToast.makeText(getActivity(), getString(R.string.update_loading_prompt),
                                SuperToast.Icon.Resource.INFO,
                                SuperToast.Background.BLUE).show();
                        return;
                    }
                    UpdateBuilder.create()
                            .updateChecker(new SkipIgnoreChecker())
                            .strategy(new UserCallsUpdateStrategy())
                            .updateDialogCreator(new UpdateNeedUpdateCreator(true))
                            .check(getActivity());
                } else {
                    permissionFragmentHelper
//                        .setForceAccepting(true)//强迫用户允许
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //fragment需要自己手动去判断不再提示
                    if (permissionFragmentHelper.isPermissionPermanentlyDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        onPermissionReallyDeclined(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case R.id.ll_cache:
                cleanCache();
                break;
            case R.id.ll_suggestion:
                startFeedback();
                break;
            case R.id.ll_about_us:
                Intent intent2 = new Intent(getActivity(), WebActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString(WebActivity.INTENT_PARAM_TITLE, getString(R.string.setting_tv_about_us));
                bundle2.putString(WebActivity.INTENT_PARAM_URL, AppConstants.ABOUTUS_WEB_URL);
                bundle2.putBoolean(WebActivity.INTENT_PARAM_PROGRESSBAR, false);
                intent2.putExtras(bundle2);
                startAnimActivity(intent2);
                break;
            case R.id.ll_privacy_clause:
                Intent intent3 = new Intent(getActivity(), WebActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString(WebActivity.INTENT_PARAM_TITLE, getString(R.string.setting_tv_privacy_clause));
                bundle3.putString(WebActivity.INTENT_PARAM_URL, AppConstants.PRIVACYPOLICY_WEB_URL);
                bundle3.putBoolean(WebActivity.INTENT_PARAM_PROGRESSBAR, false);
                intent3.putExtras(bundle3);
                startAnimActivity(intent3);
                break;
            case R.id.wb_btn_logout:
                logout();
                break;
        }
    }

    private void startFeedback() {
        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
        startAnimActivity(intent);
        /*String url;
        try {
            String versionName = AppInfo.getVersionName(getActivity());
            int versionCode = AppInfo.getVersionCode(getActivity());
            String os = Build.VERSION.RELEASE;
            os = "android " + os;
            String model = Build.MODEL;
            url = String.format(AppConstants.FEEDBACK_WEB_URL + AppConstants._PARAMS_FEEDBACK, versionName, versionCode, os, model);//程序版本, 构建版本, 系统, 型号
        } catch (Exception e) {
            url = AppConstants.FEEDBACK_WEB_URL;
        }
        Intent intent1 = new Intent(getActivity(), WebActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString(WebActivity.INTENT_PARAM_TITLE, getString(R.string.setting_tv_suggestion));
        bundle1.putString(WebActivity.INTENT_PARAM_URL, url);
        bundle1.putBoolean(WebActivity.INTENT_PARAM_PROGRESSBAR, false);
        intent1.putExtras(bundle1);
        startAnimActivity(intent1);*/
    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
        ImageLoader.getInstance().clearDiskCache();// 清除本地缓存
        ImageLoader.getInstance().clearMemoryCache();// 清除内存缓存

        SDCardTools.clearWebViewCache(getActivity());

        SDCardTools.cleanOkHttp(getActivity());
        SDCardTools.cleanImageAvatar();
        SDCardTools.cleanImageGodShefushencaiItem();

        SDCardTools.cleanUpdate();

        SuperToast.makeText(getActivity(), getString(R.string.setting_cache_success),
                SuperToast.Icon.Resource.YES,
                SuperToast.Background.GREEN).show();

        setCacheSize();
    }


    /**
     * 退出登录
     */
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.alertDialog_app_prompt_title))
                .setMessage(getString(R.string.alertDialog_app_prompt_content))
                .setPositiveButton(
                        getString(R.string.alertDialog_app_prompt_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton(getString(R.string.alertDialog_app_prompt_logout), new DialogInterface.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 账号登出时需调用此接口，调用之后不再发送账号相关内容
                //TODO :::um
                MobclickAgent.onProfileSignOff();
//                    mApplication.clearDaoUserMasterAndSession();// 清除用户本地数据库对象(skipLogin()里调用过了)
                new ResponseHandlerListener(getActivity()).skipLogin();

                try {
                    MyFragment.mInstance.handler.sendEmptyMessage(5);
                } catch (Exception e) {
                }
            }
        });

        AlertDialog mDialogLogout = builder.create();
        mDialogLogout.setCancelable(false);// 按对话框以外的地方不起作用。按返回键也不起作用
        mDialogLogout.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Main_Setting");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Main_Setting");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //再次回到这个页面的时候刷新cache大小
        if (!hidden)
            setCacheSize();
    }

}
