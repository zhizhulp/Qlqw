package com.ascba.rebate.manager;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ascba.rebate.R;
import com.ascba.rebate.bean.HomeBean;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;


public class BannerImageLoader extends ImageLoader {
    private int width;
    private int height;

    public BannerImageLoader() {
    }

    public BannerImageLoader(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (path instanceof String) {
            Picasso.with(context).load((String) path).
                    placeholder(R.mipmap.gift_head_loading)
                    .resize(width,height).into(imageView);
        } else if (path instanceof HomeBean.VideoBean) {
            Picasso.with(context).load(((HomeBean.VideoBean) path).getThumb()).
                    placeholder(R.mipmap.gift_head_loading).into(imageView);
        }
    }
}
