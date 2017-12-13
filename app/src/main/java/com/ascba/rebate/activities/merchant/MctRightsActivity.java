package com.ascba.rebate.activities.merchant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.MctRights;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import org.raphets.roundimageview.RoundImageView;

/**
 * Created by 李平 on 2017/12/4 18:44
 * Describe: 商家权益
 */

public class MctRightsActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private RoundImageView banner;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvStatus;
    private TextView tvLeftTime;
    private TextView tvEmployeeMct;
    private TextView tvEmployeeAgent;
    private TextView tvAward;
    private TextView tvAll;
    private TextView tvPay;
    private TextView tvBtm;

    private MctRights data;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_rights;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        banner = fv(R.id.banner);
        LinearLayout.LayoutParams bannerLayoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        bannerLayoutParams.height = (getResources().getDisplayMetrics().widthPixels
                - (int) ScreenDpiUtils.dp2px(this, 28)) * 35 / 83;

        tvTitle = fv(R.id.tv_title);
        tvTime = fv(R.id.tv_time);
        tvStatus = fv(R.id.tv_status);
        tvStatus.setOnClickListener(this);

        tvLeftTime = fv(R.id.tv_left_time);
        tvPay = fv(R.id.tv_pay);
        tvPay.setOnClickListener(this);

        tvEmployeeMct = fv(R.id.tv_employee_mct);
        tvEmployeeAgent = fv(R.id.tv_employee_agent);
        tvAward = fv(R.id.tv_award);
        tvAll = fv(R.id.tv_all);

        tvBtm = fv(R.id.tv_btm);

        fv(R.id.tv_see).setOnClickListener(this);
        fv(R.id.lat_employee_mct).setOnClickListener(this);
        fv(R.id.lat_employee_agent).setOnClickListener(this);
        fv(R.id.lat_award).setOnClickListener(this);
        fv(R.id.lat_all).setOnClickListener(this);

        requestData();
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.interests, RequestMethod.GET, MctRights.class);
        executeNetwork(0, "请稍后", request);
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
            case R.id.lat_employee_mct:// 入驻商家收益
                startActivity(new Intent(this, BillActivity.class).putExtra("mine_type", 6));
                break;
            case R.id.lat_employee_agent:// 推荐代理收益
                startActivity(new Intent(this, BillActivity.class).putExtra("mine_type", 7));
                break;
            case R.id.lat_award:// 礼品分收益
                startActivity(new Intent(this, ScoreBillActivity.class).putExtra("mine_type", 7));
                break;
            case R.id.lat_all:// 礼品分流水收益
                startActivity(new Intent(this, ScoreBillActivity.class).putExtra("mine_type", 6));
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            data = (MctRights) result.getData();
            Picasso.with(this).load(data.getActive_img())
                    .placeholder(R.mipmap.gift_head_loading)
                    .into(banner);
            tvTitle.setText(data.getActive_title());
            if (data.getActive_desc().isEmpty())
                tvStatus.setVisibility(View.GONE);
            else
                tvStatus.setText(data.getActive_desc());
            tvTime.setText(data.getActive_time());
            tvLeftTime.setText(data.getSeller_last_time());
            tvPay.setText(data.getSeller_status_text());

            tvEmployeeMct.setText(data.getSeller_purchase_money());
            tvEmployeeAgent.setText(data.getSeller_agent_money());
            tvAward.setText(data.getSeller_getpay_money());
            tvAll.setText(data.getSeller_referee_money());
            tvBtm.setText(data.getSeller_text());

            GradientDrawable drawable = (GradientDrawable) fv(R.id.lat_status).getBackground();
            drawable.setColor(Color.parseColor("#" + data.getActive_color()));
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
