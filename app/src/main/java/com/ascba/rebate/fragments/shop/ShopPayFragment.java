package com.ascba.rebate.fragments.shop;

import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopPayFragment extends BaseDefaultNetFragment {
    private View.OnClickListener btnClick;

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
        Log.i(TAG, "initViews: "+getClass());
        if (btnClick != null)
            fv(R.id.btn_commit).setOnClickListener(btnClick);
        else {
            Log.e(TAG, "initViews: 请setBtnClick（）设置监听");
            getActivity().finish();
        }
    }
}
