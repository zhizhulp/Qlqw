package com.ascba.rebate.activities.agent;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.bill.VoucherBillActivity;
import com.ascba.rebate.adapter.AgentAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.AgentItem;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jero on 2017/12/11 0011.
 */

public class AgentActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private AgentAdapter agentAdapter;
    private List<AgentItem> agentItems;

    //<editor-fold desc="Head头部view">
    private View headView;
    private ImageView imHead;
    private TextView tvName;
    private TextView tvClass;
    private TextView tvType;
    private TextView tvAgentNum, tvOnlineMctNum, tvMctNum, tvUserNum;
    private TextView tvTime;
    private TextView tvPay;
    //</editor-fold>

    @Override
    protected int bindLayout() {
        return R.layout.activity_agent;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                tvPay.callOnClick();
            }
        });
        getResources().getColor(R.color.grey_black_tv);
        agentItems = new ArrayList<>();
        agentAdapter = new AgentAdapter(agentItems);
        agentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AgentItem agentItem = (AgentItem) adapter.getItem(position);
                if (agentItem.getNextTitle() == null)
                    return;
                if (agentItem.getNextType() == 1) {// 现金
                    startActivity(new Intent(AgentActivity.this, BillActivity.class)
                            .putExtra("type", agentItem.getListType())
                            .putExtra("title", agentItem.getNextTitle())
                            .putExtra("mine_type", 8));
                } else if (agentItem.getNextType() == 2) {// 积分
                    startActivity(new Intent(AgentActivity.this, ScoreBillActivity.class)
                            .putExtra("type", agentItem.getListType())
                            .putExtra("title", agentItem.getNextTitle())
                            .putExtra("mine_type", 8));
                } else if (agentItem.getNextType() == 3) {// 福利券
                    startActivity(new Intent(AgentActivity.this, VoucherBillActivity.class)
                            .putExtra("type", agentItem.getListType())
                            .putExtra("title", agentItem.getNextTitle())
                            .putExtra("mine_type", 8));
                }
            }
        });
        mRecyclerView.setAdapter(agentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.agencyIndex, RequestMethod.GET, null);
        executeNetwork(0, "请稍后", request);
    }

    private void addHead() {
        headView = getLayoutInflater().inflate(R.layout.item_agent_head, null);
        imHead = headView.findViewById(R.id.im_head);
        tvName = headView.findViewById(R.id.tv_name);
        tvClass = headView.findViewById(R.id.tv_class);
        tvType = headView.findViewById(R.id.tv_type_name);
        GradientDrawable drawable = (GradientDrawable) tvType.getCompoundDrawables()[0];
        drawable.setColor(getResources().getColor(R.color.blue_btn));
        tvAgentNum = headView.findViewById(R.id.tv_agent_num);
        tvOnlineMctNum = headView.findViewById(R.id.tv_online_mct_num);
        tvMctNum = headView.findViewById(R.id.tv_mct_num);
        tvUserNum = headView.findViewById(R.id.tv_user_num);
        tvTime = headView.findViewById(R.id.tv_left_time);
        tvPay = headView.findViewById(R.id.tv_pay);
        tvPay.setOnClickListener(this);
        agentAdapter.addHeaderView(headView);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            setHead(dataObj);
            setList(dataObj);
        }
    }

    private void setHead(JSONObject jObj) {
        if (headView == null)
            addHead();
        AppConfig ins = AppConfig.getInstance();
        Picasso.with(this).load(ins.getString("avatar", null)).placeholder(R.mipmap.logo).into(imHead);
        tvName.setText(ins.getString("nickname", null));
        tvClass.setText(ins.getString("group_name", null));
        tvType.setText(jObj.getString("agent_title"));
        tvAgentNum.setText(jObj.getString("statis_agent"));
        tvOnlineMctNum.setText(jObj.getString("statis_online_business"));
        tvMctNum.setText(jObj.getString("statis_offline_business"));
        tvUserNum.setText(jObj.getString("statis_general_user"));
        tvTime.setText(jObj.getString("seller_count"));
        mMoneyBar.setTextTail(jObj.getIntValue("user_agent") == 0 ? "代理加盟" : null);
    }

    private void setList(JSONObject jObj) {
        agentItems.clear();
        agentItems.add(new AgentItem(0xff408fff, "现金收益"));
        agentItems.add(new AgentItem(0xff408fff, jObj.getString("business_income_title"),
                jObj.getString("business_income"), jObj.getString("business_income_off_title"), 1, 0));
        agentItems.add(new AgentItem(0xfff19234, jObj.getString("commission_title"),
                jObj.getString("commission"), jObj.getString("commission_off_title"), 1, 0));
        agentItems.add(new AgentItem(0xff44eee7, jObj.getString("red_commission_title"),
                jObj.getString("red_commission"), jObj.getString("red_commission_off_title"), 1, 0));
        agentItems.add(new AgentItem(0xffe15698, jObj.getString("dividend_commission_title"),
                jObj.getString("dividend_commission"), jObj.getString("dividend_commission_off_title"), 1, 0));
        agentItems.add(new AgentItem(0xffdfe156, jObj.getString("agency_title"),
                jObj.getString("agency"), jObj.getString("agency_off_title"), 1, 0));
        agentItems.add(new AgentItem(0xff834ffb, "积分收益"));
        agentItems.add(new AgentItem(0xff834ffb, jObj.getString("gift_points_title"),
                jObj.getString("gift_points"), jObj.getString("gift_points_off_title"), 2, 0));
        agentItems.add(new AgentItem(0xffffb540, "福利券收益"));
        agentItems.add(new AgentItem(0xffffb540, jObj.getString("benefit_coupon_title"),
                jObj.getString("benefit_coupon"), jObj.getString("benefit_coupon_off_title"), 3, 0));
        agentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_pay) {
            startActivity(AgentPayActivity.class, null);
        }
    }
}
