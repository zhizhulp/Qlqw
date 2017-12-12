package com.ascba.rebate.activities.agent;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.success.TextInfoSuccessActivity;
import com.ascba.rebate.adapter.MctPayAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultPayActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.MctBasePay;
import com.ascba.rebate.bean.MctPayAddress;
import com.ascba.rebate.bean.MctPayClass;
import com.ascba.rebate.bean.MctPayDesc;
import com.ascba.rebate.bean.MctPayTitle;
import com.ascba.rebate.bean.Pay;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/12/5 14:27
 * Describe: 优享商家
 */

public class AgentPayActivity extends BaseDefaultPayActivity implements View.OnClickListener {
    private List<MctBasePay> data;
    private MctPayAdapter mctAdapter;
    private View headView;
    private ImageView imHead;
    private TextView tvName;
    private TextView tvClass;

    private int is_buy_agency;
    private String balance_money, region_info, agreement_url, agreement_headname;

    @Override
    protected int bindLayout() {
        return R.layout.activity_agent_pay;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        addData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mctAdapter = new MctPayAdapter(data);

        View footView = new View(this);
        footView.setLayoutParams(new RecyclerView.LayoutParams(-1, (int) ScreenDpiUtils.dp2px(this, 3)));
        mctAdapter.addFooterView(footView);
        mRecyclerView.setAdapter(mctAdapter);
        fv(R.id.btn_apply).setOnClickListener(this);
        fv(R.id.tv_agent_html).setOnClickListener(this);
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.agentJoin, RequestMethod.GET, null);
        executeNetwork(0, "请稍后", request);
    }

    private void updatePayPsdStatus(JSONObject jObj) {
        AppConfig.getInstance().putInt("is_level_pwd", jObj.getIntValue("is_level_pwd"));
    }

    private void setList(JSONObject jObj) {
        List<MctPayClass> level = JSON.parseArray(jObj.getString("agent_setmeal_list"), MctPayClass.class);
        if (level != null && level.size() > 0) {
            data.add(new MctPayTitle("代理加盟"));
            data.addAll(level);
        }

        data.add(new MctPayAddress(region_info, is_buy_agency == 1));

        List<MctPayDesc> interests = JSON.parseArray(jObj.getString("protocol_list"), MctPayDesc.class);
        if (interests != null && interests.size() > 0) {
            data.add(new MctPayTitle("加盟即可享受以下权益", true));
            for (MctPayDesc desc : interests) {
                desc.setContent(desc.getContent().replace("\n", "\n\n"));
            }
            data.addAll(interests);
        }
        mctAdapter.notifyDataSetChanged();
    }

    private void setHead(JSONObject jObj) {
        if (headView == null) {
            headView = getLayoutInflater().inflate(R.layout.mct_pat_head, null);
            imHead = headView.findViewById(R.id.im_head);
            tvName = headView.findViewById(R.id.tv_name);
            tvClass = headView.findViewById(R.id.tv_class);
            mctAdapter.addHeaderView(headView);
        }
        AppConfig appConfig = AppConfig.getInstance();
        Picasso.with(this).load(appConfig.getString("avatar", null)).placeholder(R.mipmap.logo).into(imHead);
        tvName.setText(appConfig.getString("nickname", null));
        tvClass.setText(appConfig.getString("group_name", null));
    }

    private void addData() {
        data = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_apply)
            showPayDialog(mctAdapter.getSelect().getMoney(), balance_money);
        else if (id == R.id.tv_agent_html)
            WebViewBaseActivity.start(this, agreement_headname, agreement_url);
    }

    @Override
    protected void requestPayInfo(String type, String money, int what) {
        AbstractRequest request = buildRequest(UrlUtils.sellerPayment, RequestMethod.POST, Pay.class);
        request.add("pay_type", type);
        request.add("level_id", mctAdapter.getSelect().getId());
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject jObj = JSONObject.parseObject(data);
            balance_money = jObj.getString("balance_money");
            is_buy_agency = jObj.getInteger("is_buy_agency");
            region_info = jObj.getString("region_info");
            agreement_url = jObj.getString("agreement_url");
            agreement_headname = jObj.getString("agreement_headname");
            setHead(jObj);
            setList(jObj);
            updatePayPsdStatus(jObj);
        }
    }

    @Override
    public void payResult(String type) {
        super.payResult(type);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        bundle.putString("info", pay.getSuccess_info());
        startActivity(TextInfoSuccessActivity.class, bundle);
    }
}
