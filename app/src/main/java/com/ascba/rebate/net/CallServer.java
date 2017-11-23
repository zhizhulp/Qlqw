package com.ascba.rebate.net;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.RestRequest;

/**
 * Created by 李平 on 2017/8/11.
 * 网络队列(单例)
 */

public class CallServer {

    private static CallServer instance;

    public static CallServer getInstance() {
        if (instance == null)
            synchronized (CallServer.class) {
                if (instance == null)
                    instance = new CallServer();
            }
        return instance;
    }

    private RequestQueue requestQueue;
    private DownloadQueue downloadQueue;

    private CallServer() {
        requestQueue = NoHttp.newRequestQueue();
        downloadQueue = NoHttp.newDownloadQueue();
    }

    public <T> void addRequest(int what, RestRequest<T> request, OnResponseListener<T> listener) {
        requestQueue.add(what, request, listener);
    }

    public void download(int what, DownloadRequest request, DownloadListener listener) {
        downloadQueue.add(what, request, listener);
    }

    public void addStringRequest(int what, RestRequest<String> request, OnResponseListener<String> listener){
        requestQueue.add(what, request, listener);
    }

    // 完全退出app时，调用这个方法释放CPU。
    public void stop() {
        if (requestQueue != null) {
            requestQueue.stop();
        }
        if (downloadQueue != null) {
            downloadQueue.stop();
        }
    }

    public void cancelDownloadBySign(int sign) {
        if (downloadQueue != null) {
            downloadQueue.cancelBySign(sign);
        }
    }
}