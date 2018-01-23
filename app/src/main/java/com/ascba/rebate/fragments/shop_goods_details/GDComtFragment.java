package com.ascba.rebate.fragments.shop_goods_details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ascba.rebate.R;

/**
 * 商品详情-商品评价
 */
public class GDComtFragment extends Fragment {


    public GDComtFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gdcomt, container, false);
    }

}
