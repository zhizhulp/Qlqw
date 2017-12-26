package com.ascba.rebate.base.fragment;

import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.ToastManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.net.CallServer;
import com.ascba.rebate.net.EntityRequest;
import com.ascba.rebate.net.HttpResponseListener;
import com.ascba.rebate.net.HttpResponseListenerF;
import com.ascba.rebate.net.StringRequest;
import com.ascba.rebate.utils.EncodeUtils;
import com.ascba.rebate.utils.PackageUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.ref.WeakReference;

/**
 * fragment 网络基类
 */

public abstract class BaseNetFragment extends BaseDefaultUIFragment {
    private RequestQueue mQueue;
    private Object object = new Object();

    protected <T> void executeNetwork(int what, String message, AbstractRequest<T> request) {
        if (mQueue == null) {
            mQueue = NoHttp.newRequestQueue();
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
        if (hasCache()) {
            request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        }
        request.setCancelSign(object);
        request.addHeader("apiversion", PackageUtils.getPackageVersion(getActivity()));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mQueue != null) {
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

    private static class ImpHttpResponseListener<T> extends HttpResponseListenerF<T> {
        ImpHttpResponseListener(BaseNetFragment context, String message) {
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
                    ToastManager.show("code:" + code + "\n" + "msg :" + tResult.getMsg());
                    break;
                case -4515://服务端给的数据不是json数据
                    ToastManager.show("code:" + code + "\n" + "msg :" + tResult.getMsg());
                    break;
                case -7102://服务端有问题
                    context.get().mHandleFailed(what);
                    break;
                case -1545:
                    ToastManager.show(tResult.getMsg());
                    break;
                case 10:
                    ToastManager.show(tResult.getMsg());
                    break;
                case 11:
                    ToastManager.show(tResult.getMsg());
                    break;
                case 20://掉线
                    context.get().mHandleReLogin(what, tResult);
                    break;
                case 200:
                    context.get().mHandle200(what, tResult);
                    break;
                case 404:
                    context.get().mHandle404(what, tResult);
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
