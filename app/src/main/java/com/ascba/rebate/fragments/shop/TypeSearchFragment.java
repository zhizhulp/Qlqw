package com.ascba.rebate.fragments.shop;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.STDetAdapter;
import com.ascba.rebate.adapter.STTabAdapter;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.STBase;
import com.ascba.rebate.bean.ShopTypeDet;
import com.ascba.rebate.bean.ShopTypeHead;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2018/1/31 16:18
 * Describe: 商品分类
 */

public class TypeSearchFragment extends BaseDefaultNetFragment {

    private RecyclerView tabRec;
    private RecyclerView detRec;
    private List<String> tabData;
    private List<STBase> typeData;
    private STDetAdapter stDetAdapter;
    private STTabAdapter stTabAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_type_search;
    }

    @Override
    protected void initViews() {
        super.initViews();
        //
        tabRec = fv(R.id.tab_recyclerView);
        tabRec.setLayoutManager(new LinearLayoutManager(getContext()));
        stTabAdapter = new STTabAdapter(android.R.layout.simple_list_item_1, getTabData());
        stTabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
                int newPos = getNew(position);
                View headView = stDetAdapter.getViewByPosition(detRec, newPos, android.R.id.text1);
                Log.d(TAG, "onItemClick: " + headView);
//                detRec.smoothScrollToPosition(newPos);
//                detRec.smoothScrollBy(0, headView.getTop());
                ((LinearLayoutManager)detRec.getLayoutManager()).scrollToPositionWithOffset(newPos,0);
            }
        });
        tabRec.setAdapter(stTabAdapter);
        //
        detRec = fv(R.id.det_recyclerView);
        detRec.setLayoutManager(new GridLayoutManager(getContext(), 3));
        stDetAdapter = new STDetAdapter(getTypeData());
        stDetAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return typeData.get(position).getSpan();
            }
        });
        detRec.setAdapter(stDetAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private int getNew(int position) {
        for (int i = 0; i < typeData.size(); i++) {
            STBase stBase = typeData.get(i);
            if (stBase instanceof ShopTypeHead) {
                if (((ShopTypeHead) stBase).getPosition() == position) {
                    return i;
                }
            }
        }
        return 0;
    }

    private List<String> getTabData() {
        tabData = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            tabData.add("服饰" + i);
        }
        return tabData;
    }

    private List<STBase> getTypeData() {
        typeData = new ArrayList<>();
        for (int i = 0; i < tabData.size(); i++) {
            typeData.add(new ShopTypeHead(tabData.get(i), i));
            for (int j = 0; j < Math.random() * 10.0; j++) {
                typeData.add(new ShopTypeDet(tabData.get(i) + j,
                        "http://img.ivsky.com/img/tupian/pre/201710/08/qima-001.jpg"));
            }
        }
        return typeData;
    }
}
