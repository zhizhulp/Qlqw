package com.ascba.rebate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.ModuleEntity;
import com.ascba.rebate.utils.ModulesUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 李平 on 2017/10/11 18:15
 * Describe: 首页-导航栏
 */


public class HomeGridAdapter extends BaseAdapter {
    private Context context;
    private List<ModuleEntity> data;

    public HomeGridAdapter(Context context, List<ModuleEntity> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_module, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ModuleEntity navBean = data.get(position);
        viewHolder.text.setText(navBean.getNav_name());
        int isRead = navBean.getIs_read();
        viewHolder.point3.setVisibility(isRead == 3 ? View.VISIBLE : View.GONE);
        if (isRead == 0 || isRead == 3) {
            viewHolder.point.setImageResource(0);
            viewHolder.point.clearAnimation();
        } else if (isRead == 2) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.module_new_anim);
            viewHolder.point.setImageResource(R.mipmap.module_new);
            viewHolder.point.startAnimation(animation);
        } else {//isRead == 1
            viewHolder.point.setImageResource(R.mipmap.module_red);
            viewHolder.point.clearAnimation();
        }
        if (ModulesUtils.navActivity == 0)
            Picasso.with(context).load(navBean.getNav_icon()).placeholder(R.mipmap.module_loading).into(viewHolder.icon);
        else if (ModulesUtils.navActivity == 1)
            Picasso.with(context).load(navBean.getNav_activity()).placeholder(R.mipmap.module_loading).into(viewHolder.icon);
        return convertView;
    }

    private class ViewHolder {
        private ImageView icon;
        private ImageView point;
        private ImageView point3;
        private TextView text;

        ViewHolder(View rootView) {
            icon = (ImageView) rootView.findViewById(R.id.item_module_icon);
            point = (ImageView) rootView.findViewById(R.id.item_module_read);
            point3 = (ImageView) rootView.findViewById(R.id.item_module_read3);
            text = (TextView) rootView.findViewById(R.id.item_module_name);
        }

    }
}
