package com.ascba.rebate.activities.msg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.MsgListAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.MsgListEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jero on 2017/9/19 0019.
 */

public class MsgListActivity extends BaseDefaultNetActivity {

    private List<MsgListEntity> msgList;
    private MsgListAdapter msgListAdapter;
    private int mPage = 1;

    @Override
    protected int bindLayout() {
        return R.layout.activity_msg;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setTextTitle(getIntent().getStringExtra("title"));
        msgListAdapter = new MsgListAdapter();
        mRecyclerView.setAdapter(msgListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MsgListEntity msgListEntity = (MsgListEntity) adapter.getItem(position);
                WebViewBaseActivity.start(MsgListActivity.this, "消息详情", msgListEntity.getAddress());
                msgListEntity.setIs_read(1);
                msgListAdapter.notifyDataSetChanged();
            }
        });
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {//请求数据
                mPage = 1;
                requestMsgNetwork(0);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                requestMsgNetwork(1);
            }
        });
        View view = new View(this);
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, 75));
        msgListAdapter.addFooterView(view);
        requestMsgNetwork(0);
    }

    public void requestMsgNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.messageList, RequestMethod.POST, null);
        request.add("messageType", getIntent().getStringExtra("messageType"));
        request.add("class_id", getIntent().getIntExtra("class_id", 0));
        request.add("page", mPage);
        executeNetwork(what, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(result.getData().toString());
                    msgList = JSON.parseArray(jsonObject.getString("classList"), MsgListEntity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msgListAdapter.setNewData(msgList);
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(result.getData().toString());
                    msgList = JSON.parseArray(jsonObject.getString("classList"), MsgListEntity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (msgList.size() == 0) {
                    loadMoreOver();
                } else {
                    msgListAdapter.addData(msgList);
                }
                break;
        }
    }
}
