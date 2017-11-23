package com.ascba.rebate.listener;

import android.support.annotation.NonNull;

import java.io.File;

import top.zibin.luban.OnCompressListener;

/**
 * Created by 李平 on 2017/8/21.
 * 图片压缩监听（Luban）
 */

public class CompressListener implements OnCompressListener {
    private Callback callback;

    public CompressListener(@NonNull Callback callback) {
        this.callback =callback;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(File file) {
        callback.getCompressFile(file);
    }

    @Override
    public void onError(Throwable e) {

    }

    public interface Callback {
        void getCompressFile(File file);
    }
}
