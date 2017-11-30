package com.ascba.rebate.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;

/**
 * Created by Jero on 2017/11/28 0028.
 */

public class EmptyUtils {
    /**
     * adapter.setEmptyView(new EmptyUtils().getView(this,R.layout.empty_bill));
     *
     * @param context
     * @param layoutId
     * @return
     */
    public View getView(Context context, @LayoutRes int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    /**
     * empty标准版:
     * 文字大小13dp 色值808080 距离图片35dp
     * 图片距离顶部84dp  像素 420*310
     */
    public View getView(Context context, @DrawableRes int imId, String tvStr) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.empty_layout, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.empty_im);
        TextView textView = (TextView) inflate.findViewById(R.id.empty_tv);
        imageView.setImageResource(imId);
        textView.setText(tvStr);
        return inflate;
    }
}
