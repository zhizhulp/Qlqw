package com.ascba.rebate.activities.agent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.bill.VoucherBillActivity;
import com.ascba.rebate.adapter.AgentAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.AgentItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jero on 2017/12/11 0011.
 */

public class CityAgentActivity extends BaseDefaultNetActivity {
    private AgentAdapter agentAdapter;
    private List<AgentItem> agentItems;

    //<editor-fold desc="Head头部view">
    private View headView;
    private ImageView imHead;
    private TextView tvName;
    private TextView tvClass;
    private TextView tvType;
    private TextView tvAddress;
    private TextView tvAgentNum, tvOnlineMctNum, tvMctNum, tvUserNum;
    //</editor-fold>

    @Override
    protected int bindLayout() {
        return R.layout.activity_city_agent;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
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
                    startActivity(new Intent(CityAgentActivity.this, BillActivity.class)
                            .putExtra("type", agentItem.getListType())
                            .putExtra("title", agentItem.getNextTitle())
                            .putExtra("mine_type", 8));
                } else if (agentItem.getNextType() == 2) {// 积分
                    startActivity(new Intent(CityAgentActivity.this, ScoreBillActivity.class)
                            .putExtra("type", agentItem.getListType())
                            .putExtra("title", agentItem.getNextTitle())
                            .putExtra("mine_type", 8));
                } else if (agentItem.getNextType() == 3) {// 福利券
                    startActivity(new Intent(CityAgentActivity.this, VoucherBillActivity.class)
                            .putExtra("type", agentItem.getListType())
                            .putExtra("title", agentItem.getNextTitle())
                            .putExtra("mine_type", 8));
                }
            }
        });
        mRecyclerView.setAdapter(agentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO
        setList(null);
        addHead();
    }

    private void addHead() {
        headView = getLayoutInflater().inflate(R.layout.item_agent_head, null);
        imHead = headView.findViewById(R.id.im_head);
        tvName = headView.findViewById(R.id.tv_name);
        tvClass = headView.findViewById(R.id.tv_class);
        tvType = headView.findViewById(R.id.tv_type_name);
        tvAgentNum = headView.findViewById(R.id.tv_agent_num);
        tvOnlineMctNum = headView.findViewById(R.id.tv_online_mct_num);
        tvMctNum = headView.findViewById(R.id.tv_mct_num);
        tvUserNum = headView.findViewById(R.id.tv_user_name);
        tvAddress = headView.findViewById(R.id.tv_select_address);
        tvAddress.setVisibility(View.VISIBLE);
        headView.findViewById(R.id.lat_agent_num).setVisibility(View.GONE);
        agentAdapter.addHeaderView(headView);
    }

    private void setHead(JSONObject jObj) {
        if (headView == null)
            addHead();
        Picasso.with(this).load(jObj.getString("seller_image")).placeholder(R.mipmap.logo).into(imHead);
        tvName.setText(jObj.getString("seller_name"));
        tvClass.setText(jObj.getString("seller_rule"));
        tvType.setText(jObj.getString("seller_rule"));
        tvAgentNum.setText(jObj.getString(""));
        tvOnlineMctNum.setText(jObj.getString(""));
        tvMctNum.setText(jObj.getString(""));
        tvUserNum.setText(jObj.getString(""));
        tvAddress.setText(jObj.getString(""));
    }

    private void setList(JSONObject object) {
        agentItems.add(new AgentItem(0xff408fff, "现金收益"));
        agentItems.add(new AgentItem(0xff408fff, "", "", "", 1, 0));
        agentItems.add(new AgentItem(0xff834ffb, "积分收益"));
        agentItems.add(new AgentItem(0xff834ffb, "", "", "", 2, 0));
        agentItems.add(new AgentItem(0xffffb540, "福利券收益"));
        agentItems.add(new AgentItem(0xffffb540, "", "", "", 3, 0));
    }
}
