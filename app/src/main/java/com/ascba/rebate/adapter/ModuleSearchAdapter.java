package com.ascba.rebate.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.ModuleEntity;
import com.squareup.picasso.Picasso;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModuleSearchAdapter extends BaseQuickAdapter<ModuleEntity, BaseViewHolder> {

    public ModuleSearchAdapter() {
        super(R.layout.item_module_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleEntity item) {
        Picasso.with(mContext).load(item.getNav_icon())
                .placeholder(R.mipmap.module_loading).into((ImageView) helper.getView(R.id.item_module_icon));
        helper.setText(R.id.item_module_name, item.getNav_name());
    }
}