package com.ascba.rebate.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.manager.ToastManager;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.PayUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        api = WXAPIFactory.createWXAPI(this, CodeUtils.WX_PAY_APP_ID);
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

    @Override
    public void onResp(BaseResp resp) {
        int errCode = resp.errCode;
        if (errCode == 0) {//成功
            Intent intent = new Intent();
            intent.putExtra("type", "微信支付");
            intent.putExtra("money", PayUtils.getInstance().money);
            PayUtils.getInstance().goSuccess(intent);
        } else if (errCode == -1) {//错误
            ToastManager.show(getString(R.string.failed_pay));
        } else if (errCode == -2) {//用户取消
            ToastManager.show(getString(R.string.cancel_pay));
        }
        finish();
    }

}