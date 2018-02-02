package com.ascba.rebate.fragments.shop;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.ShopEnterShowActivity;
import com.ascba.rebate.activities.shop.ShopInActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ShopSuccess;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopPayFragment extends BaseDefaultNetFragment implements View.OnClickListener {
    private View.OnClickListener btnClick;
    public ShopSuccess shopSuccess;

    public void setPayBtnClick(View.OnClickListener btnClick) {
        this.btnClick = btnClick;
    }

    public void pageSync() {
        requestNetwork();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_shop_pay;
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (btnClick != null)
            fv(R.id.btn_commit).setOnClickListener(btnClick);
        else {
            Log.e(TAG, "initViews: setPayBtnClick()设置监听");
            getActivity().finish();
        }
        fv(R.id.shop_success_class).setOnClickListener(this);
        fv(R.id.shop_store_info_tv).setOnClickListener(this);
        fv(R.id.shop_see_btn).setOnClickListener(this);

        requestNetwork();
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.storePerfectShow, RequestMethod.GET, ShopSuccess.class);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            shopSuccess = (ShopSuccess) result.getData();
            setDetail();
            AppConfig.getInstance().putInt("is_level_pwd", shopSuccess.getIs_level_pwd());
        }
    }

    private void setDetail() {
        setText(R.id.shop_name_tv, shopSuccess.getStore_name());
        setText(R.id.shop_top_tv, shopSuccess.getStore_title());
        setText(R.id.shop_success_url, shopSuccess.getStore_login_url());
        setText(R.id.shop_success_name, shopSuccess.getStore_login_name());
        setText(R.id.shop_success_password, shopSuccess.getStore_login_passd());
        setText(R.id.shop_success_type, shopSuccess.getStore_type());
        setText(R.id.shop_success_class, shopSuccess.getStore_primary_class());
        TextView shopStatus = fv(R.id.shop_success_status);
        switch (shopSuccess.getStore_status()) {
            case 1:
                shopStatus.setText("未激活");
                shopStatus.setTextColor(getResources().getColor(R.color.shop_det_red));
                break;
            case 2:
                shopStatus.setText("已激活");
                shopStatus.setTextColor(getResources().getColor(R.color.shop_green));
                break;
            case 3:
                shopStatus.setText("已过期");
                shopStatus.setTextColor(getResources().getColor(R.color.shop_det_red));
                break;
        }
        setText(R.id.shop_success_monwy, shopSuccess.getStore_money());
        if (shopSuccess.getTop_tip_show() == 1) {
            TextView warnTv = fv(R.id.shop_warn_tv);
            warnTv.setVisibility(View.VISIBLE);
            warnTv.setText(shopSuccess.getTop_tip());
        }
        if (shopSuccess.getButton_status() == 1) {
            Button button = fv(R.id.btn_commit);
            button.setVisibility(View.VISIBLE);
            button.setText(shopSuccess.getButton_tip());
        }
    }

    private void setText(@IdRes int id, String str) {
        ((TextView) fv(id)).setText(str);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.shop_store_info_tv) {
            startActivity(new Intent(getContext(), ShopInActivity.class).putExtra("is_first", false));
        } else if (v.getId() == R.id.shop_see_btn) {
            startActivity(ShopEnterShowActivity.class, null);
        } else if (v.getId() == R.id.shop_success_class) {
            WebViewBaseActivity.start(getContext(), shopSuccess.getStore_class_h5_title(), shopSuccess.getStore_class_h5());
        }
    }
}
