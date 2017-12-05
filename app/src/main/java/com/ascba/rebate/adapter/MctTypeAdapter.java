package com.ascba.rebate.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.MctType;

import java.util.ArrayList;
import java.util.List;


public class MctTypeAdapter extends BaseAdapter implements Filterable {
    private List<MctType> data;
    private ArrayFilter mFilter;
    private ArrayList<MctType> mUnfilteredData;

    public MctTypeAdapter(List<MctType> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_text, parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvType.setText(data.get(position).getText());
        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    class ViewHolder {
        TextView tvType;

        ViewHolder(View root) {
            tvType = root.findViewById(R.id.tv_mct_type);
        }
    }


    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<>(data);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<MctType> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<MctType> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<MctType> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    MctType pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if(pc.getText()!=null && pc.getText().contains(prefixString)){

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            data = (List<MctType>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
