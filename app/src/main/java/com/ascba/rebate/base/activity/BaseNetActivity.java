package com.ascba.rebate.base.activity;

import android.os.Bundle;
import android.util.Log;

import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.net.CallServer;
import com.ascba.rebate.net.EntityRequest;
import com.ascba.rebate.net.HttpResponseListener;
import com.ascba.rebate.net.StringRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.PackageUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * 网络基类
 */
public abstract class BaseNetActivity extends BaseDefaultUIActivity {
    private RequestQueue mQueue;
    private Object object=new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected <T> void executeNetwork(int what, String message, AbstractRequest<T> request) {
        if(mQueue==null){
            mQueue= NoHttp.newRequestQueue();
        }
        mQueue.add(what, request, new ImpHttpResponseListener<T>(this, message));
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
        request.setCancelSign(object);
        if (hasCache()) {
            request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        }
        request.addHeader("apiversion", PackageUtils.getPackageVersion(this));
        request.addHeader("clientfrom", "android" + PackageUtils.getAndroidVersion());
        request.addHeader("deviceuuid", PackageUtils.getDeviceId());
        String nonceStr = EncodeUtils.makeNonceStr();
        request.addHeader("noncestr", nonceStr);
        String[] strs = EncodeUtils.makeSignHead(nonceStr, url);
        request.addHeader("sign", strs[1]);
        request.addHeader("timestamp", strs[0]);
        request.addHeader("sessionId", AppConfig.getInstance().getString("session_id", null));
        request.addHeader("accessToken", AppConfig.getInstance().getString("access_token", null));
        return request;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mQueue!=null){
            mQueue.cancelBySign(object);
            mQueue.stop();
        }
    }

    /**
     * 设置是否有缓存
     *
     * @return true有缓存 false无缓存
     */
    protected boolean hasCache() {
        return false;
    }

    private static class ImpHttpResponseListener<T> extends HttpResponseListener<T> {

        ImpHttpResponseListener(BaseNetActivity context, String message) {
            super(context, message);
        }

        @Override
        public void onHttpStart(int what) {

        }

        @Override
        public void onHttpSucceed(int what, Response<Result<T>> response) {
            Result<T> tResult = response.get();
            BaseNetActivity context = this.context.get();
            int code = tResult.getCode();
            switch (code) {
                case -2017://请求包体为空
                    context.showToast("code:" + code + "\n" + "msg :" + tResult.getMsg());
                    break;
                case -4515://服务端给的数据不是json数据
                    context.showToast("code:" + code + "\n" + "msg :" + tResult.getMsg());
                    break;
                case -7102://服务端有问题
                    context.mHandleFailed(what);
                    break;
                case -1545:
                    context.showToast(tResult.getMsg());
                    break;
                case 10:
                    context.showToast(tResult.getMsg());
                    break;
                case 11:
                    context.showToast(tResult.getMsg());
                    break;
                case 20://掉线
                    context.mHandleReLogin(what, tResult);
                    break;
                case 200:
                    context.mHandle200(what, tResult);
                    break;
                case 404:
                    context.mHandle404(what, tResult);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onHttpFailed(int what, Response<Result<T>> response) {
            Exception e = response.getException();
            if (e instanceof NetworkError) {
                context.get().mHandleNoNetwork(what);
            }
        }

        @Override
        public void onHttpFinish(int what) {
            context.get().stopRefreshAndLoadMore();
        }
    }

    protected abstract <T> void mHandle200(int what, Result<T> result);

    protected abstract void mHandle404(int what, Result result);

    protected abstract void mHandleReLogin(int what, Result result);

    protected abstract void mHandleFailed(int what);

    protected abstract void mHandleNoNetwork(int what);

}
