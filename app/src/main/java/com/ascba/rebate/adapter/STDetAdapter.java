package com.ascba.rebate.adapter;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.STBase;
import com.ascba.rebate.bean.ShopTypeDet;
import com.ascba.rebate.bean.ShopTypeHead;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
public class STDetAdapter extends BaseMultiItemQuickAdapter<STBase,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public STDetAdapter(List<STBase> data) {
        super(data);
        addItemType(0,android.R.layout.simple_list_item_1);
        addItemType(1,R.layout.shop_type_det);
    }

    @Override
    protected void convert(BaseViewHolder helper, STBase item) {
        Log.d(TAG, "convert: "+helper.getAdapterPosition());
        switch (item.getItemType()){
            case 0:
                ShopTypeHead item1 = (ShopTypeHead) item;
                helper.setText(android.R.id.text1,item1.getText());
                helper.setBackgroundColor(android.R.id.text1, ContextCompat.getColor(mContext,android.R.color.holo_orange_light));
                break;
            case 1:
                ShopTypeDet item2 = (ShopTypeDet) item;
                helper.setText(R.id.tv_name,item2.getName());
                helper.setBackgroundColor(R.id.tv_name, ContextCompat.getColor(mContext,android.R.color.holo_purple));

                Picasso.with(mContext).load(item2.getImage()).into((ImageView) helper.getView(R.id.imageView5));
                break;
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
        Log.d(TAG, "onCreateViewHolder: ");
        return baseViewHolder;
    }
}
