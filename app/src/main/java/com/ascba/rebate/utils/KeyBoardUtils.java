package com.ascba.rebate.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/2/2.
 */
public class KeyBoardUtils {
    private static final String TAG = "EditTextUtil";
    /**隐藏输入法
     * @param context
     * @param toGetWindowTokenView
     */
    public static void hideKeyboard(Context context, View toGetWindowTokenView){
        showKeyboard(context, null, toGetWindowTokenView, false);
    }
    /**显示输入法
     * @param context
     * @param et
     */
    public static void showKeyboard(Context context, EditText et){
        showKeyboard(context, et, true);
    }
    /**显示/隐藏输入法
     * @param context
     * @param et
     * @param show
     */
    public static void showKeyboard(Context context, EditText et, boolean show){
        showKeyboard(context, et, null, show);
    }
    /**显示输入法
     * @param context
     * @param et
     * @param toGetWindowTokenView(为null时toGetWindowTokenView = et) 包含et的父View，键盘根据toGetWindowTokenView的位置来弹出/隐藏
     */
    public static void showKeyboard(Context context, EditText et, View toGetWindowTokenView) {
        showKeyboard(context, et, toGetWindowTokenView, true);
    }
    /**显示/隐藏输入法
     * @param context
     * @param et
     * @param toGetWindowTokenView(为null时toGetWindowTokenView = et) 包含et的父View，键盘根据toGetWindowTokenView的位置来弹出/隐藏
     * @param show
     */
    public static void showKeyboard(Context context, EditText et, View toGetWindowTokenView, boolean show){
        if (context == null) {
            Log.e(TAG, "showKeyboard  context == null >> return;");
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);//imm必须与context唯一对应
        if (toGetWindowTokenView == null) {
            Log.w(TAG, "showKeyboard   toGetWindowTokenView == null");
            toGetWindowTokenView = et;
        }
        if (toGetWindowTokenView == null) {
            Log.e(TAG, "showKeyboard  toGetWindowTokenView == null && et == null  >> return;");
            return;
        }

        if (show == false) {
            imm.hideSoftInputFromWindow(toGetWindowTokenView.getWindowToken(), 0);
            if (et != null) {
                et.clearFocus();
            }
        } else {
            if (et != null) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                imm.toggleSoftInputFromWindow(toGetWindowTokenView.getWindowToken()
                        , InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }
}
