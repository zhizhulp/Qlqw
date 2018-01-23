package com.ascba.rebate.activities.score_shop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.fragments.score_shop.BtmFragment;
import com.ascba.rebate.fragments.score_shop.TopFragment;
import com.ascba.rebate.view.vertical_drag.DragLayout;

/**
 * Created by 李平 on 2017/9/20 14:51
 * Describe:商品详情
 */

public class GiftGoodsDetailsActivity extends BaseDefaultNetActivity {

    private TextView tvBtmScore;
    private TextView tvApply;
    private View latBtm;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TextView getTvBtmScore() {
        return tvBtmScore;
    }

    public TextView getTvApply() {
        return tvApply;
    }

    public View getLatBtm() {
        return latBtm;
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_gift_goods_details;
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_TRANSPARENT_NOTALL;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        int goodsId = extras.getInt("goods_id");
        int scene = extras.getInt("scene");

        TopFragment topFragment = TopFragment.getInstance(goodsId,scene);
        final BtmFragment btmFragment = new BtmFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.top, topFragment).add(R.id.bottom, btmFragment)
                .commit();

        DragLayout.ShowNextPageNotifier notifier = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                btmFragment.initView(url);
            }

            @Override
            public void onDragTop() {

            }
        };
        DragLayout draglayout = (DragLayout) findViewById(R.id.dragLayout);
        draglayout.setNextPageListener(notifier);

        tvBtmScore = fv(R.id.tv_btm_score);
        tvApply = fv(R.id.tv_apply);

        latBtm = fv(R.id.lat);
    }


}
