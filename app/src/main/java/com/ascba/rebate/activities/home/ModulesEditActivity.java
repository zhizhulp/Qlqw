package com.ascba.rebate.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ascba.rebate.R;
import com.ascba.rebate.adapter.ModuleDraggableAdapter;
import com.ascba.rebate.adapter.ModuleListAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ModuleEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.ModulesUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.MoneyBar;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModulesEditActivity extends BaseDefaultNetActivity {
    private ModuleListAdapter moduleListAdapter;
    private ModuleDraggableAdapter moduleDraggableAdapter;
    private List<ModuleEntity> homeList, backList;// 可改变的home数据及其比对备份
    private List<ModuleEntity> moduleList;

    @Override
    protected int bindLayout() {
        return R.layout.activity_modules_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setTextTail("完成");
        mMoneyBar.setTailShow(true);
        mMoneyBar.setCallBack(new MoneyBar.CallBack() {
            @Override
            public void clickBack(View back) {
                if (ModulesUtils.equalList(homeList, backList))
                    finish();
                else
                    dm.showAlertDialog2("是否保存已编辑的内容", "取消", "保存", new DialogManager.Callback() {
                        @Override
                        public void handleRight() {
                            save();
                        }

                        @Override
                        public void handleLeft() {
                            finish();
                        }
                    });
            }

            @Override
            public void clickTail() {
                if (ModulesUtils.equalList(homeList, backList))
                    finish();
                else
                    save();
            }
        });

        homeList = JSON.parseArray(getIntent().getStringExtra(ModulesUtils.HOME), ModuleEntity.class);
        backList = new ArrayList<>(homeList);
        moduleDraggableAdapter = new ModuleDraggableAdapter(homeList);
        RecyclerView topRecyclerView = fv(R.id.module_edit_top);
        topRecyclerView.setAdapter(moduleDraggableAdapter);
        topRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragAndSwipeCallback(moduleDraggableAdapter));
        itemTouchHelper.attachToRecyclerView(topRecyclerView);
        moduleDraggableAdapter.enableDragItem(itemTouchHelper, 0, true);
// 自动排序无需监听
//        moduleDraggableAdapter.setOnItemDragListener(new OnItemDragListener() {
//            @Override
//            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.i(TAG, "onItemDragStart: " + pos);
//            }
//
//            @Override
//            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
//                Log.i(TAG, "onItemDragMoving: from = " + from + "//to = " + to);
//            }
//
//            @Override
//            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
//                Log.i(TAG, "onItemDragEnd: " + pos);
//
//            }
//        });
        topRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_module_select) {
//                    if (homeList.size() <= 4) {
//                        showToast("最少保留三项！");
//                        return;
//                    }
                    delHome(position);
                }
            }
        });

        moduleList = JSON.parseArray(getIntent().getStringExtra(ModulesUtils.LIST), ModuleEntity.class);
        moduleListAdapter = new ModuleListAdapter(true, moduleList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return moduleListAdapter.getItem(position).getItemType();
            }
        });
        mRecyclerView.setAdapter(moduleListAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_module_select) {
                    ModuleEntity item = (ModuleEntity) adapter.getItem(position);
                    if (item.isSelect() == 0)
                        if (homeList.size() == 8)
                            showToast("首页最多添加7个应用");
                        else
                            addHome(position, item);
                }
            }
        });

        ModulesUtils.selectItem(homeList, moduleList);
    }

    private void delHome(int position) {
        ModulesUtils.delItem(homeList.get(position), moduleList);
        moduleDraggableAdapter.remove(position);
        moduleListAdapter.notifyDataSetChanged();
    }

    private void addHome(int position, ModuleEntity item) {
        ModulesUtils.addItem(item, moduleList);
        int last = homeList.size() - 1;
        moduleDraggableAdapter.add(last, item);
        moduleListAdapter.notifyDataSetChanged();
    }

    private void save() {
        requestNetwork();
        Intent intent = new Intent();
        intent.putExtra(ModulesUtils.HOME, JSON.toJSONString(homeList));
        setResult(RESULT_OK, intent);
    }

    private void requestNetwork() {
        StringBuilder list = new StringBuilder();
        for (ModuleEntity moduleEntity : homeList) {
            list.append(moduleEntity.getNav_id());
            list.append(",");
        }
        String navs;
        if (list.length() < 3)
            navs = "";
        else
            navs = list.substring(0, list.length() - 3);
        AbstractRequest request = buildRequest(UrlUtils.setNavMember, RequestMethod.POST, null);
        request.add("setNavs", navs);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            showToast(result.getMsg());
            finish();
        }
    }
}
