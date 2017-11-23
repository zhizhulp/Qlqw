package com.ascba.rebate.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.manager.ToastManager;
import com.ascba.rebate.utils.WXUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Jero on 2017/10/30 0030.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyApplication.getInstance().getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType();//1 '登录 2 '分享
        int errCode = baseResp.errCode;
        WXUtils.getInstance().loginCode = errCode;
        if (errCode == BaseResp.ErrCode.ERR_OK && type == 1) {
            WXUtils.getInstance().setCode((SendAuth.Resp) baseResp);
        } else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            ToastManager.show(type == 1 ? "取消微信登录" : "取消微信分享");
        } else if (errCode == BaseResp.ErrCode.ERR_SENT_FAILED) {
            ToastManager.show(type == 1 ? "微信登录失败" : "微信分享失败");
        } else if (errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
            ToastManager.show("拒绝授权");
        } else if (errCode == BaseResp.ErrCode.ERR_UNSUPPORT) {
            Log.e("wx", "onResp: 不支持！");
        } else if (errCode == BaseResp.ErrCode.ERR_BAN) {
            Log.e("wx", "onResp: 签名不匹配！");
        }
        finish();
    }
}
