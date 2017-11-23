package com.ascba.rebate.base.activity;

import android.os.Bundle;

import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.net.CallServer;
import com.ascba.rebate.net.EntityRequest;
import com.ascba.rebate.net.HttpResponseListener;
import com.ascba.rebate.net.StringRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.PackageUtils;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * 网络基类
 */
public abstract class BaseNetActivity extends BaseDefaultUIActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected <T> void executeNetwork(int what, String message, AbstractRequest<T> request) {
        CallServer.getInstance().addRequest(what, request, new ImpHttpResponseListener<T>(this, message));
    }

    protected <T> void executeNetwork(int what, AbstractRequest<T> request) {
        executeNetwork(what, null, request);
    }

    protected <T> AbstractRequest buildRequest(String url, RequestMethod method, Class<T> clazz) {
        AbstractRequest request;
        if (clazz == null) {
            request = new StringRequest(url, method);
        } else {
            request = new EntityRequest<>(url, method, clazz);
        }
        if (hasCache()) {
            request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        }
        request.addHeader("apiversion", PackageUtils.getPackageVersion(this));
        request.addHeader("clientfrom", "android" + PackageUtils.getAndroidVersion());
        request.addHeader("deviceuuid", PackageUtils.getDeviceId());
        String nonceStr = EncodeUtils.makeNonceStr();
        request.addHeader("noncestr", nonceStr);
        request.addHeader("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        request.addHeader("sign", EncodeUtils.makeSign(nonceStr, url));
        request.addHeader("sessionId", AppConfig.getInstance().getString("session_id", null));
        request.addHeader("accessToken", AppConfig.getInstance().getString("access_token", null));
        return request;
    }

    /**
     * 设置是否有缓存
     *
     * @return true有缓存 false无缓存
     */
    protected boolean hasCache() {
        return false;
    }

    private class ImpHttpResponseListener<T> extends HttpResponseListener<T> {

        ImpHttpResponseListener(BaseUIActivity context, String message) {
            super(context, message);
        }

        @Override
        public void onHttpStart(int what) {

        }

        @Override
        public void onHttpSucceed(int what, Response<Result<T>> response) {
            Result<T> tResult = response.get();
            int code = tResult.getCode();
            switch (code) {
                case -2017://请求包体为空
                    showToast("code:" + code + "\n" + "msg :" + tResult.getMsg());
                    break;
                case -4515://服务端给的数据不是json数据
                    showToast("code:" + code + "\n" + "msg :" + tResult.getMsg());
                    break;
                case -7102://服务端有问题
                    mHandleFailed(what);
                    break;
                case -1545:
                    showToast(tResult.getMsg());
                    break;
                case 10:
                    showToast(tResult.getMsg());
                    break;
                case 11:
                    showToast(tResult.getMsg());
                    break;
                case 20://掉线
                    mHandleReLogin(what, tResult);
                    break;
                case 200:
                    mHandle200(what, tResult);
                    break;
                case 404:
                    mHandle404(what, tResult);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onHttpFailed(int what, Response<Result<T>> response) {
            Exception e = response.getException();
            if (e instanceof NetworkError) {
                mHandleNoNetwork(what);
            }
        }

        @Override
        public void onHttpFinish(int what) {
            stopRefreshAndLoadMore();
        }
    }

    protected abstract <T> void mHandle200(int what, Result<T> result);

    protected abstract void mHandle404(int what, Result result);

    protected abstract void mHandleReLogin(int what, Result result);

    protected abstract void mHandleFailed(int what);

    protected abstract void mHandleNoNetwork(int what);

}
