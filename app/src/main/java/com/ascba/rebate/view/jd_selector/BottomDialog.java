package com.ascba.rebate.view.jd_selector;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.ScreenDpiUtils;

public class BottomDialog extends Dialog {

    private AddressSelector selector2;
    public BottomDialog(Context context) {
        super(context, R.style.bottom_dialog);
        init(context);
    }

    private void init(Context context) {
        selector2 = new AddressSelector(context);
        setContentView(selector2.getView());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) ScreenDpiUtils.dp2px(context, 256);
        window.setAttributes(params);

        window.setGravity(Gravity.BOTTOM);
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        dismiss();
        this.selector2.setOnAddressSelectedListener(listener);
    }
}
