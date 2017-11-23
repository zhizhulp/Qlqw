package com.ascba.rebate.fragments.main;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.bill.VoucherBillActivity;
import com.ascba.rebate.activities.mine.AwardActivity;
import com.ascba.rebate.activities.mine.BalanceActivity;
import com.ascba.rebate.activities.mine.SettingActivity;
import com.ascba.rebate.activities.user_msg.UserMsgActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.MineEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.WaveViewByBezier;
import com.ascba.rebate.view.picasso.RoundedCornersTransformation;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

import static com.ascba.rebate.R.id.group_name;


/**
 * A simple {@link Fragment} subclass.
 * 我的
 */
public class MineFragment extends BaseDefaultNetFragment implements View.OnClickListener {
    private WaveViewByBezier waveViewByBezier3;
    private ImageView ivHead, ivInvite;
    private TextView tvNumber, tvGroup, tvRemainder, tvCoupon, tvTickets, tvBenefit, tvPurchase;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            waveViewByBezier3.startAnimation();
        }
    };

    @Override
    protected int bindLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViews() {
        super.initViews();
        RelativeLayout rlPresent = fv(R.id.present_more_rl);
        RelativeLayout rlPrivilege = fv(R.id.myprivilege_rl);
        RelativeLayout rlBill = fv(R.id.bill_rl);
        RelativeLayout rlRemainder = fv(R.id.remainder_rl);
        RelativeLayout rlCoupon = fv(R.id.coupon_rl);
        RelativeLayout rlSetting = fv(R.id.setting_rl);
        tvGroup = fv(group_name);
        tvNumber = fv(R.id.id_mine_number);
        ivInvite = fv(R.id.invite_iv);
        ivHead = fv(R.id.mine_head);
        tvRemainder = fv(R.id.mine_remainder_tv);
        tvCoupon = fv(R.id.mine_coupon_tv);
        tvTickets = fv(R.id.mine_tickets);
        ivInvite.setOnClickListener(this);
        rlPresent.setOnClickListener(this);
        rlPrivilege.setOnClickListener(this);
        rlBill.setOnClickListener(this);
        rlRemainder.setOnClickListener(this);
        rlCoupon.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        tvBenefit = fv(R.id.mine_benefit_tv);
        tvPurchase = fv(R.id.mine_purchase_tv);
        fv(R.id.purchase_rl).setOnClickListener(this);
        fv(R.id.benefit_rl).setOnClickListener(this);
        WaveViewByBezier waveViewByBezier1 = fv(R.id.waveview1);
        waveViewByBezier3 = fv(R.id.waveview3);
        waveViewByBezier1.startAnimation();
        handler.sendEmptyMessageDelayed(2, 1500);
        View latUserHead = fv(R.id.lat_user_head);
        latUserHead.setOnClickListener(this);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setPrimaryColorsId(R.color.blue_btn, android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {//请求数据
                requestMineNetwork(0);
            }
        });
        requestMineNetwork(0);
        mStatusView.empty();
    }

    public void requestMineNetwork(int what) {
        AbstractRequest request = buildRequest(UrlUtils.my, RequestMethod.POST, MineEntity.class);
        executeNetwork(what, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        MineEntity mineEntity = (MineEntity) result.getData();
        MineEntity.UserInfoBean userInfo = mineEntity.getUserInfo();
        MineEntity.UserDataBean userData = mineEntity.getUserData();
        Picasso.with(getContext()).load(userInfo.getAvatar())
                .transform(new RoundedCornersTransformation(7, 0))
                .placeholder(R.mipmap.head_loading).into(ivHead);
        tvNumber.setText(userInfo.getNickname());
        tvGroup.setText(userInfo.getGroup_name());
        tvRemainder.setText(userData.getMoney());
        tvCoupon.setText(userData.getVoucher());
        tvTickets.setText(userData.getWhite_score());
        tvBenefit.setText(userData.getBill());
        tvPurchase.setText(userData.getRecharge());
        Picasso.with(getContext()).load(userData.getJoinPic())
                .placeholder(R.mipmap.mine_loading).into(ivInvite);
        AppConfig config = AppConfig.getInstance();
        config.putString("mobile", userInfo.getMobile());
        config.putString("nickname", userInfo.getNickname());
        config.putString("sex", userInfo.getSex());
        config.putInt("age", userInfo.getAge());
        config.putString("avatar", userInfo.getAvatar());
        config.putInt("card_status", userInfo.getCard_status());
        config.putInt("is_level_pwd", userInfo.getIs_level_pwd());
        config.putInt("company_status", userInfo.getCompany_status());
        config.putString("group_name", userInfo.getGroup_name());
        config.putString("location", userInfo.getLocation());
        config.putInt("is_weixin", userInfo.getIs_weixin());
        mStatusView.content();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_user_head://进入个人信息界面
                startActivityForResult(UserMsgActivity.class, null, CodeUtils.REQUEST_USER_HEAD);
                break;
            case R.id.present_more_rl://礼品账单
                startActivity(ScoreBillActivity.class, null);
                break;
            case R.id.myprivilege_rl://我的特权
                //startActivity(PrivilegeActivity.class, null);
                break;
            case R.id.bill_rl://现金账单
//                startActivity(BillActivity.class, null);
                break;
            case R.id.remainder_rl://现金余额
                startActivityForResult(BalanceActivity.class, null, CodeUtils.REQUEST_RECHARGE);
                break;
            case R.id.benefit_rl://累计现金分红
                Bundle bb = new Bundle();
                bb.putInt("mine_type", 5);
                startActivity(BillActivity.class, bb);
                break;
            case R.id.coupon_rl://累计福利分红
                startActivity(VoucherBillActivity.class, null);
                break;
            case R.id.purchase_rl://佣金充值总额
                Bundle b = new Bundle();
                b.putInt("mine_type", 5);
                startActivity(ScoreBillActivity.class, b);
                break;
            case R.id.setting_rl://设置
                startActivity(SettingActivity.class, null);
                break;
            case R.id.invite_iv://邀请奖励
                startActivity(AwardActivity.class, null);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_USER_HEAD && resultCode == Activity.RESULT_OK) {//修改用户头像
            boolean updateHead = data.getBooleanExtra("update_head", false);
            boolean updateName = data.getBooleanExtra("update_name", false);
            if (updateHead) {
                Picasso.with(getContext()).load(AppConfig.getInstance().getString("avatar", null))
                        .transform(new RoundedCornersTransformation(7, 0))
                        .placeholder(R.mipmap.head_loading).into(ivHead);
            }
            if (updateName) {
                tvNumber.setText(AppConfig.getInstance().getString("nickname", null));
            }
        } else if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            requestMineNetwork(0);
        } else if (requestCode == CodeUtils.REQUEST_RECHARGE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: ");
            requestMineNetwork(0);
        }
    }
}
