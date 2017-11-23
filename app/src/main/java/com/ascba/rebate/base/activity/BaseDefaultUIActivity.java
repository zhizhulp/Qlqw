package com.ascba.rebate.base.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ascba.rebate.R;
import com.ascba.rebate.receiver.NetworkReceiver;
import com.ascba.rebate.view.EasyStatusView;
import com.ascba.rebate.view.MoneyBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import static android.util.TypedValue.COMPLEX_UNIT_SP;


public class BaseDefaultUIActivity extends BaseUIActivity {
    /**
     * 状态控制view
     */
    protected EasyStatusView mStatusView;
    protected RecyclerView mRecyclerView;
    private NetworkReceiver mNetReceiver;
    public MoneyBar mMoneyBar;
    /**
     * 刷新控件
     */
    protected SmartRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int bindLayout() {
        return R.layout.default_override;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initStatusView();
        initRefreshLayout();
        initTvNetWatcher();
        initMoneyBar();
    }

    private void initMoneyBar() {
        mMoneyBar = fv(R.id.mb);
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_NORMAL;
    }

    private void initTvNetWatcher() {
        setNetworkReceiver();
    }

    private void setNetworkReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetReceiver = new NetworkReceiver();
        registerReceiver(mNetReceiver, filter);
    }

    private void initRefreshLayout() {
        mRefreshLayout = fv(R.id.refresh_layout);
        mRecyclerView = fv(R.id.recyclerView);
        if (mRefreshLayout != null) {
            mRefreshLayout.setHeaderHeight(48);
            mRefreshLayout.setFooterHeight(48);
            mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
            //mRefreshLayout.setReboundDuration(0);//回弹动画时长
            ClassicsFooter cf = new ClassicsFooter(this);
            cf.setTextSizeTitle(COMPLEX_UNIT_SP,14);
            cf.setDrawableArrowSize(17);
            cf.setDrawableProgressSize(17);
            mRefreshLayout.setRefreshFooter(cf);
            ClassicsHeader ch = new ClassicsHeader(this);
            ch.setTextSizeTitle(COMPLEX_UNIT_SP,14);
            ch.setDrawableArrowSize(17);
            ch.setDrawableProgressSize(17);
            ch.setEnableLastTime(false);
            //ch.setFinishDuration(0);//刷新结束停留时间
            mRefreshLayout.setRefreshHeader(ch);
        }
    }

    private void initStatusView() {
        mStatusView = fv(R.id.statusView);
    }

    protected void stopRefreshAndLoadMore() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setLoadmoreFinished(false);
        }
        if (mRefreshLayout != null &&  mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore();
        }
    }

    /**
     * 没有更多了
     */
    protected void loadMoreOver() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
            mRefreshLayout.setLoadmoreFinished(true);
        }
    }

    protected void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetReceiver != null) {
            unregisterReceiver(mNetReceiver);
        }
    }
}
