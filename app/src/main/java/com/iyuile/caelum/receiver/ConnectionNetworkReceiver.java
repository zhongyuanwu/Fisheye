package com.iyuile.caelum.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.iyuile.caelum.R;
import com.iyuile.caelum.util.MyApplication;
import com.iyuile.caelum.view.toast.SuperToast;


/**
 * @Description 网络变化广播
 */
public class ConnectionNetworkReceiver extends BroadcastReceiver {

    private IConnectionNetwork conn;
    public static boolean isCheckUpdate;

    public ConnectionNetworkReceiver(IConnectionNetwork conn) {
        super();
        this.conn = conn;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conManager != null) {
            {
                NetworkInfo[] infos = conManager.getAllNetworkInfo();
                boolean isConnection = false;
                boolean is2g = false;

                if (infos != null) {
                    for (int i = 0; i < infos.length; i++) {
                        NetworkInfo info = infos[i];
                        if (info != null) {
                            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                                if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
                                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                                    is2g = true;
                                }
                            }
                            if (info.isConnected()) {
                                isConnection = true;
                                break;
                            }
                        }
                    }
                }

                if (isConnection) {
                    conn.conn(intent);
                } else {
                    conn.dissConn(intent);
                }

                if (is2g) {
                    SuperToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getResources().getString(R.string.response_code_networkavailable_slow),
                            SuperToast.Icon.Resource.WARNING,
                            SuperToast.Background.YELLOW).show();
                    conn.connSlow(intent);
                }
            }
        }
    }

}
