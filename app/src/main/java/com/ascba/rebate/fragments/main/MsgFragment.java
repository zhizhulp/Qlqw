package com.ascba.rebate.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.msg.MsgListActivity;
import com.ascba.rebate.adapter.MsgAdapter;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.MsgEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 消息
 */
public class MsgFragment extends BaseDefaultNetFragment {

    private List<MsgEntity> msgList;
    private MsgAdapter msgAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {//请求数据
                requestMsgNetwork(0);
            }
        });
        msgAdapter = new MsgAdapter();
        mRecyclerView.setAdapter(msgAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        msgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                msgList.get(position).setIs_read(1);
                msgAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putInt("class_id", msgList.get(position).getClass_id());
                bundle.putString("messageType", msgList.get(position).getMessageType());
                bundle.putString("title", msgList.get(position).getTitle());
                startActivity(MsgListActivity.class, bundle);
            }
        });
        requestMsgNetwork(0);
    }

    public void requestMsgNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.messageClass, RequestMethod.GET, null);
        executeNetwork(what, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0)
            try {
                JSONObject jsonObject = new JSONObject(result.getData().toString());
                msgList = JSON.parseArray(jsonObject.getString("messageClass"), MsgEntity.class);
                msgAdapter.setNewData(msgList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}
