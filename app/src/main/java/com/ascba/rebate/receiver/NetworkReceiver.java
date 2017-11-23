package com.ascba.rebate.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.yanzhenjie.nohttp.tools.NetUtils;

import static android.view.Gravity.CENTER;


public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            FrameLayout decorView = (FrameLayout) ((Activity) context).getWindow().getDecorView();
            TextView tvNet = (TextView) decorView.findViewById(R.id.tv_net_watcher);
            if (!NetUtils.isNetworkAvailable()) {//没网络
                if (tvNet == null) {
                    TextView textView = new TextView(context);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ScreenDpiUtils.dp2px(context, 56));
                    lp.topMargin = (int) ScreenDpiUtils.dp2px(context, 81);
                    textView.setLayoutParams(lp);
                    textView.setText(context.getResources().getString(R.string.no_network));
                    textView.setId(R.id.tv_net_watcher);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundColor(Color.BLACK);
                    textView.setAlpha(0.40f);
                    textView.setGravity(CENTER);
                    decorView.addView(textView);
                }
            } else {
                if (tvNet != null) {
                    decorView.removeView(tvNet);
                }
            }
        }
    }
}
