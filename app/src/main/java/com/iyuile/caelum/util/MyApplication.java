package com.iyuile.caelum.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuile.caelum.activity.ActivityManager;
import com.iyuile.caelum.api.ResponseHandlerListener;
import com.iyuile.caelum.api.impl.ApiServiceImpl;
import com.iyuile.caelum.entity.UserEntity;
import com.iyuile.caelum.entity.response.UpdateResponse;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.util.update.MyApplicationUpdateCheckCB;
import com.iyuile.caelum.util.update.MyApplicationUpdateDownloadCB;
import com.iyuile.caelum.util.update.MyApplicationUpdateStrategy;
import com.iyuile.caelum.util.update.UpdateNeedDownloadCreator;
import com.iyuile.caelum.util.update.UpdateNeedInstallCreator;
import com.iyuile.caelum.util.update.UpdateNeedUpdateCreator;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.business.UpdateWorker;
import org.lzh.framework.updatepluginlib.creator.ApkFileCreator;
import org.lzh.framework.updatepluginlib.creator.ExitExecutor;
import org.lzh.framework.updatepluginlib.creator.InstallChecker;
import org.lzh.framework.updatepluginlib.model.CheckEntity;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author WangYao
 * @version 1
 * @Description 自定义全局application
 * @ProjectName Apus
 * @ClassName {@link MyApplication}
 * @Date 2016-3-30 下午1:41:34
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    public ActivityManager mActivityManager;

    public UserEntity mUserObject;

    public String wxAppID;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        OkGo.init(this);

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
//                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
//                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
//                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//                .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
//                    .setCookieStore(new PersistentCookieStore())        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();                               //方法一：信任所有证书,不安全有风险
//                    .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//                    //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//                    .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//                    .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
//                    .addCommonHeaders(headers)  //设置全局公共头
//                    .addCommonParams(params);   //设置全局公共参数

        } catch (Exception e) {
            e.printStackTrace();
        }


        mInstance = this;
        mActivityManager = ActivityManager.getInstance();// Activity管理器

        SDCardTools.isSDCardEnable(getApplicationContext());

        initImageLoader(getApplicationContext());

        initUpdate();

        initWx();
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * 微信
     */
    private void initWx() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            wxAppID = appInfo.metaData.getString("wechat_appid");
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    /**
     * 初始化ImageLoader
     *
     * @param context 上下文
     */
    private static void initImageLoader(Context context) {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(context, SDCardTools._PROJECT_FOLDER + File.separator + "ImagesCache"); //debug   //ImagesCache
//		<application android:largeHeap="true"/>
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024)// 50 Mb
//                .imageDownloader(new ImageLoaderWithHeader(context))// :::图片验证码头信息
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCache(new UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                //TODO :::debug
//                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    private void initUpdate() {
        UpdateConfig.getConfig()
                // 必填：数据更新接口,.url与.checkEntity两种方式任选一种填写
                .url("update_url")
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        try {
                            UpdateResponse updateData = UpdateResponse.toJSONObjectFromData(response);
                            if (updateData.getData() != null)
                                return updateData.getData();
                        } catch (Exception e) {
                        }
                        return null;
                    }
                })
                //退出操作
                .exitExecutor(new ExitExecutor() {
                    @Override
                    public void exit() {
                        //如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
//                        MobclickAgent.onKillProcess(this);
                        mActivityManager.exit();
                    }
                })
                //配置检查更新时的回调
                .checkCB(new MyApplicationUpdateCheckCB(mInstance))
                // apk下载的回调
                .downloadCB(new MyApplicationUpdateDownloadCB(mInstance))
                /*// 自定义更新检查器。
                .updateChecker(new UpdateChecker() {
                    @Override
                    public boolean check(Update update) {
                        // 在此对配置jsonParser返回的update实体类做是否需要更新的检查。返回true为需要更新。返回false代表不需要更新
                        return new DefaultChecker().check(update);
                    }
                })*/
                // 自定义更新接口的访问任务
                .checkWorker(new UpdateWorker() {
                    @Override
                    protected String check(CheckEntity checkEntity) throws Exception {
                        // 此方法返回的数据会传递至配置类UpdateConfig中的jsonParser所指定的配置中。
                        try {
                            UpdateResponse updateResponse = ApiServiceImpl.findUpdateVersionSynchroImpl(mInstance);
                            return new Gson().toJson(updateResponse);
                        } catch (IOException e) {
                        }
                        return null;
                    }
                })
                /*// 自定义apk下载任务
                .downloadWorker(new DownloadWorker() {
                    @Override
                    protected void download(String url, File target) throws Exception {
                        try {
                            ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
                                @Override
                                protected void onProgress(long progress, long total, boolean done) {
                                    sendUpdateProgress(progress, total);
                                    //完成后调用清除方法
                                    if (done) {
                                        ProgressHelper.clearProgressHandler();
                                    }
                                }
                            });
                            ResponseBody responseBody = ApiServiceImpl.downloadUpdateAppSynchroImpl(mInstance, url);
                            SDFileOperate.writeFileSdcardFile(target.getAbsolutePath(), responseBody.bytes());
                        } catch (IOException e) {
                        }
                    }
                })*/
                //apk安装前进行预校验
                .installChecker(new InstallChecker() {
                    @Override
                    public boolean check(Update update, String apkUrl) {
                        String localFileMd5 = EncryptionUtils.getFileMD5(new File(apkUrl));
                        return localFileMd5 != null && localFileMd5.equalsIgnoreCase(update.getMd5()) ? true : false;
                    }
                })
                // 自定义下载文件缓存,默认下载至系统自带的缓存目录下
                .fileCreator(new ApkFileCreator() {
                    @Override
                    public File create(String versionName) {
                        // versionName 为解析的Update实例中的update_url数据。在些可自定义下载文件缓存路径及文件名。放置于File中
                        // 根据传入的versionName创建下载时使用的文件名
                        return SDFileOperate.createFile(SDCardTools.getUpdatePath(), "update_v_" + versionName);
                    }
                })
                // 自定义更新策略，默认WIFI下自动下载更新
                .strategy(new MyApplicationUpdateStrategy())
                // 自定义检查出更新后显示的Dialog，
                .updateDialogCreator(new UpdateNeedUpdateCreator())
                // 自定义下载时的进度条Dialog
                .downloadDialogCreator(new UpdateNeedDownloadCreator())
                // 自定义下载完成后。显示的Dialog
                .installDialogCreator(new UpdateNeedInstallCreator())
        ;
    }

    private Typeface typeFace, woodBodyFont, yeGenyouFont, redocnFont;

    /**
     * 图表字体
     *
     * @return
     */
    public Typeface getIconStyleFont() {
        if (typeFace == null) {
            // 将字体文件保存在assets/fonts/目录下，创建Typeface对象
            typeFace = Typeface.createFromAsset(mInstance.getAssets(), "fonts/iconfont.ttf");
        }
        return typeFace;
    }

    /**
     * 通用字体
     *
     * @return
     */
    public Typeface getWoodBodyStyleFont() {
        if (woodBodyFont == null) {
            // 将字体文件保存在assets/fonts/目录下，创建Typeface对象
            woodBodyFont = Typeface.createFromAsset(mInstance.getAssets(), "fonts/wood_body.ttf");
        }
        return woodBodyFont;
    }

    /**
     * 标题字体
     *
     * @return
     */
    public Typeface getYeGenyouStyleFont() {
        if (yeGenyouFont == null) {
            // 将字体文件保存在assets/fonts/目录下，创建Typeface对象
            yeGenyouFont = Typeface.createFromAsset(mInstance.getAssets(), "fonts/ye_genyou.ttf");
        }
        return yeGenyouFont;
    }


    private String token;

    /**
     * 令牌
     * 登录后才能使用,
     */
    public synchronized String mToken() {
        if (token == null || token.equals("null")) {
            return token = getSpUtil().getAccessTokenData();
        }
        return token;
    }

    private String deviceInfo;

    /**
     * 设备信息
     */
    public synchronized String mDeviceInfo() {
        try {
            if (deviceInfo == null || deviceInfo.equals("null")) {
                deviceInfo = getSpUtil().getCurrentDeviceInfo();
                if (deviceInfo.equals("null")) {
                    deviceInfo = AppInfo.getDeviceInfo(this);
                    if (deviceInfo == null) {
                        deviceInfo = TimeUtil.longToString(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + (int) ((Math.random() * 9 + 1) * 100000);
                    }
                }
            }
        } catch (Exception e) {
            deviceInfo = TimeUtil.longToString(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + (int) ((Math.random() * 9 + 1) * 100000);
        }
        return deviceInfo;
    }

    // 单例模式，才能及时返回数据
    private SharedPreferenceUtil mSpUtil;
    private final String PREFERENCE_NAME = "_sharedinfo";

    // info数据和首次使用等
    public synchronized SharedPreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String sharedName = SDCardTools._PROJECT_FOLDER + PREFERENCE_NAME + "_application";
            mSpUtil = new SharedPreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    /**
     * 退出登录的时候调用,切换用户本地数据库等
     */
    public void clearDaoUserMasterAndSession() {
        token = "null";
        getSpUtil().setAccessTokenData("null");
        getSpUtil().setCurrentUsersPhone("null");
    }

    public void findTokenAndUsersInfo(final Context context) {
        findTokenAndUsersInfo(context, true);
    }

    /**
     * 获取令牌和用户信息
     *
     * @param context
     * @param skipLogin
     */
    public void findTokenAndUsersInfo(final Context context, boolean skipLogin) {

        try {
            String userJSON = getSpUtil().getCurrentUserInfo();
            if (!userJSON.equals("null"))
                mUserObject = UserEntity.toJSONObjectFromData(userJSON);
        } catch (JsonSyntaxException e) {
            //解析错误
//            e.printStackTrace();
        }

        try {
            if (!getSpUtil().getAccessTokenData().equals("null")) {
                ApiServiceImpl.findMyUsersInfoImpl(context);
            } else {
                if (!skipLogin)
                    return;
                new ResponseHandlerListener(context).skipLogin();
                return;
            }

        } catch (Exception e) {
            new ResponseHandlerListener(context).skipLogin();
            e.printStackTrace();
        }
    }

}
