package com.ascba.rebate.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.DebugAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/9/13 10:39
 * Describe:选择支付方式
 */

public class DebugSelectDialog extends BottomSheetDialog {
    private Callback callback;
    private String type;

    private List<String> types;
    private DebugAdapter pt;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public DebugSelectDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.layout_debug, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        //关闭对话框
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //去付款
        findViewById(R.id.debug_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.go(type);
                }
            }
        });
        //列表
        RecyclerView rvTypes = (RecyclerView) findViewById(R.id.debug_type_list);
        types = new ArrayList<>();
        initPayTypesData(types);
        pt = new DebugAdapter(types);
        pt.setCallback(new DebugAdapter.Callback() {
            @Override
            public void onClicked(String arg0) {
                type = arg0;
            }
        });
        rvTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTypes.setAdapter(pt);

        View parent = (View) contentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        contentView.measure(0, 0);
        behavior.setPeekHeight(contentView.getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);

    }

    private void initPayTypesData(List<String> types) {
        if (types != null)
            types.clear();
        types.add("http://app.qlqwgw.com/");
        types.add("http://apidebug.qlqwp2p.com/");
        types.add("http://demoapp.qlqwp2p.com/");
    }

    public interface Callback {
        void go(String type);
    }
}
