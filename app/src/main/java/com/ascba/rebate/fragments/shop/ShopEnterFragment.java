package com.ascba.rebate.fragments.shop;

import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.personal_identification.PIStartActivity;
import com.ascba.rebate.activities.shop.ShopInActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ShopEnter;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopEnterFragment extends BaseDefaultNetFragment implements View.OnClickListener {
    private ShopEnter shopEnter;
    private int cardStatus = 0;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_shop_enter;
    }

    @Override
    protected void initViews() {
        super.initViews();
        fv(R.id.btn_commit).setOnClickListener(this);
        fv(R.id.purchase_url).setOnClickListener(this);

        requestNetwork();
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.storeEntrance, RequestMethod.GET, ShopEnter.class);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_commit) {
            if (shopEnter.getStore().getTip_status() == 0 || cardStatus == 1)
                startActivity(ShopInActivity.class, null);
            else if (shopEnter.getStore().getTip_status() == 1) {
                dm.showAlertDialog2(shopEnter.getStore().getTip_text(), "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        cardStatus = -1;
                        startActivity(PIStartActivity.class, null);
                    }
                });
            }
        } else if (v.getId() == R.id.purchase_url) {
            WebViewBaseActivity.start(getContext(), "", shopEnter.getStore().getStore_settled_h5());
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            shopEnter = (ShopEnter) result.getData();


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (cardStatus == -1)
            cardStatus = AppConfig.getInstance().getInt("card_status", 0);
    }
}
