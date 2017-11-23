package com.ascba.rebate.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by Jero on 2017/9/28 0028.
 */

public class EasyBRVAHEmpty extends EasyStatusView {

    private BaseQuickAdapter baseQuickAdapter;

    public EasyBRVAHEmpty(Context context) {
        super(context);
        super.onFinishInflate();
    }

    public void bind(BaseQuickAdapter adapter, int emptyLayoutId) {
        setEmptyLayoutId(emptyLayoutId);
        bind(adapter);
    }

    public void bind(BaseQuickAdapter adapter, View emptyView) {
        setEmptyView(emptyView);
        bind(adapter);
    }

    public void bind(BaseQuickAdapter adapter) {
        baseQuickAdapter = adapter;
        initView();
        setLoadingView(new View(getContext()));
        loading();
    }

    private void initView() {
        baseQuickAdapter.setEmptyView(this);
        baseQuickAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (baseQuickAdapter.getData().size() > 0) {
                    content();
                } else {
                    empty();
                }
            }
        });
    }
}
