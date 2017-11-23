package com.ascba.rebate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.SureOrdersEntity;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by lenovo on 2017/10/16.
 * 现金确认列表
 */

public class SureOrdersAdapter extends BaseQuickAdapter<SureOrdersEntity.DataListBean,BaseViewHolder> {
    public SureOrdersAdapter(@LayoutRes int layoutResId, @Nullable List<SureOrdersEntity.DataListBean> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, SureOrdersEntity.DataListBean item) {
        ImageView imageView = helper.getView(R.id.myallorders_icon);//商品图片
        Picasso.with(mContext).load(item.getAvatar()).into(imageView);
        helper.setText(R.id.myallorders_money, item.getMoney());//名称
        helper.setText(R.id.myallorders_category_txt, item.getCreate_time());//交易时间
        helper.setText(R.id.myallorders_pay_cash,item.getPay_type_text());//支付类型
        helper.setText(R.id.await_sure_order,item.getOrder_status_text());//确认状态
    }
}
