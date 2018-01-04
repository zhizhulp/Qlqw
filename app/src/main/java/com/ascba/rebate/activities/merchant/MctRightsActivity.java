package com.ascba.rebate.activities.merchant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AgentAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.AgentItem;
import com.ascba.rebate.bean.MctRights;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/12/4 18:44
 * Describe: 商家权益
 */

public class MctRightsActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private AgentAdapter agentAdapter;
    private List<AgentItem> agentItems;

    //<editor-fold desc="Head头部view">
    private View headView;
    private RoundImageView banner;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvStatus;
    private TextView tvLeftTime;
    private TextView tvLeftStr;
    private RelativeLayout latStatus;
    private TextView tvPay;
    private TextView tvBtm;
    //</editor-fold>

    private MctRights data;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_rights;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        agentItems = new ArrayList<>();
        agentAdapter = new AgentAdapter(agentItems);
        agentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AgentItem agentItem = (AgentItem) adapter.getItem(position);
                agentItem.goBill(MctRightsActivity.this);
            }
        });
        mRecyclerView.setAdapter(agentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvBtm = fv(R.id.tv_btm);

        fv(R.id.tv_see).setOnClickListener(this);
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.interests, RequestMethod.GET, MctRights.class);
        executeNetwork(0, "请稍后", request);
    }

    private void addHead() {
        headView = getLayoutInflater().inflate(R.layout.item_mct_head, null);
        banner = headView.findViewById(R.id.banner);
        LinearLayout.LayoutParams bannerLayoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        bannerLayoutParams.height = (getResources().getDisplayMetrics().widthPixels
                - (int) ScreenDpiUtils.dp2px(this, 28)) * 35 / 83;

        tvTitle = headView.findViewById(R.id.tv_title);
        tvTime = headView.findViewById(R.id.tv_time);
        tvStatus = headView.findViewById(R.id.tv_status);
        tvStatus.setOnClickListener(this);
        latStatus = headView.findViewById(R.id.lat_status);

        tvLeftStr = headView.findViewById(R.id.tv_left_time_str);
        tvLeftTime = headView.findViewById(R.id.tv_left_time);
        tvPay = headView.findViewById(R.id.tv_pay);
        tvPay.setOnClickListener(this);
        agentAdapter.addHeaderView(headView);
    }

    private void setHead() {
        if (headView == null)
            addHead();
        Picasso.with(this).load(Uri.parse(data.getActive_img()))
                .placeholder(R.mipmap.gift_head_loading)
                .into(banner);
        tvTitle.setText(data.getActive_title());
        if (data.getActive_desc() == null || data.getActive_desc().isEmpty())
            tvStatus.setVisibility(View.GONE);
        else
            tvStatus.setText(data.getActive_desc());
        tvTime.setText(data.getActive_time());
        tvLeftStr.setText(data.getSeller_status_tip());
        tvLeftTime.setText(data.getSeller_last_time());
        if (data.getSeller_status_text() == null || data.getSeller_status_text().isEmpty())
            tvPay.setVisibility(View.GONE);
        else
            tvPay.setText(data.getSeller_status_text());
        GradientDrawable drawable = (GradientDrawable) latStatus.getBackground();
        drawable.setColor(Color.parseColor("#" + data.getActive_color()));
    }

    private void setList() {
        agentItems.clear();
        agentItems.add(new AgentItem(0xff44cee7, "现金收益"));
        agentItems.add(new AgentItem(0xff44cee7, "入驻商家收益",
                data.getSeller_purchase_money(), "入驻商家收益", 1, "13"));
        agentItems.add(new AgentItem(0xff44cee7, "推荐代理收益",
                data.getSeller_agent_money(), "推荐代理收益", 1, "14"));
        agentItems.add(new AgentItem(0xffff4040, "礼品分收益"));
        agentItems.add(new AgentItem(0xffff4040, "礼品分收益",
                data.getSeller_getpay_money(), "礼品分收益", 2, "2"));
        agentItems.add(new AgentItem(0xffff4040, "礼品分收益流水",
                data.getSeller_referee_money(), "礼品分收益流水", 2, "5"));
        agentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_see://查看商家服务
                if (!TextUtils.isEmpty(data.getSeller_url())) {
                    WebViewBaseActivity.start(this, "商家权益", data.getSeller_url());
                } else {
                    Log.e(TAG, "url is null");
                }
                break;
            case R.id.tv_pay://续费
                startActivityForResult(MctPayActivity.class, null, CodeUtils.REQUEST_MCT_PAY);
                break;
            case R.id.tv_status:// 活动
                if (data.getActive_status() == 1)
                    WebViewBaseActivity.start(this, data.getActive_h5_title(), data.getActive_url());
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            data = (MctRights) result.getData();
            setHead();
            setList();
            tvBtm.setText(data.getSeller_text());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_MCT_PAY && resultCode == RESULT_OK) {
            requestData();
        }
    }
}
