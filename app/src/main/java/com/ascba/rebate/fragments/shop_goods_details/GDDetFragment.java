package com.ascba.rebate.fragments.shop_goods_details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.shop.ShopActivity;
import com.ascba.rebate.fragments.score_shop.BtmFragment;
import com.ascba.rebate.fragments.score_shop.TopFragment;
import com.ascba.rebate.view.vertical_drag.DragLayout;

/**
 * 商品详情-商品详情
 */
public class GDDetFragment extends Fragment {


    public GDDetFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gddet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GDDescTopFragment topFragment = new GDDescTopFragment();
        final BtmFragment btmFragment = new BtmFragment();

        getChildFragmentManager().beginTransaction()
                .add(R.id.top, topFragment).add(R.id.bottom, btmFragment)
                .commit();

        DragLayout.ShowNextPageNotifier notifier = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                btmFragment.initView("http://www.baidu.com");
                ((ShopActivity) getActivity()).switchTitle(false);
            }

            @Override
            public void onDragTop() {
                ((ShopActivity) getActivity()).switchTitle(true);
            }
        };
        DragLayout draglayout = view.findViewById(R.id.dragLayout);
        draglayout.setNextPageListener(notifier);
    }
}
