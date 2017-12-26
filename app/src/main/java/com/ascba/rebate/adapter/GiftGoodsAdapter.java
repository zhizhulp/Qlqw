package com.ascba.rebate.adapter;

import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.ScoreHome;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/9/20 18:28
 * Describe: 积分商品
 */

public class GiftGoodsAdapter extends BaseQuickAdapter<ScoreHome.GiftGoods, BaseViewHolder> {

    public GiftGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<ScoreHome.GiftGoods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreHome.GiftGoods item) {
        Picasso.with(mContext).load(item.getImg()).placeholder(R.mipmap.gift_goods_loading)
                .resizeDimen(R.dimen.goods_dimen,R.dimen.goods_dimen)
                .into((ImageView) helper.getView(R.id.im_icon));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_score, item.getPrice());
        TextView view = helper.getView(R.id.tv_money);
        String toMoney = item.getTo_money();
        String money = item.getMoney();
        if (!TextUtils.isEmpty(toMoney)) {
            setMoney(view,toMoney);
        }
        if (!TextUtils.isEmpty(money) && !money.equals("0.00")) {
            view.setText("+" + money + "元");
            view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private void setMoney(TextView view, String toMoney) {
        SpannableString ss=new SpannableString("原价 " + toMoney + "元");
        ss.setSpan(new StrikethroughSpan(), 3, ss.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(ss);
    }
}
