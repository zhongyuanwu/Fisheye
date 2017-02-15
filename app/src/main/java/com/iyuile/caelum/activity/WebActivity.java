package com.iyuile.caelum.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.tools.SDCardTools;
import com.iyuile.caelum.util.CommonUtils;
import com.iyuile.caelum.util.OkHttpUtils;
import com.iyuile.caelum.util.PixelUtil;
import com.umeng.analytics.MobclickAgent;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * web
 */
public class WebActivity extends BaseBackSwipeActivity {

    public static WebActivity mInstance;

    public static final String INTENT_PARAM_URL = "intent_param_url";
    public static final String INTENT_PARAM_TITLE = "intent_param_title";
    public static final String INTENT_PARAM_HEADDERS = "intent_param_headders";
    public static final String INTENT_PARAM_PROGRESSBAR = "intent_param_progressbar";

    private WebView web;
    private ProgressBar progressBar;

    private String url, title;
    private boolean isHeader, isProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        try {
            url = bundle.getString(INTENT_PARAM_URL);
            if (url == null) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
            return;
        }
        isHeader = bundle.getBoolean(INTENT_PARAM_HEADDERS, false);
        isProgressBar = bundle.getBoolean(INTENT_PARAM_PROGRESSBAR, true);

        setContentView(R.layout.activity_web);
        getTintManager().setStatusBarDarkMode(true, this);// 文字颜色,true:黑色,false:白色
        getTintManager().setStatusBarTintColor(ContextCompat.getColor(this, android.R.color.transparent));
        mInstance = this;
        mApplication.mActivityManager.addActivity(this);
        try {
            title = bundle.getString(INTENT_PARAM_TITLE);
            if (title == null) {
//                initTopBarForLeftIcon(getString(R.string.title_web_loading), 0);
                initTopBarForLeftSecondaryIcon(getString(R.string.title_web_loading), 0, 0, mApplication.getWoodBodyStyleFont());
                setStatusBar(this);
                initActionbar();
            } else {
//                initTopBarForLeftIcon(title, 0);
                initTopBarForLeftSecondaryIcon(title, 0, 0, mApplication.getWoodBodyStyleFont());
                setStatusBar(this);
                initActionbar();
            }
        } catch (Exception e) {
//            initTopBarForLeftIcon(getString(R.string.title_web_loading), 0);
            initTopBarForLeftSecondaryIcon(getString(R.string.title_web_loading), 0, 0, mApplication.getWoodBodyStyleFont());
            setStatusBar(this);
            initActionbar();
        }

