package com.ascba.rebate.activities.merchant;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.MctRights;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
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
    private TextView tvEmployee;
    private TextView tvAward;
    private TextView tvAll;
    private TextView tvPay;
    private TextView tvBtm;
    private String sellerUrl;

    @Override
    protected int bindLayout() {
        return R.layout.activity_mct_rights;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        banner = fv(R.id.banner);
        tvTitle = fv(R.id.tv_title);
        tvTime = fv(R.id.tv_time);
        tvStatus = fv(R.id.tv_status);
        tvLeftTime = fv(R.id.tv_left_time);

        tvEmployee = fv(R.id.tv_employee);
        tvAward = fv(R.id.tv_award);
        tvAll = fv(R.id.tv_all);

        tvBtm = fv(R.id.tv_btm);

        fv(R.id.tv_see).setOnClickListener(this);
        tvPay = fv(R.id.tv_pay);
        tvPay.setOnClickListener(this);

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
                if (!TextUtils.isEmpty(sellerUrl)) {
                    WebViewBaseActivity.start(this, "商家权益", sellerUrl);
                } else {
                    Log.e(TAG, "url is null");
                }
                break;
            case R.id.tv_pay://续费
                startActivityForResult(MctPayActivity.class,null, CodeUtils.REQUEST_MCT_PAY);
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            MctRights data = (MctRights) result.getData();
            Picasso.with(this).load(data.getActive_img()).into(banner);
            tvTitle.setText(data.getActive_title());
            tvStatus.setText(data.getActive_desc());
            tvTime.setText(data.getActive_time());
            tvLeftTime.setText(data.getSeller_last_time());
            tvPay.setText(data.getSeller_status_text());

            tvEmployee.setText(data.getSeller_purchase_money());
            tvAward.setText(data.getSeller_referee_money());
            tvAll.setText(data.getSeller_give_money());
            tvBtm.setText(data.getSeller_text());

            sellerUrl = data.getSeller_url();
        }
    }
}
