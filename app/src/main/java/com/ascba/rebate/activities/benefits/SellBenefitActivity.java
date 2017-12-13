package com.ascba.rebate.activities.benefits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.adapter.BenefitAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.BenefitMain;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ScoreHome;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

/**
 * Created by 李平 on 2017/10/30 11:29
 * Describe: 寄卖收益
 */

public class SellBenefitActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private List<BenefitMain.Benefit> benefitList;
    private BenefitAdapter adapter;
    private View headView;
    private TextView tvTitle;
    private TextView tvMoney;
    private ViewFlipper tvStatus;
    private TextView btnStatus;
    private int onSale;
    private String sellUrl;

    @Override
    protected int bindLayout() {
        return R.layout.activity_sell_benefit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                requestData();
            }
        });
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                Bundle bundle = new Bundle();
                bundle.putInt("mine_type", 5);
                startActivity(BillActivity.class, bundle);
            }
        });
        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.CapitalProfitList, RequestMethod.GET, BenefitMain.class);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            BenefitMain benefitMain = (BenefitMain) result.getData();
            List<BenefitMain.Benefit> benefits = benefitMain.getRanking_list();
            ScoreHome.Member member = benefitMain.getMember();
            setAdapter(benefits);
            addHeader(member);
        }
    }

    private void addHeader(ScoreHome.Member member) {
        if (headView == null) {
            headView = LayoutInflater.from(this).inflate(R.layout.benefit_head, mRecyclerView, false);
            tvTitle = (TextView) headView.findViewById(R.id.tv_title);
            tvMoney = (TextView) headView.findViewById(R.id.tv_money);
            tvStatus = (ViewFlipper) headView.findViewById(R.id.tv_status);
            btnStatus = (TextView) headView.findViewById(R.id.btn_status);
            btnStatus.setOnClickListener(this);
            ImageView imageView = (ImageView) headView.findViewById(R.id.iv_top);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.height = getResources().getDisplayMetrics().widthPixels * 433 / 540;
            imageView.setLayoutParams(params);
            adapter.addHeaderView(headView);
        }
        sellUrl = member.getAgreement_url();
        onSale = member.getOn_sale();
        if (onSale == 0) {//未开通
            btnStatus.setText("开通寄卖");
            btnStatus.setVisibility(View.VISIBLE);
            btnStatus.setEnabled(true);
        } else if (onSale == 1) {//开通已查看
            btnStatus.setText("关闭寄卖");
            btnStatus.setVisibility(View.GONE);
            btnStatus.setEnabled(false);
        } else if (onSale == 2) {//开通未查看
            btnStatus.setText("查看详情");
            btnStatus.setVisibility(View.VISIBLE);
            btnStatus.setEnabled(true);
        } else {
            btnStatus.setVisibility(View.GONE);
            btnStatus.setEnabled(false);
        }
        tvTitle.setText("今日寄卖动态（" + member.getRanking() + "）");
        tvMoney.setText(setMoney(member.getToday_money()));
        setMoneySpanString(member.getNeed_money(), member.getScore(),
                member.getLast_bill_time(), member.getLeast_settlement());
    }

    private SpannableString setMoney(String beforeStr) {
        SpannableString ss = new SpannableString(beforeStr + "元");
        ss.setSpan(new RelativeSizeSpan(0.38f), ss.length() - 1, ss.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void setMoneySpanString(String need_money, int score, String last_time, float clearUnit) {
        tvStatus.removeAllViews();
        if (onSale == 0) {//未开通
            SpannableString ss = new SpannableString("当前礼品积分：" + score);
            addView(ss);
        } else if (onSale == 1) {//开通已查看
            String oneStr = "距离系统结算到账还差";
            String twoStr = "元，最低结算单元：" + clearUnit + "元";
            SpannableString ss1 = new SpannableString(oneStr + need_money + twoStr);
            ss1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_btn)),
                    oneStr.length(), ss1.length() - twoStr.length(), SPAN_INCLUSIVE_EXCLUSIVE);
            TextView tv = new TextView(this);
            tv.setText(ss1);
            tv.setTextColor(ContextCompat.getColor(this, R.color.grey_black_tv3));
            tv.setTextSize(12);
            tvStatus.addView(tv);

            String s2 = "当前寄卖总额：" + score;
            addView(s2);
        } else if (onSale == 2) {//开通未查看
            SpannableString ss = new SpannableString("提示：上一次结算日期" + last_time);
            addView(ss);
        }
        if (tvStatus.getChildCount() > 1) {
            tvStatus.setAutoStart(true);
        } else {
            tvStatus.setAutoStart(false);
        }
    }

    private void addView(CharSequence ss) {
        TextView tv = new TextView(this);
        tv.setText(ss);
        tv.setTextColor(ContextCompat.getColor(this, R.color.grey_black_tv3));
        tv.setTextSize(12);
        tvStatus.addView(tv);
    }

    private void setAdapter(List<BenefitMain.Benefit> benefits) {
        if (benefitList == null) {
            benefitList = new ArrayList<>();
        } else {
            benefits.clear();
        }
        benefitList.addAll(benefits);
        if (adapter == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new BenefitAdapter(R.layout.benefit_item, benefitList);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (onSale == 0) {//未开通
            SaleProtocolActivity.jumpIntent(this, sellUrl);
        } else if (onSale == 1) {//开通已查看
            // TODO: 2017/10/31 关闭接口暂时不做
        } else if (onSale == 2) {//开通未查看
            BenefitDetActivity.jumpResultIntent(this, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            requestData();
        }
    }
}
