package com.ascba.rebate.activities.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.AboutEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.PackageUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2017/9/15 0015.
 */

public class AboutActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private RelativeLayout rlIntroduced, rlScore;
    private TextView tvEdition;
    private static final int ABOUT = 681;
    private AboutEntity aboutEntity;
    private TextView tvPhone,tvSkype;

    @Override
    protected int bindLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        rlIntroduced = fv(R.id.about_introduced_rl);
        rlScore = fv(R.id.about_score_rl);
        tvEdition = fv(R.id.about_edition_tv);
        tvPhone = fv(R.id.tv_phone);
        tvSkype = fv(R.id.tv_skype);
        fv(R.id.phone_rl).setOnClickListener(this);
        //fv(R.id.about_skype_rl).setOnClickListener(this);
        rlIntroduced.setOnClickListener(this);
        rlScore.setOnClickListener(this);
        tvEdition.setText("版本V" + PackageUtils.getPackageVersion(this));
        requestAboutNetwork(ABOUT);
    }

    public void requestAboutNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.about, RequestMethod.GET, AboutEntity.class);
        request.add("mobileType", "android");
        executeNetwork(what, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        switch (what) {
            case ABOUT:
                aboutEntity = (AboutEntity) result.getData();
//                tvEdition.setText("版本V" + aboutEntity.getEdition());
//                if (aboutEntity.getScoreUrl().equals("http://www.andriod.com")) {
//                    fv(R.id.about_score_rl).setVisibility(View.GONE);
//                }
                tvSkype.setText(aboutEntity.getSkype());
                tvPhone.setText(aboutEntity.getMobile());
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_introduced_rl:
                if (aboutEntity != null)
                    WebViewBaseActivity.start(this, "项目介绍", aboutEntity.getAppabout());
                break;
            case R.id.about_score_rl:
                if (aboutEntity != null)
                    WebViewBaseActivity.start(this, "给我们评分", aboutEntity.getScoreUrl());
                break;
            case R.id.about_skype_rl:
                break;
            case R.id.phone_rl:
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent1);
                break;
        }
    }
}
