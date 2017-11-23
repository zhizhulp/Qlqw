package com.ascba.rebate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascba.rebate.R;


/**
 * 竞拍底部导航
 */

public class AppTabs extends LinearLayout implements View.OnClickListener {
    private ImageView imZero;
    private ImageView imThree;
    private ImageView imFour;

    private TextView tvZero;
    private TextView tvThree;
    private TextView tvFour;

    private TextView tvThreeNoty;

    private Callback callback;
    private int filPos = -1;//代表被选择的位置

    public AppTabs(Context context) {
        super(context);
        initViews(context);
    }

    public AppTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);

    }

    public AppTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }


    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.shop_tabs, this, true);
        imZero = ((ImageView) findViewById(R.id.im_tabs_zero));
        imThree = ((ImageView) findViewById(R.id.im_tabs_three));
        imFour = ((ImageView) findViewById(R.id.im_tabs_four));

        tvZero = ((TextView) findViewById(R.id.tv_tabs_zero));
        tvThree = ((TextView) findViewById(R.id.tv_tabs_three));
        tvFour = ((TextView) findViewById(R.id.tv_tabs_four));

        tvThreeNoty = (TextView) findViewById(R.id.im_tabs_three_noty);

        View viewZero = findViewById(R.id.tabs_zero_par);

        View viewThree = findViewById(R.id.tabs_three_par);
        View viewFour = findViewById(R.id.tabs_four_par);

        viewZero.setOnClickListener(this);
        viewThree.setOnClickListener(this);
        viewFour.setOnClickListener(this);

        statusChaByPosition(0, filPos);

    }

    public interface Callback {
        void clickZero(View v);

        void clickThree(View v);

        void clickFour(View v);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabs_zero_par://首页
                statusChaByPosition(0, filPos);
                if (callback != null)
                    callback.clickZero(v);
                break;
            case R.id.tabs_three_par://消息
                statusChaByPosition(1, filPos);
                if (callback != null)
                    callback.clickThree(v);
                break;
            case R.id.tabs_four_par://我
                statusChaByPosition(2, filPos);
                if (callback != null)
                    callback.clickFour(v);
                break;
        }
    }

    /**
     * 改变tab状态
     *
     * @param currentPos 当前位置
     * @param beforePos  之前位置
     */
    public void statusChaByPosition(int currentPos, int beforePos) {
        if (beforePos != currentPos) {
            filPos = currentPos;
            switch (beforePos) {
                case 0:
                    imZero.setImageResource(R.mipmap.tab_main);
                    tvZero.setTextColor(getResources().getColor(R.color.grey_black_tv));
                    break;
                case 1:
                    imThree.setImageResource(R.mipmap.tab_msg);
                    tvThree.setTextColor(getResources().getColor(R.color.grey_black_tv));
                    break;
                case 2:
                    imFour.setImageResource(R.mipmap.tab_me);
                    tvFour.setTextColor(getResources().getColor(R.color.grey_black_tv));
                    break;
                default:
                    break;
            }
            switch (currentPos) {
                case 0:
                    tvZero.setTextColor(getResources().getColor(R.color.blue_btn));
                    imZero.setImageResource(R.mipmap.tab_main_select);
                    break;
                case 1:
                    tvThree.setTextColor(getResources().getColor(R.color.blue_btn));
                    imThree.setImageResource(R.mipmap.tab_msg_select);
                    break;
                case 2:
                    tvFour.setTextColor(getResources().getColor(R.color.blue_btn));
                    imFour.setImageResource(R.mipmap.tab_me_select);
                    break;
                default:
                    break;
            }
        }
    }

    public int getFilPos() {
        return filPos;
    }

    public void setFilPos(int filPos) {
        this.filPos = filPos;
    }

    public void setThreeNoty(int count) {
        if (count > 0) {
            tvThreeNoty.setVisibility(VISIBLE);
        } else {
            tvThreeNoty.setVisibility(GONE);
        }
        tvThreeNoty.setText(String.valueOf(count));
    }

    public int getThreeNotyNum() {
        return Integer.parseInt(tvThreeNoty.getText().toString());
    }

}
