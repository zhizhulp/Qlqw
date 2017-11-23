package com.ascba.rebate.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.ModuleEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModuleDraggableAdapter extends BaseItemDraggableAdapter<ModuleEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ModuleDraggableAdapter(List<ModuleEntity> data) {
        super(R.layout.item_module_edit, data);
    }

    @Override
    public int getItemCount() {
        // 屏蔽更多按钮
        return super.getItemCount() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 添加虚线框（footer方法实现）后的处理 (无foot状态最长为size-1，有foot状态长度为size，position从0开始)
        if (position == mData.size() - 1)
            return FOOTER_VIEW;
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleEntity item) {
        Picasso.with(mContext).load(item.getNav_icon())
                .placeholder(R.mipmap.module_loading).into((ImageView) helper.getView(R.id.item_module_icon));
        helper.setText(R.id.item_module_name, item.getNav_name());
        helper.addOnClickListener(R.id.item_module_select);
    }
}