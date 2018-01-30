package com.ascba.rebate.fragments.shop;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.personal_identification.PIStartActivity;
import com.ascba.rebate.activities.shop.ShopInActivity;
import com.ascba.rebate.adapter.ShopEnterAdapter;
import com.ascba.rebate.adapter.ShopEnterTopAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ShopEnter;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.shop.EnterInfoDialog;
import com.ascba.rebate.view.shop.EnterTopDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopEnterFragment extends BaseDefaultNetFragment implements View.OnClickListener {
    private ShopEnter shopEnter;
    private int cardStatus = 0;
    private boolean isShow = false;

    private ShopEnterAdapter shopEnterAdapter;
    private LinearLayout headView;
    private ShopEnterTopAdapter shopEnterTopAdapter;
    private EnterInfoDialog showDialog;

    public void setShowType() {
        isShow = true;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_shop_enter;
    }

    @Override
    protected void initViews() {
        super.initViews();
        fv(R.id.btn_commit).setOnClickListener(this);
        fv(R.id.purchase_url).setOnClickListener(this);

        if (isShow) {
            fv(R.id.lat_bottom).setVisibility(View.GONE);
            fv(R.id.btn_commit).setVisibility(View.GONE);
        }
        shopEnterAdapter = new ShopEnterAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(shopEnterAdapter);

        requestNetwork();
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.storeEntrance, RequestMethod.GET, ShopEnter.class);
        executeNetwork(0, "请稍后", request);
    }

    private void addHeader() {
        if (headView != null)
            return;
        shopEnterTopAdapter = new ShopEnterTopAdapter();
        shopEnterTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopEnter.PatternBean bean = (ShopEnter.PatternBean) adapter.getItem(position);
                requestInfoNetwork(bean.getPattern_id());
                if (showDialog == null) {
                    showDialog = new EnterInfoDialog(getActivity(), R.style.dialog);
                }
                showDialog.setTitle(bean.getPattern_title());
            }
        });
        headView = (LinearLayout) getLayoutInflater().inflate(R.layout.item_shop_enter_top, null);
        RecyclerView topRecyclerView = headView.findViewById(R.id.item_recycler);
        topRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        topRecyclerView.setAdapter(shopEnterTopAdapter);
        topRecyclerView.addItemDecoration(new EnterTopDecoration(getContext()));
        shopEnterAdapter.addHeaderView(headView);
    }

    private void requestInfoNetwork(int id) {
        AbstractRequest request = buildRequest(UrlUtils.storeSalePattern, RequestMethod.GET, null);
        request.add("pattern_id", id);
        executeNetwork(1, "请稍后", request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_commit) {
            if (shopEnter.getTip_status() == 0 || cardStatus == 1)
                startActivity(ShopInActivity.class, null);
            else if (shopEnter.getTip_status() == 1) {
                dm.showAlertDialog2(shopEnter.getTip_text(), "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        cardStatus = -1;
                        startActivity(PIStartActivity.class, null);
                    }
                });
            }
        } else if (v.getId() == R.id.purchase_url) {
            WebViewBaseActivity.start(getContext(), shopEnter.getStore_settled_h5_title(), shopEnter.getStore_settled_h5());
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            addHeader();
            shopEnter = (ShopEnter) result.getData();
            shopEnterAdapter.setNewData(shopEnter.getInterests());
            shopEnterTopAdapter.setNewData(shopEnter.getPattern());
            ViewGroup.LayoutParams params = headView.getLayoutParams();
            params.height = getTopHeight(shopEnter.getPattern().size());
        } else if (what == 1) {
            String str = JSON.parseObject(result.getData().toString()).getString("value");
            showDialog.setMsg(str);
            showDialog.show();
        }
    }

    private int getTopHeight(int length) {
        //                     105 + line * 97 + (line - 1)*1      line = (length + 1) / 2
        return (int) ScreenDpiUtils.dp2px(getContext(), 104 + ((length + 1) / 2) * 98);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (cardStatus == -1)
            cardStatus = AppConfig.getInstance().getInt("card_status", 0);
    }
}
