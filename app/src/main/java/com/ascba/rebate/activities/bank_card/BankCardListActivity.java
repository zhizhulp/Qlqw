package com.ascba.rebate.activities.bank_card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.personal_identification.PIStartActivity;
import com.ascba.rebate.adapter.BankCardAdapter;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.BankCard;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/9/13 14:37
 * Describe: 银行卡列表
 */

public class BankCardListActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private BankCardAdapter adapter;
    private List<BankCard.BankListBean> data;
    private int deletePosition;//删除的位置
    private View footView;

    @Override
    protected int bindLayout() {
        return R.layout.activity_bank_card_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                requestData();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();
        adapter = new BankCardAdapter(R.layout.bank_card_item, data);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                dm.showAlertDialog2("您确定要删除这张银行卡吗？", null, null, new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        deletePosition = position;
                        requestDelete(data.get(position).getId());
                    }
                });
            }
        });
        footView = getLayoutInflater().inflate(R.layout.btn_add_card, mRecyclerView, false);
        footView.findViewById(R.id.btn_add).setOnClickListener(this);

        View emptyView = getLayoutInflater().inflate(R.layout.bank_card_empty, mRecyclerView, false);
        emptyView.findViewById(R.id.btn_add).setOnClickListener(this);
        adapter.setEmptyView(emptyView);
        mRecyclerView.setAdapter(adapter);
        requestData();
    }

    private void requestDelete(int id) {
        AbstractRequest request = buildRequest(UrlUtils.delbanks, RequestMethod.POST, null);
        request.add("bank_id", id);
        executeNetwork(1, "请稍后", request);
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.getBankList, RequestMethod.GET, BankCard.class);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            BankCard bankCard = (BankCard) result.getData();
            if (data.size() > 0) {
                data.clear();
            }
            List<BankCard.BankListBean> bankList = bankCard.getBank_list();
            if (bankList.size() > 0) {
                data.addAll(bankList);
                if (adapter.getFooterLayoutCount() == 0) {
                    adapter.setFooterView(footView);
                }
            }
            adapter.notifyDataSetChanged();
        } else if (what == 1) {
            showToast(result.getMsg());
            data.remove(deletePosition);
            adapter.notifyItemRemoved(deletePosition);
        }
    }

    //添加银行卡
    @Override
    public void onClick(View v) {
        if (AppConfig.getInstance().getInt("card_status", 0) == 0) {
            dm.showAlertDialog2(getString(R.string.no_pi), null, null, new DialogManager.Callback() {
                @Override
                public void handleLeft() {
                    startActivity(PIStartActivity.class,null);
                }
            });
        }else {
            startActivityForResult(AddBankCardActivity.class, null, CodeUtils.REQUEST_ADD_CARD);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_ADD_CARD && resultCode == RESULT_OK) {
            requestData();
        }
    }
}
