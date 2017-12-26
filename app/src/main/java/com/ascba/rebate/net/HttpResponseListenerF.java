package com.ascba.rebate.net;

import android.app.ProgressDialog;

import com.ascba.rebate.R;
import com.ascba.rebate.base.fragment.BaseNetFragment;
import com.ascba.rebate.bean.Result;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.ServerError;
import com.yanzhenjie.nohttp.error.StorageReadWriteError;
import com.yanzhenjie.nohttp.error.StorageSpaceNotEnoughError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.ref.WeakReference;

/**
 * Created by 李平 on 2017/8/11.
 * 网络请求监听器
 */

public abstract class HttpResponseListenerF<T> implements OnResponseListener<Result<T>> {

    private ProgressDialog dialog;
    protected WeakReference<BaseNetFragment> context;

    public HttpResponseListenerF(BaseNetFragment context, String message) {
        this.context =new WeakReference<>(context);
        if(message!=null){
            this.dialog = new ProgressDialog(this.context.get().getActivity(), R.style.dialog);
            dialog.setCanceledOnTouchOutside(false);//点击外部不可取消
            dialog.setCancelable(true);//返还键可取消
            dialog.setMessage(message);
        }
    }


    @Override
    public void onStart(int what) {
        if(dialog!=null && !dialog.isShowing()) {
            dialog.show();
        }
        onHttpStart(what);
    }

    @Override
    public void onSucceed(int what, Response<Result<T>> response) {
        onHttpSucceed(what,response);
    }

    @Override
    public void onFailed(int what, Response<Result<T>> response) {
        BaseNetFragment context = this.context.get();
        Exception e = response.getException();
        if (e instanceof NetworkError) {
            //context.showToast(e.getMessage());
        }else if(e instanceof ServerError){
            context.showToast(e.getMessage());
        }else if(e instanceof StorageReadWriteError){
            context.showToast(e.getMessage());
        }else if(e instanceof StorageSpaceNotEnoughError){
            context.showToast(e.getMessage());
        }else if(e instanceof TimeoutError){
            context.showToast(e.getMessage());
        }else if(e instanceof UnKnownHostError){
            context.showToast(e.getMessage());
        }else if(e instanceof NotFoundCacheError){
            context.showToast(e.getMessage());
        }else if(e instanceof URLError){
            context.showToast(e.getMessage());
        }else {
            context.showToast(e.getMessage());
        }
        onHttpFailed(what,response);

    }

    @Override
    public void onFinish(int what) {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        onHttpFinish(what);
    }

    public abstract void onHttpSucceed(int what, Response<Result<T>> response);
    public abstract void onHttpFailed(int what, Response<Result<T>> response);
    public abstract void onHttpFinish(int what);
    public abstract void onHttpStart(int what);
}
