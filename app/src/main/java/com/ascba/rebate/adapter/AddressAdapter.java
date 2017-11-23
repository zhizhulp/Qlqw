package com.ascba.rebate.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ascba.rebate.R;
import com.ascba.rebate.bean.AddressEntity;


public class AddressAdapter extends BaseQuickAdapter<AddressEntity, BaseViewHolder> {

    public AddressAdapter() {
        super(R.layout.address_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressEntity item) {
        helper.setText(R.id.item_address_name, item.getConsignee());
        helper.setText(R.id.item_address_phone, item.getMobile());
        helper.setText(R.id.item_address_tv, item.getAddress_detail());
        helper.setChecked(R.id.item_address_check, item.getType() == 1);
        helper.getView(R.id.item_address_check).setEnabled(item.getType() == 0);
        helper.setText(R.id.item_address_check, item.getType() == 0 ? "设为默认" : "默认地址");
        helper.addOnClickListener(R.id.item_address_del);
        helper.addOnClickListener(R.id.item_address_edit);
        helper.addOnClickListener(R.id.item_address_check);
    }
}
