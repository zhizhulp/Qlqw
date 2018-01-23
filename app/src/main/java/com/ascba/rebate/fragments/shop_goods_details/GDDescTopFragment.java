package com.ascba.rebate.fragments.shop_goods_details;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.manager.BannerImageLoader;
import com.ascba.rebate.view.RadiusBackgroundSpan;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.util.ScreenUtils;

/**
 * 商品详情-商品基本信息
 */
public class GDDescTopFragment extends BaseDefaultNetFragment {

    private Banner banner;
    private TextView tvTitle;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_gddesc;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initBanner();
        tvTitle = fv(R.id.tv_title);
        showTitle("自营", "小天鹅洗衣机小天鹅洗衣机小天鹅洗衣机小天鹅洗衣机小天鹅洗衣机小天鹅洗衣机小天鹅洗衣机");
    }

    private void initBanner() {
        banner = fv(R.id.banner);
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        int width = ScreenUtils.widthPixels(getContext());
        int height = width * 512 / 820;
        ViewGroup.LayoutParams lp = banner.getLayoutParams();
        lp.height = height;
        banner.setImageLoader(new BannerImageLoader(width, height));
        banner.setImages(addImages());
        banner.start();
    }

    private List<String> addImages() {
        List<String> strs = new ArrayList<>();
        strs.add("http://img.ivsky.com/img/bizhi/pre/201212/29/total_war_rome_2.jpg");
        strs.add("http://img.ivsky.com/img/bizhi/pre/201212/29/total_war_rome_2-001.jpg");
        strs.add("http://img.ivsky.com/img/bizhi/pre/201212/29/total_war_rome_2-002.jpg");
        return strs;
    }

    private void showTitle(String tag, String title) {
        if (TextUtils.isEmpty(tag)) {
            tvTitle.setText(title);
        } else {
            RadiusBackgroundSpan span = new RadiusBackgroundSpan(MyApplication.getInstance()
                    , ContextCompat.getColor(MyApplication.getInstance(), R.color.shop_det_red), tag);
            span.setRightMarginDpValue(5);
            SpannableString ss = new SpannableString(tag + title);
            ss.setSpan(span, 0, tag.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTitle.setText(ss);
        }
    }

    private void stopBanner() {
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBanner();
    }
}
