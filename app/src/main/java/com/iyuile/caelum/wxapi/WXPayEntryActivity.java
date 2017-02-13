package com.iyuile.caelum.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.iyuile.caelum.R;
import com.iyuile.caelum.activity.OrderBuyActivity;
import com.iyuile.caelum.utils.Log;
import com.iyuile.caelum.utils.MyApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付-完成后调用
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = ":::MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().wxAppID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * 回调中errCode值列表：
     * <p/>
     * 名称	描述	解决方案
     * 0	成功	展示成功页面
     * -1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
     * -2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode + "\nresp.errStr:" + resp.errStr);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int resultCode = resp.errCode;
            //1
            try {
                if (OrderBuyActivity.mInstance.onPayResultListener != null)
                    OrderBuyActivity.mInstance.onPayResultListener.onFinish(resultCode);
            } catch (Exception e) {
            }
            finish();
        }
    }


}