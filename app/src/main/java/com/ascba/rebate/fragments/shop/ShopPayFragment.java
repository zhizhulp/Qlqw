package com.ascba.rebate.fragments.shop;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.ShopEnterActivity;
import com.ascba.rebate.activities.shop.ShopInActivity;
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
    private ShopSuccess shopSuccess;

    public void setBtnClick(View.OnClickListener btnClick) {
        this.btnClick = btnClick;
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
            Log.e(TAG, "initViews: 请setBtnClick（）设置监听");
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
        }
    }

    private void setDetail() {
        setText(R.id.shop_warn_tv, shopSuccess.getTop_tip());
        setText(R.id.shop_warn_tv, shopSuccess.getTop_tip());
    }

    private void setText(@IdRes int id, String str) {
        ((TextView) fv(id)).setText(str);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.shop_store_info_tv) {
            startActivity(new Intent(getContext(), ShopInActivity.class).putExtra("is_first", false));
        } else if (v.getId() == R.id.shop_see_btn) {
            startActivity(new Intent(getContext(), ShopEnterActivity.class).putExtra("type", 1));
        } else if (v.getId() == R.id.shop_success_class) {
            WebViewBaseActivity.start(getContext(), "", "");
        }
    }
}
