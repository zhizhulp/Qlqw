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
import com.ascba.rebate.utils.PayUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.jd_selector.BottomDialog;
import com.ascba.rebate.view.jd_selector.City;
import com.ascba.rebate.view.jd_selector.County;
import com.ascba.rebate.view.jd_selector.OnAddressSelectedListener;
import com.ascba.rebate.view.jd_selector.Province;
import com.ascba.rebate.view.jd_selector.Street;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AgentPayActivity extends BaseDefaultPayActivity implements View.OnClickListener {
    private List<MctBasePay> data;
    private MctPayAdapter mctAdapter;
    private View headView;
    private ImageView imHead;
    private TextView tvName;
    private TextView tvClass;

    private String balance_money, agreement_url, agreement_headname;
    private MctPayAddress address;

    private BottomDialog dialog;

    @Override
    protected int bindLayout() {
        return R.layout.activity_agent_pay;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                super.clickBack(back);
                startActivity(AgentActivity.class, null);
            }
        });
        addData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mctAdapter = new MctPayAdapter(data);

        View footView = new View(this);
        footView.setLayoutParams(new RecyclerView.LayoutParams(-1, (int) ScreenDpiUtils.dp2px(this, 3)));
        mctAdapter.addFooterView(footView);
        mctAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.lat_address) {
                    if (dialog == null) {
                        dialog = new BottomDialog(AgentPayActivity.this);
                        dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                            @Override
                            public void onAddressSelected(Province province, City city, County county, Street street) {
                                dialog.dismiss();
                                address.setIsBuyAgency(2);
                                address.setSelectID(street.getId());
                                address.setAddress(String.format("%s-%s-%s-%s", province.getName(), city.getName(), county.getName(), street.getName()));
                                mctAdapter.notifyItemChanged(position + 1);
                            }
                        });
                    }
                    if (address.getIsBuyAgency() != 1)
                        dialog.show();
                }
            }
        });
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

        data.add(address);

        List<MctPayDesc> interests = JSON.parseArray(jObj.getString("protocol_list"), MctPayDesc.class);
        if (interests != null && interests.size() > 0) {
            data.add(new MctPayTitle("加盟即可享受以下权益", true, false));
            for (MctPayDesc desc : interests) {
                desc.setContent(desc.getContent().replace("\n", "\n\n"));
            }
            interests.get(interests.size() - 1).setLast(true);
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
        if (id == R.id.btn_apply) {
            if (address.getIsBuyAgency() == 0) {
                showToast("请选择代理区域");
                return;
            }
            showPayDialog(mctAdapter.getSelect().getMoney(), balance_money);
        } else if (id == R.id.tv_agent_html)
            WebViewBaseActivity.start(this, agreement_headname, agreement_url);
    }

    @Override
    protected void requestPayInfo(String type, String money, int what) {
        AbstractRequest request = buildRequest(UrlUtils.agentPayment, RequestMethod.POST, Pay.class);
        request.add("pay_type", type);
        request.add("setmeal_id", mctAdapter.getSelect().getId());
        request.add("region_id", address.getSelectID());
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            String data = (String) result.getData();
            JSONObject jObj = JSONObject.parseObject(data);
            balance_money = jObj.getString("balance_money");
            address = new MctPayAddress(jObj.getString("region_info"),
                    jObj.getInteger("is_buy_agency"), jObj.getInteger("region_id"));
            if (address.getSelectID() == 0) {
                address.setIsBuyAgency(0);
            }
            agreement_url = jObj.getString("agreement_url");
            agreement_headname = jObj.getString("agreement_headname");
            setHead(jObj);
            setList(jObj);
            updatePayPsdStatus(jObj);
        }
    }

    @Override
    public void payResult(String type) {
        if (address.getIsBuyAgency() != 1) {
            address.setIsBuyAgency(1);
            mctAdapter.notifyDataSetChanged();
        }
        if (type.equals(PayUtils.BALANCE)) {
            BigDecimal balance, money;
            balance = new BigDecimal(balance_money);
            money = new BigDecimal(payUtils.money);
            balance = balance.subtract(money);
            balance_money = balance.toString();
        }
        payUtils.clear();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        bundle.putString("info", pay.getSuccess_info());
        startActivity(TextInfoSuccessActivity.class, bundle);
    }

    @Override
    public void onBackPressed() {
        startActivity(AgentActivity.class, null);
        super.onBackPressed();
    }
}
