package com.ascba.rebate.activities.merchant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;

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

        fv(R.id.tv_see).setOnClickListener(this);
        fv(R.id.tv_pay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_see://查看商家服务
                break;
            case R.id.tv_pay://续费

                break;
        }
    }
}
