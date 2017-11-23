package com.ascba.rebate.manager;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.utils.ScreenDpiUtils;

/**
 * dialog管理类
 */
public class DialogManager {
    private Context context;

    public DialogManager(Context context) {
        this.context = context;
    }

    public Dialog showAlertDialog(String message, String btnStr, final Callback dialogClick) {
        final Dialog dialogAlter = new Dialog(context, R.style.dialog);
        setCancel(dialogAlter);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view, null);
        dialogAlter.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        tvMsg.setText(message);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setText(btnStr == null ? "确定" : btnStr);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleLeft();
                }
                dialogAlter.dismiss();
            }
        });
        Window window = dialogAlter.getWindow();
        WindowManager.LayoutParams wlp;
        if (window != null) {
            wlp = window.getAttributes();
            wlp.width = (int) ScreenDpiUtils.dp2px(context, 240);
            window.setAttributes(wlp);
        }
        dialogAlter.show();
        return dialogAlter;
    }

    /**
     * 可以处理确定和取消的情况，可以自定义button文字
     */
    public Dialog showAlertDialog2(String message, String leftBtn, String rightBtn, final Callback dialogClick) {
        final Dialog dialogAlterSure = new Dialog(context, R.style.dialog);
        setCancel(dialogAlterSure);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        dialogAlterSure.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//取消按钮
        btSure.setText(leftBtn == null ? "确定" : leftBtn);
        btCancel.setText(rightBtn == null ? "取消" : rightBtn);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleLeft();
                }
                dialogAlterSure.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleRight();
                }
                dialogAlterSure.dismiss();
            }
        });

        tvMsg.setText(message);
        Window window = dialogAlterSure.getWindow();
        WindowManager.LayoutParams wlp;
        if (window != null) {
            wlp = window.getAttributes();
            wlp.width = (int) ScreenDpiUtils.dp2px(context, 240);
            window.setAttributes(wlp);
        }
        dialogAlterSure.show();
        return dialogAlterSure;
    }

    /**
     * 解除三方绑定
     */
    public Dialog showUnbind(String message, final Callback dialogClick) {
        final Dialog dialogAlterSure = new Dialog(context, R.style.dialog);
        setCancel(dialogAlterSure);
        View alertView = LayoutInflater.from(context).inflate(R.layout.dialog_unbind, null);
        dialogAlterSure.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//取消按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleLeft();
                }
                dialogAlterSure.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleRight();
                }
                dialogAlterSure.dismiss();
            }
        });
        tvMsg.setText(String.format((String) tvMsg.getText(), message, message));
        Window window = dialogAlterSure.getWindow();
        WindowManager.LayoutParams wlp;
        if (window != null) {
            wlp = window.getAttributes();
            wlp.width = (int) ScreenDpiUtils.dp2px(context, 270);
            window.setAttributes(wlp);
        }
        dialogAlterSure.show();
        return dialogAlterSure;
    }

    /**
     * 指纹识别开启弹窗
     */
    public Dialog showFingerprint(final Callback dialogClick) {
        final Dialog dialogAlter = new Dialog(context, R.style.dialog);
        setCancel(dialogAlter);
        View alertView = LayoutInflater.from(context).inflate(R.layout.dialog_fingerprint, null);
        dialogAlter.setContentView(alertView);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleLeft();
                }
                dialogAlter.dismiss();
            }
        });
        Window window = dialogAlter.getWindow();
        WindowManager.LayoutParams wlp;
        if (window != null) {
            wlp = window.getAttributes();
            wlp.width = (int) ScreenDpiUtils.dp2px(context, 270);
            window.setAttributes(wlp);
        }
        dialogAlter.show();
        return dialogAlter;
    }

    /**
     * 赠送信息确认
     */
    public Dialog showGiveDialog(String message, final Callback dialogClick) {
        final Dialog dialogAlterSure = new Dialog(context, R.style.dialog);
        setCancel(dialogAlterSure);
        View alertView = LayoutInflater.from(context).inflate(R.layout.alert_view_with_2_button, null);
        dialogAlterSure.setContentView(alertView);
        TextView tvMsg = (TextView) alertView.findViewById(R.id.tv_alert_msg);//提示信息
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        TextView btCancel = (TextView) alertView.findViewById(R.id.tv_alert_cancel);//取消按钮
        btSure.setText("取消");
        btCancel.setText("确定");
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleLeft();
                }
                dialogAlterSure.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleRight();
                }
                dialogAlterSure.dismiss();
            }
        });

        tvMsg.setText(message);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvMsg.getLayoutParams();
        layoutParams.setMargins((int) ScreenDpiUtils.dp2px(context, 28), (int) ScreenDpiUtils.dp2px(context, 20)
                , (int) ScreenDpiUtils.dp2px(context, 28), (int) ScreenDpiUtils.dp2px(context, 20));
        tvMsg.setLayoutParams(layoutParams);
        dialogAlterSure.show();
        return dialogAlterSure;
    }

    /**
     * 人脸检测失败弹框
     */
    public Dialog showFaceFailed(final Callback dialogClick) {
        final Dialog dialogAlter = new Dialog(context, R.style.dialog);
        dialogAlter.setCancelable(false);
        dialogAlter.setCanceledOnTouchOutside(false);
        View alertView = LayoutInflater.from(context).inflate(R.layout.face_check_failed, null);
        dialogAlter.setContentView(alertView);
        TextView btSure = (TextView) alertView.findViewById(R.id.tv_alert_sure);//确定按钮
        alertView.findViewById(R.id.im_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleRight();
                }
                dialogAlter.dismiss();
            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogClick != null) {
                    dialogClick.handleLeft();
                }
                dialogAlter.dismiss();
            }
        });
        Window window = dialogAlter.getWindow();
        WindowManager.LayoutParams wlp;
        if (window != null) {
            wlp = window.getAttributes();
            wlp.width = context.getResources().getDisplayMetrics().widthPixels - (int) ScreenDpiUtils.dp2px(context, 48);
            window.setAttributes(wlp);
        }
        dialogAlter.show();
        return dialogAlter;
    }

    public static abstract class Callback {
        public void handleLeft() {

        }

        public void handleRight() {
        }
    }

    private void setCancel(Dialog d) {
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(false);
    }
}
