package com.ascba.rebate.activities.seller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.score_buy.ScoreBuyHome1Activity;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.RegexUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.yanzhenjie.nohttp.RequestMethod;

import java.util.ArrayList;

/**
 * Created by Jero on 2017/10/18 0018.
 */

public class SellerGiveCreateActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private static final int GET = 705;
    private static final int SAVE = 172;

    private EditText mobile, num;
    private TextView info1, info2, score;
    private Button btn;
    private LinearLayout mobileLay;

    private int type = 0;//0 账户，1 微信

    private int give_min_config;
    private int give_max_config;
    private int store_gift_score;
    private int store_gift_status;
    private String store_gift_status_text;
    private String agreement_url;

    private int s;

    @Override
    protected int bindLayout() {
        return R.layout.activity_seller_give;
    }

    @Override
    protected void initViews(final Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mMoneyBar.setTextTail("赠送记录");
        mMoneyBar.setTailShow(true);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                Bundle bundle = new Bundle();
                bundle.putInt("mine_type", 2);
                startActivity(ScoreBillActivity.class, bundle);
            }
        });
        score = fv(R.id.seller_give_score);
        mobile = fv(R.id.seller_give_mobile);
        num = fv(R.id.seller_give_num);
        info1 = fv(R.id.seller_give_info1);
        info2 = fv(R.id.seller_give_info2);
        mobileLay = fv(R.id.seller_give_mobile_lay);
        btn = fv(R.id.seller_give_btn);
        btn.setOnClickListener(this);
        fv(R.id.seller_give_url).setOnClickListener(this);

        initEdits();
        initTabs();
        requestNetwork();
    }

    private void initEdits() {
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check(s.toString(), mobile.getText().toString());
            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check(num.getText().toString(), s.toString());
            }
        });
    }

    private void initTabs() {
        mobileLay.setVisibility(View.VISIBLE);
        info1.setText("被赠用户必须为系统会员方能赠送，一旦赠送成功则无法退回，请谨慎操作！赠送详情请进入赠送记录查看即可。");
        info2.setText("如赠送用户为非系统会员系统建议通过微信扫码领取。");
        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("系统账户"));
        tabEntities.add(new TabEntity("微信领取"));
        CommonTabLayout commonTabLayout = fv(R.id.tab_lay);
        commonTabLayout.setTabData(tabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                type = position;
                check(num.getText().toString(), mobile.getText().toString());
                if (position == 0) {
                    mobileLay.setVisibility(View.VISIBLE);
                    info1.setText("被赠用户必须为系统会员方能赠送，一旦赠送成功则无法退回，请谨慎操作！赠送详情请进入赠送记录查看即可。");
                    info2.setText("如赠送用户为非系统会员系统建议通过微信扫码领取。");
                } else if (position == 1) {
                    mobileLay.setVisibility(View.GONE);
                    info1.setText("赠送流程：商家输入赠送的礼品分->生成二维码(10分钟内有效)。");
                    info2.setText("领取流程：打开微信->点击扫一扫->领取成功。通过微信扫码（微信注册/手机注册已完成绑定微信）即可领取，一旦用户领取成功，且无法退回。再次扫描二维码则失效！");
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {//singleTask模式进入刷新  startresult模式无法调用
        super.onNewIntent(intent);
        requestNetwork();
    }

    private void check(String num, String mobile) {
        if (num.length() <= 0 || (type == 0 && mobile.length() != 11)) {
            btn.setEnabled(false);
            return;
        }
        try {
            s = Integer.parseInt(num);
            if (s < give_min_config || s > give_max_config) {
                btn.setEnabled(false);
            } else if (type == 1 || RegexUtils.isMobilePhoneNum(mobile)) {
                btn.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            btn.setEnabled(false);
        }
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.PurchaseGiveCreate, RequestMethod.GET, null);
        executeNetwork(GET, "请稍后", request);
    }

    private void requestSaveNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.PurchaseGiveSave, RequestMethod.POST, null);
        if (type == 0)
            request.add("mobile", mobile.getText().toString());
        request.add("score", s);
        executeNetwork(SAVE, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == GET) {
            JSONObject object = JSON.parseObject(result.getData().toString());
            give_min_config = object.getIntValue("give_min_config");
            give_max_config = object.getIntValue("give_max_config");
            store_gift_score = object.getIntValue("store_gift_score");
            store_gift_status = object.getIntValue("store_gift_status");
            store_gift_status_text = object.getString("store_gift_status_text");
            agreement_url = object.getString("agreement_url");
            num.setHint("请输入礼品额度，最低" + give_min_config + "起送");
            score.setText("" + store_gift_score);
        } else if (what == SAVE) {
            JSONObject object = JSON.parseObject(result.getData().toString());
            boolean tips = object.getBoolean("tips");
            if (type == 1) {
                String redirectUrl = object.getString("redirect_url");
                WebViewBaseActivity.start(this, "领取礼品", redirectUrl);
                num.setText("");
            } else if (tips) {
                dm.showAlertDialog(result.getMsg(), "我知道了", null);
                mobile.setText("");
            } else if (type == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("info", "" + s);
                bundle.putInt("type", 1);
                bundle.putString("title", "赠送成功");
                startActivity(PurchaseSuccessActivity.class, bundle);
//                showToast(result.getMsg());
//                dm.showAlertDialog("赠送成功!", "我知道了", null);
                store_gift_score -= s;
                score.setText("" + store_gift_score);
                num.setText("");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.seller_give_btn) {
            int s = Integer.parseInt(num.getText().toString());
            if (store_gift_status == 0 || s > store_gift_score) {
                num.setText("");
                dm.showAlertDialog2(store_gift_status_text, null, null, new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        startActivity(new Intent(SellerGiveCreateActivity.this, ScoreBuyHome1Activity.class)
                                .putExtra("page", "give"));
                    }
                });
            } else if (type == 0) {
                dm.showGiveDialog("手机号码：" + mobile.getText().toString()
                        + "\n礼品额度：" + num.getText().toString()
                        + "\n请确认信息，是否需要赠送？", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        requestSaveNetwork();
                    }
                });
            } else {
                requestSaveNetwork();
            }
        } else if (v.getId() == R.id.seller_give_url) {
            WebViewBaseActivity.start(this, "礼品赠送说明", agreement_url);
        }
    }

    class TabEntity implements CustomTabEntity {
        public String title;

        public TabEntity(String title) {
            this.title = title;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return 0;
        }

        @Override
        public int getTabUnselectedIcon() {
            return 0;
        }
    }
}
