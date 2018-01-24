package com.ascba.rebate.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.AgentItem;
import com.ascba.rebate.utils.DrawableChangeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public class AgentAdapter extends BaseMultiItemQuickAdapter<AgentItem, BaseViewHolder> {
    public static final int TITLE_TYPE = 529;
    public static final int NUM_TYPE = 624;

    public AgentAdapter(List<AgentItem> data) {
        super(data);
        addItemType(TITLE_TYPE, R.layout.item_agent_title);
        addItemType(NUM_TYPE, R.layout.item_agent_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AgentItem item) {
        switch (item.getItemType()) {
            case TITLE_TYPE:
                helper.setText(R.id.tv_title, item.getTitle());
                DrawableChangeUtils.setShapeDrawableColor((TextView) helper.getView(R.id.tv_title),
                        DrawableChangeUtils.Drawable_LEFT, item.getColor());
                break;
            case NUM_TYPE:
                helper.setText(R.id.tv_title, item.getTitle());
                DrawableChangeUtils.setShapeDrawableColor((TextView) helper.getView(R.id.tv_title),
                        DrawableChangeUtils.Drawable_LEFT, item.getColor());
                helper.setText(R.id.tv_num, item.getContent());
                break;
        }
    }

    private void setLeftColor(TextView title, @ColorInt int color) {
        GradientDrawable drawable = (GradientDrawable) title.getCompoundDrawables()[0];
        drawable.setColor(color);
    }
}