        web = (WebView) findViewById(R.id.web_01);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_01);
        webConfigure();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initActionbar() {
        mHeaderLayout.setTitleTypeface(mApplication.getWoodBodyStyleFont());
        final View tvLabel = findViewById(R.id.tv_label);
        final TextView tvTitle = mHeaderLayout.getTitleCenter();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
        layoutParams.setMargins(PixelUtil.dp2px(30f), layoutParams.topMargin, PixelUtil.dp2px(30f), layoutParams.bottomMargin);
        tvTitle.setLayoutParams(layoutParams);

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

    /**
     * 缓存模式(5种)
     * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
     * LOAD_DEFAULT:根据cache-control决定是否从网络上取数据。
     * LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式
     * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
     * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
     * 如：www.taobao.com的cache-control为no-cache，在模式LOAD_DEFAULT下，
     * 无论如何都会从网络上取数据，如果没有网络，就会出现错误页面；在LOAD_CACHE_ELSE_NETWORK模式下，
     * 无论是否有网络，只要本地有缓存，都使用缓存。本地没有缓存时才从网络上获取。
     * www.360.com.cn的cache-control为max-age=60，在两种模式下都使用本地缓存数据。
     * <p/>
     * <p/>
     * 总结：根据以上两种模式，建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT，无网络时，使用LOAD_CACHE_ELSE_NETWORK
     * 。
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void webConfigure() {

        //------------------------------设置缓存开启------------------------------------

        web.getSettings().setRenderPriority(RenderPriority.HIGH);
        if (CommonUtils.isNetworkAvailable()) {//有网络链接
            web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式
        } else {
            web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        }
        // 开启 DOM storage API 功能
        web.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        web.getSettings().setDatabaseEnabled(true);
        // 开启 Application Caches 功能
        web.getSettings().setAppCacheEnabled(true);
        //设置缓冲大小，200M
        web.getSettings().setAppCacheMaxSize(1024 * 1024 * 200);
        String cacheDirPath = SDCardTools.getDBPath(this);
        // 设置数据库缓存路径
        web.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        web.getSettings().setAppCachePath(cacheDirPath);

        //------------------------------------------------------------------

        web.setHorizontalScrollBarEnabled(false);
        web.setVerticalScrollBarEnabled(false);

        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 16)
                web.getSettings().setMediaPlaybackRequiresUserGesture(false);
        } catch (Exception e) {
        }

        // 若无设置WebViewClient,以loadUrl加载网页会打开内置浏览器
//        web.setWebViewClient(new WebViewClient());
        // 设置一个默认的基类
//        web.setWebChromeClient(new WebChromeClient());
        // 允许JavaScript执行
        web.getSettings().setJavaScriptEnabled(true);
        // 设置允许访问文件数据
        web.getSettings().setAllowFileAccess(true);
        // WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
        web.getSettings().setUseWideViewPort(false);

        web.loadUrl(url);

        web.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.e(":::webview:onProgressChanged", ":::"+newProgress);
//                progressBar.setProgress(newProgress);
                if (isProgressBar)
                    progressBar.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofInt(progressBar, "progress", newProgress);
                anim.setDuration(500);
                anim.start();
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (progressBar.getProgress() == 100) {
                            AlphaAnimation animationClose = new AlphaAnimation(1f, 0f);
                            animationClose.setDuration(500);
                            progressBar.startAnimation(animationClose);
                            animationClose.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBar.setProgress(0);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                        }
                    }
                });
            }

            /**
             * 获取title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (WebActivity.this.title == null) {
                    mHeaderLayout.setDefaultTitle(title);
                    initActionbar();
                }
            }

        });

        web.setWebViewClient(new WebViewClient() {
            // 点击超链接触发
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.e(":::webview:shouldOverrideUrlLoading", "url = " + url);
                // 方法一->//在当前的webview中跳转到新的url
                view.loadUrl(url);
                return true;

                // 方法二->启动手机浏览器来打开新的url
                // Intent i = new Intent(Intent.ACTION_VIEW);
                // i.setData(Uri.parse(url));
                // startActivity(i);
                // return true;
            }

            // 访问失败
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
                // Log.i("onReceivedError", errorCode + ":" + description + ":"+ failingUrl);
                // if (errorCode == -2)// description=找不到该网址。

                view.loadUrl("file:///android_asset/network_warning.html");

            }

            // 访问开始
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                Log.e(":::webview:onPageStarted", "url = " + url);
                super.onPageStarted(view, url, favicon);
            }

            // 访问完成
            @Override
            public void onPageFinished(WebView view, String url) {
//                Log.e(":::webview:onPageFinished", "url = " + url);
                super.onPageFinished(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (!isHeader)
                    return super.shouldInterceptRequest(view, request);
                else
                    return handleRequestViaOkHttp(url);
            }

            @NonNull
            private WebResourceResponse handleRequestViaOkHttp(@NonNull String url) {
                try {
                    OkHttpClient client = OkHttpUtils.getInstance(WebActivity.this);

                    final Call call = client.newCall(new Request.Builder()
                            .url(url)
                            .build()
                    );

                    final Response response = call.execute();

                    return new WebResourceResponse("text/html", "UTF-8", response.body().byteStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    /**
     * 返回处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            if (!CommonUtils.isNetworkAvailable())
                return super.onKeyDown(keyCode, event);
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        try {//网页关闭时,跳转空页面,关闭媒体(音乐)
            web.loadUrl("about:blank");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mInstance = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(WebActivity.class.getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(WebActivity.class.getSimpleName());
        MobclickAgent.onResume(this);
    }
}
