package com.ascba.rebate.adapter;

import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.ModuleEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModuleListAdapter extends BaseMultiItemQuickAdapter<ModuleEntity, BaseViewHolder> {
    public static final int TYPE_HEAD = 4;
    public static final int TYPE_ITEM = 1;

    private boolean isSelect = false;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ModuleListAdapter(boolean is_select, List<ModuleEntity> data) {
        super(data);
        addItemType(TYPE_HEAD, R.layout.item_module_title);
        isSelect = is_select;
        if (isSelect)
            addItemType(TYPE_ITEM, R.layout.item_module_edit);
        else
            addItemType(TYPE_ITEM, R.layout.item_module);
    }

    public ModuleListAdapter() {
        this(false, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleEntity item) {
        switch (item.getItemType()) {
            case TYPE_HEAD:
                getHead(helper, item);
                break;
            case TYPE_ITEM:
                if (isSelect)
                    getEdit(helper, item);
                else
                    getItem(helper, item);
                break;
        }
    }

    private void getHead(BaseViewHolder helper, ModuleEntity item) {
        helper.setText(R.id.item_title, item.getCategory_name());
    }

    private void getItem(BaseViewHolder helper, ModuleEntity item) {
        Picasso.with(mContext).load(Uri.parse(item.getNav_icon()))
                .placeholder(R.mipmap.module_loading).into((ImageView) helper.getView(R.id.item_module_icon));
        helper.setText(R.id.item_module_name, item.getNav_name());
        int isRead = item.getIs_read();
        helper.getView(R.id.item_module_read3).setVisibility(isRead == 3 ? View.VISIBLE : View.GONE);
        if (isRead == 0 || isRead == 3) {
            helper.setImageResource(R.id.item_module_read, 0);
            helper.getView(R.id.item_module_read).clearAnimation();
        } else if (isRead == 2) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.module_new_anim);
            helper.setImageResource(R.id.item_module_read, R.mipmap.module_new);
            helper.getView(R.id.item_module_read).startAnimation(animation);
        } else {//isRead == 1
            helper.setImageResource(R.id.item_module_read, R.mipmap.module_red);
            helper.getView(R.id.item_module_read).clearAnimation();
        }
    }

    private void getEdit(BaseViewHolder helper, ModuleEntity item) {
        Picasso.with(mContext).load(Uri.parse(item.getNav_icon()))
                .placeholder(R.mipmap.module_loading).into((ImageView) helper.getView(R.id.item_module_icon));
        helper.setText(R.id.item_module_name, item.getNav_name());
        switch (item.isSelect()) {
            case 1:
                helper.setImageResource(R.id.item_module_select, R.mipmap.module_is_select);
                break;
            case 0:
                helper.setImageResource(R.id.item_module_select, R.mipmap.module_add);
                break;
        }
        helper.addOnClickListener(R.id.item_module_select);
    }
}