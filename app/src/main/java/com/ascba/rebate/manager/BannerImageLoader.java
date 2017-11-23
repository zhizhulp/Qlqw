package com.ascba.rebate.manager;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.HomeBean;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;


public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (path instanceof String) {
            Picasso.with(context).load((String) path).
                    placeholder(R.mipmap.gift_head_loading)
                    .fit().centerCrop().into(imageView);
        } else if (path instanceof HomeBean.VideoBean) {
            Picasso.with(context).load(((HomeBean.VideoBean) path).getThumb()).
                    placeholder(R.mipmap.gift_head_loading).into(imageView);
        }
    }

    @Override
    public ImageView createImageView(Context context) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }
}
