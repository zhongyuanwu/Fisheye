package com.iyuile.caelum;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.iyuile.alertdialog.AlertDialog;
import com.iyuile.caelum.activity.FunctionActivity;
import com.iyuile.caelum.activity.ShoppingCartActivity;
import com.iyuile.caelum.fragment.DailyFragment;
import com.iyuile.caelum.fragment.FindFragment;
import com.iyuile.caelum.fragment.HotFragment;
import com.iyuile.caelum.receiver.ConnectionNetworkReceiver;
import com.iyuile.caelum.receiver.IConnectionNetwork;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.util.Log;
import com.iyuile.caelum.util.update.EnterUpdateCheckCB;
import com.iyuile.caelum.view.BottomBar;
import com.iyuile.caelum.view.BottomBarTab;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by k21 on 2017/1/16.
 */

public class SupportMainActivity extends SupportActivity implements IConnectionNetwork, OnPermissionCallback {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    private ConnectionNetworkReceiver connReceiver;
    public static final int REQUEST_CODE_LOGIN = 0x00001011;//请求登录
    public final String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    protected PermissionHelper permissionHelper;
    @Bind(R.id.bottomBar)
    BottomBar mBottomBar;
    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;
    @Bind(R.id.main_shopping_iv_right)
    ImageButton mImageButton;

    private SupportFragment[] mFragments = new SupportFragment[3];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu);
        //获取控件
        ButterKnife.bind(this);
        permissionHelper = PermissionHelper.getInstance(this);
        if (savedInstanceState == null) {
            mFragments[FIRST] = DailyFragment.newInstance();
            mFragments[SECOND] = FindFragment.newInstance();
            mFragments[THIRD] = HotFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findFragment(DailyFragment.class);
            mFragments[SECOND] = findFragment(FindFragment.class);
            mFragments[THIRD] = findFragment(HotFragment.class);
        }
        setPermiss();
        initView();
        setListener();
        registerNetWorkReceiver();

    }

    private void setPermiss() {
        //首次进入返回true,true,如果用户选择拒绝下次进入返回true,false,如果用户选择不再提示返回true,true
        if (permissionHelper.isPermissionDeclined(MULTI_PERMISSIONS[0]) && permissionHelper.isPermissionPermanentlyDenied(MULTI_PERMISSIONS[0]))
            permissionHelper.request(MULTI_PERMISSIONS[0]);
        if (permissionHelper.isPermissionDeclined(MULTI_PERMISSIONS[1]) && permissionHelper.isPermissionPermanentlyDenied(MULTI_PERMISSIONS[1]))
            permissionHelper.request(MULTI_PERMISSIONS[1]);
        if (permissionHelper.isPermissionDeclined(MULTI_PERMISSIONS[2]) && permissionHelper.isPermissionPermanentlyDenied(MULTI_PERMISSIONS[2]))
            permissionHelper.request(MULTI_PERMISSIONS[2]);
    }

    private void initView() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设置不显示标题
        mainToolbar.setNavigationIcon(R.drawable.ic_action_menu);

        mBottomBar
                .addItem(new BottomBarTab(this, R.drawable.ic_message_white_24dp, "每日精选"))
                .addItem(new BottomBarTab(this, R.drawable.ic_home_white_24dp, "发现更多"))
                .addItem(new BottomBarTab(this, R.drawable.ic_discover_white_24dp, "热门排行"));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
//                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    //设置事件监听
    private void setListener() {

        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupportMainActivity.this, FunctionActivity.class);
                startActivity(intent);
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupportMainActivity.this,ShoppingCartActivity.class));
            }
        });

    }

    /**
     * 注册网路监听
     */
    private void registerNetWorkReceiver() {
        connReceiver = new ConnectionNetworkReceiver(this);
        IntentFilter connFilter = new IntentFilter();
        connFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connReceiver, connFilter);
    }

    /**
     * 注销网络监听
     */
    private void unRegisterNetWorkReceiver() {
        if (connReceiver != null) {
            unregisterReceiver(connReceiver);
            connReceiver = null;
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        for (String value : permissionName) {
            if (value.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                SDCardTools.isSDCardEnable(getApplicationContext());
                checkUpdate();
            }
        }
    }

    @Override
    public void conn(Intent intent) {
        checkUpdate();
    }

    /**
     * 检查版本更新
     */
    private void checkUpdate() {
        //检查版本更新
        if (!connReceiver.isCheckUpdate) {
            UpdateBuilder.create()
                    //配置检查更新时的回调
                    .checkCB(new EnterUpdateCheckCB(this, connReceiver, permissionHelper))
                    .check(this);
        }
    }

    @Override
    public void dissConn(Intent intent) {
    }

    @Override
    public void connSlow(Intent intent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * 允许
     *
     * @param permissionName
     */
//    @Override
//    public void onPermissionGranted(@NonNull String[] permissionName) {
//        Log.i(":::onPermissionGranted", "允许");
//    }

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
            builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert_dialog_permission_prompt_title));
        }
        builder.setPositiveButton(
                getString(R.string.alert_dialog_permission_prompt_request),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isReallyDeclined) {
                            permissionHelper.openSettingsScreen();
                        } else {
                            permissionHelper.requestAfterExplanation(permissionName);
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

    @Override
    protected void onDestroy() {
        unRegisterNetWorkReceiver();
//        try {
//            HttpUtils.getClient().cancelAllRequests(true);// 取消全部请求
//        } catch (Exception e) {}
//        mInstance = null;
        super.onDestroy();
    }
}
