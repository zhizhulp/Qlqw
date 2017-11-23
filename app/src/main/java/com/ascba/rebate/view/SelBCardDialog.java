package com.ascba.rebate.view;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.SelBCardAdapter;
import com.ascba.rebate.bean.BankCard;

import java.util.List;

/**
 * Created by 李平 on 2017/9/14 10:15
 * Describe:选择银行卡
 */

public class SelBCardDialog extends BottomSheetDialog implements View.OnClickListener {
    private List<BankCard.BankListBean> data;
    private Callback callback;
    private BankCard.BankListBean selBankCard;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public SelBCardDialog(Context context, List<BankCard.BankListBean> data) {
        super(context);
        this.data = data;
        selBankCard = data.get(0);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.layout_sel_bank_card, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        findViewById(R.id.tv_add_card).setOnClickListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SelBCardAdapter adapter = new SelBCardAdapter(R.layout.select_bank_card_item, data);
        adapter.setCallback(new SelBCardAdapter.Callback() {
            @Override
            public void onClicked(BankCard.BankListBean bankCard) {
                selBankCard = bankCard;
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (callback != null) {
                    callback.onCancel();
                }
                break;
            case R.id.tv_sure:
                if (callback != null) {
                    callback.onSure(selBankCard);
                }
                break;
            case R.id.tv_add_card:
                if (callback != null) {
                    callback.onAddCard();
                }
                break;
        }
    }

    public interface Callback {
        void onCancel();

        void onSure(BankCard.BankListBean bankCard);

        void onAddCard();
    }
}
