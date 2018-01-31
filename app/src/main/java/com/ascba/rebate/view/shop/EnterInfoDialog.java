package com.ascba.rebate.view.shop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.ascba.rebate.R;

public class EnterInfoDialog extends Dialog {
    private Context context;
    private TextView title, msg;

    public EnterInfoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_shop_enter);
        title = findViewById(R.id.tv_title);
        msg = findViewById(R.id.tv_msg);
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(String str) {
        title.setText(str);
    }

    public void setMsg(String str) {
        msg.setText("\u3000\u3000"+str);
    }
}
