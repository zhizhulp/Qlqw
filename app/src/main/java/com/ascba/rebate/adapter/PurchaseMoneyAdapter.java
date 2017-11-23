package com.ascba.rebate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.PurchaseEntity;

import java.util.ArrayList;
import java.util.List;


public class PurchaseMoneyAdapter extends BaseAdapter {
    int select = 0;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public PurchaseEntity.MoneyConfigBean getSelectItem() {
        return data.get(select);
    }

    private Context context;
    private List<PurchaseEntity.MoneyConfigBean> data;

    public PurchaseMoneyAdapter(Context context, List<PurchaseEntity.MoneyConfigBean> data) {
        this.context = context;
        if (data != null)
            this.data = data;
        else
            this.data = new ArrayList<>();
    }

    public void setData(List<PurchaseEntity.MoneyConfigBean> data) {
        if (data != null)
            this.data = data;
        else
            this.data = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PurchaseEntity.MoneyConfigBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_seller_purchase, parent, false);
        TextView textView = (TextView) convertView;
        PurchaseEntity.MoneyConfigBean item = getItem(position);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(item.getCz_money());
        stringBuffer.append("元");
        if (!item.getCz_desc().isEmpty()) {
            stringBuffer.append("  (" + item.getCz_desc() + ")");
        }
        textView.setText(stringBuffer.toString());
        if (select == position) {
            textView.setEnabled(true);
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setEnabled(false);
            textView.setTextColor(context.getResources().getColor(R.color.grey_black_tv));
        }
        return convertView;
    }
}
