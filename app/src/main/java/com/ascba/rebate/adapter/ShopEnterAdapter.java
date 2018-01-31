package com.ascba.rebate.adapter;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.ShopEnter;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


public class ShopEnterAdapter extends BaseQuickAdapter<ShopEnter.InterestsBean, BaseViewHolder> {

    public ShopEnterAdapter() {
        super(R.layout.item_shop_enter);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopEnter.InterestsBean item) {
        helper.setText(R.id.item_title, item.getInterests_title());
        helper.setText(R.id.item_tip, item.getInterests_tip());
        LinearLayout list = helper.getView(R.id.item_list);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) ScreenDpiUtils.dp2px(mContext, 29));
        params.topMargin = (int) ScreenDpiUtils.dp2px(mContext, 0.5f);
        for (ShopEnter.InterestsBean.InterestsIndexBean bean : item.getInterests_index()) {
            TextView view = new TextView(mContext);
            view.setTextColor(mContext.getResources().getColor(R.color.grey_black_tv));
            view.setTextSize(13);
            view.setGravity(Gravity.CENTER_VERTICAL);
            view.setLayoutParams(params);
            view.setText(bean.getInterests_value());
            list.addView(view);
        }
    }
}
