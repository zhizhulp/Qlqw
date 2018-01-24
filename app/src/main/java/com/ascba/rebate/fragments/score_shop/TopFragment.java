package com.ascba.rebate.fragments.score_shop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.score_shop.GiftGoodsDetailsActivity;
import com.ascba.rebate.activities.setting.AddressActivity;
import com.ascba.rebate.activities.setting.AddressAddActivity;
import com.ascba.rebate.activities.success.TextInfoSuccessActivity;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.AddressEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.bean.ScoreHome;
import com.ascba.rebate.manager.BannerImageLoader;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.NumberFormatUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.utils.WindowsUtils;
import com.ascba.rebate.view.NumberBtn;
import com.ascba.rebate.view.vertical_drag.CustScrollView;
import com.yanzhenjie.nohttp.RequestMethod;
import com.youth.banner.Banner;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

/**
 * 礼品商品详情上半个
 */
public class TopFragment extends BaseDefaultNetFragment implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvScore;
    private TextView tvMoney;
    private NumberBtn numberBtn;
    private TextView tvDesc;
    private TextView tvHtml;
    private TextView tvBtmScore;
    private int goodsId;
    private TextView tvAddress;
    private int addressID;
    private TextView tvYunfei;
    private TextView tvApply;
    private float totalScore;
    private float perScore;
    private float yunfei;//运费
    private Banner banner;
    private View viewUser;
    private TextView tvUserName;
    private TextView tvMobile;
    private int buttonTip;
    private View latBtm;
    private View latContent;
    private int type = 1;//1礼品分商品，2福利券商品
    private int scene = 1;//1礼品商城 2礼品采购
    private TextView tvAddMoney;
    private float totalVo;
    private float perAddMoney;
    private int isMoney;
    private CustScrollView scrollView;
    private FrameLayout mBarLayout;

    public TopFragment() {
    }

    public static TopFragment getInstance(int goodsId, int scene) {
        TopFragment top = new TopFragment();
        Bundle b = new Bundle();
        b.putInt("goods_id", goodsId);
        b.putInt("scene", scene);
        top.setArguments(b);
        return top;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_top;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvTitle = fv(R.id.tv_title);
        tvScore = fv(R.id.tv_score);
        tvMoney = fv(R.id.tv_money);
        tvYunfei = fv(R.id.tv_yunfei);
        tvAddMoney = fv(R.id.tv_add_money);

        viewUser = fv(R.id.lat_user);
        tvUserName = fv(R.id.tv_user_name);
        tvMobile = fv(R.id.tv_user_mobile);
        numberBtn = fv(R.id.number_btn);
        numberBtn.setCallback(new NumberBtn.Callback() {
            @Override
            public void clickSub(int sub) {
                isEnoughSetting(sub);
            }

            @Override
            public void clickAdd(int add) {
                isEnoughSetting(add);
            }

            @Override
            public void stockWarn() {
                //tvApply.setEnabled(false);
                //tvApply.setText("暂时缺货");
            }
        });

        tvDesc = fv(R.id.tv_desc);
        tvHtml = fv(R.id.tv_html);

        fv(R.id.lat_address).setOnClickListener(this);
        tvAddress = fv(R.id.tv_address);

        GiftGoodsDetailsActivity activity = (GiftGoodsDetailsActivity) getActivity();
        tvApply = activity.getTvApply();
        tvApply.setOnClickListener(this);
        tvBtmScore = activity.getTvBtmScore();
        latBtm = activity.getLatBtm();
        latContent = fv(R.id.lat_content);

        banner = fv(R.id.banner);
        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.height = getResources().getDisplayMetrics().widthPixels;
        banner.setImageLoader(new BannerImageLoader(params.height, params.height));
        getParamsAddRequest();
        requestData();
        scrollView = fv(R.id.scrollView);
        setMoneyBarAlpha(params.height);

    }

    private void setMoneyBarAlpha(int maxHeight) {
        int statusBarHeight = WindowsUtils.getStatusBarHeight(getContext());
        mBarLayout = (FrameLayout) getActivity().findViewById(R.id.top_title_layout);
        mBarLayout.setBackgroundResource(R.color.transparent);
        FrameLayout bottomLay = (FrameLayout) getActivity().findViewById(R.id.bottom);
        ViewGroup.LayoutParams params = bottomLay.getLayoutParams();
        params.height = getResources().getDisplayMetrics().heightPixels
                - (int) ScreenDpiUtils.dp2px(getContext(), 56)
                - (int) ScreenDpiUtils.dp2px(getContext(), 43);
        if (getResources().getDisplayMetrics().heightPixels > getResources().getDisplayMetrics().widthPixels)
            params.height = params.height - statusBarHeight;
        final int height = maxHeight - statusBarHeight - (int) ScreenDpiUtils.dp2px(getContext(), 56);
        scrollView.setScrollListener(new CustScrollView.ScrollListener() {
            @Override
            public void onScrollChange(int l, int t, int oldl, int oldt) {
                if (t <= height) {
                    mBarLayout.setBackgroundColor(Color.argb(t * 255 / height, 64, 143, 255));
                } else {
                    mBarLayout.setBackgroundResource(R.color.blue_btn);
                }
            }
        });
    }

    //判断是否足够支付的设置
    @SuppressLint("SetTextI18n")
    private void isEnoughSetting(int num) {
        if (type == 1) {//礼品分兑换
            if (buttonTip == 0) {
                if (num * perScore > totalScore) {
                    tvApply.setEnabled(false);
                    tvApply.setText(("积分不足"));
                } else {
                    tvApply.setEnabled(true);
                    tvApply.setText("立即兑换");
                }
            }
        } else if (type == 2) {//福利券兑换
            if (buttonTip == 0) {
                if (isMoney == 0) {//不能用钱
                    if (num * perScore > totalVo) {
                        tvApply.setEnabled(false);
                        tvApply.setText("福利券不足");
                    } else {
                        tvApply.setEnabled(true);
                        tvApply.setText("立即兑换");
                    }
                } else {
                    tvApply.setEnabled(true);
                    tvApply.setText("立即兑换");
                }
            }
        }
        tvBtmScore.setText(getBtmSpanStr(num * perScore, num * perAddMoney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply:
                requestApply(0);
                break;
            case R.id.lat_address:
                if (viewUser.getVisibility() == View.VISIBLE) {//有地址
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    bundle.putInt("address_id", addressID);
                    startActivityForResult(AddressActivity.class, bundle, CodeUtils.REQUEST_ADDRESS);
                } else if (viewUser.getVisibility() == View.GONE) {//没有地址
                    Intent intent = new Intent(getActivity(), AddressAddActivity.class);
                    startActivityForResult(intent, CodeUtils.REQUEST_NO_ADDRESS);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_ADDRESS || requestCode == CodeUtils.REQUEST_NO_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                AddressEntity address = data.getParcelableExtra("address");
                if (address != null) {
                    viewUser.setVisibility(View.VISIBLE);
                    addressID = address.getAddress_id();
                    tvAddress.setText(address.getAddress_detail());
                    tvMobile.setText(address.getMobile());
                    tvUserName.setText("收货人：" + address.getConsignee());
                } else {
                    viewUser.setVisibility(View.GONE);
                    tvAddress.setText(getString(R.string.no_address));
                }
            }
        }
    }

    private void getParamsAddRequest() {
        final Bundle bundle = getArguments();
        goodsId = bundle.getInt("goods_id");
        scene = bundle.getInt("scene");
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.getGoodsDetail, RequestMethod.POST, null);
        request.add("goods_id", goodsId);
        request.add("scene", scene);
        executeNetwork(0, request);
    }

    private void requestApply(int status) {
        AbstractRequest request = buildRequest(UrlUtils.scoreExchange, RequestMethod.POST, null);
        request.add("goods_id", goodsId);
        request.add("goods_num", numberBtn.getNum());
        request.add("address_id", addressID);
        request.add("status", status);
        request.add("scene", scene);
        executeNetwork(1, request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            latBtm.setVisibility(View.VISIBLE);
            latContent.setVisibility(View.VISIBLE);
            String str = (String) result.getData();
            JSONObject strObj = JSON.parseObject(str);
            String goodsStr = strObj.getString("goods");
            ScoreHome.GiftGoods goods = JSON.parseObject(goodsStr, ScoreHome.GiftGoods.class);
            type = goods.getType();
            tvTitle.setText(goods.getTitle());
            perScore = Float.parseFloat(goods.getPrice());
            String desc = goods.getDescription();
            if (!TextUtils.isEmpty(desc)) {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(desc);
            } else {
                tvDesc.setVisibility(View.GONE);
            }
            tvHtml.setMovementMethod(ScrollingMovementMethod.getInstance());
            tvHtml.setText(Html.fromHtml(goods.getHtml()));
            String money = goods.getMoney();
            if ((TextUtils.isEmpty(money) || money.equals("0.00"))) {
                tvMoney.setVisibility(View.VISIBLE);
                tvAddMoney.setVisibility(View.GONE);
                tvMoney.setText("￥" + goods.getTo_money());
                tvMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tvMoney.setVisibility(View.GONE);
                tvAddMoney.setVisibility(View.VISIBLE);
                tvAddMoney.setText("+ " + goods.getMoney() + "元");
            }
            try {
                perAddMoney = Float.parseFloat(goods.getMoney());
            } catch (NumberFormatException e) {
                perAddMoney = 0f;
                e.printStackTrace();
            }
            String freight = goods.getFreight_price();
            if (freight != null || freight.equals("")) {
                yunfei = Float.parseFloat(freight);
                if (yunfei > 0) {
                    tvYunfei.setText("￥" + yunfei);
                } else {
                    tvYunfei.setText("免邮");
                }
            }
            //总计
            tvScore.setText(getScoreSpanStr(goods.getPrice()));
            tvBtmScore.setText(getBtmSpanStr(Float.parseFloat(goods.getPrice()), perAddMoney));
            banner.setImages(goods.getPhotos()).start();
            String address = strObj.getString("address");
            JSONObject addObj = JSON.parseObject(address);
            if (addObj != null) {
                viewUser.setVisibility(View.VISIBLE);
                addressID = addObj.getInteger("address_id");
                tvAddress.setText(addObj.getString("address_detail"));
                tvUserName.setText("收货人：" + addObj.getString("address_realname"));
                tvMobile.setText(addObj.getString("address_mobile"));
            } else {
                viewUser.setVisibility(View.GONE);
                tvAddress.setText(getString(R.string.no_address));
            }

            String score = goods.getScore();
            totalScore = Float.parseFloat(score);
            totalVo = goods.getVoucher();
//            try {
//                int maxNum = (int) Math.floor((type == 1 ? totalScore : totalVo) / perScore);
//                numberBtn.setStock(Math.min(maxNum, goods.getInventory()));
//            } catch (Exception e) {
            numberBtn.setStock(goods.getInventory());
//            }
            isMoney = goods.getIs_money();
            buttonTip = goods.getButton_tip();
            tvApply.setEnabled(buttonTip == 0);
            tvApply.setText(goods.getButton_text());
            String url = goods.getUrl();
            ((GiftGoodsDetailsActivity) getActivity()).setUrl(url);
            isEnoughSetting(1);
        } else if (what == 1) {
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            int tip = dataObj.getIntValue("tip");
            if (tip == 0) {
                JSONObject jsonObject = JSON.parseObject(result.getData().toString());
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putInt("select", type);
                bundle.putString("info", jsonObject.getString("success_text"));
                startActivity(TextInfoSuccessActivity.class, bundle);
                getActivity().setResult(Activity.RESULT_OK);
            } else if (tip == 1) {
                dm.showAlertDialog2(result.getMsg(), null, null, new DialogManager.Callback() {
                    @Override
                    public void handleLeft() {
                        requestApply(1);
                    }
                });
            }

        }
    }

    private SpannableString getScoreSpanStr(String s) {
        SpannableString ss = null;
        if (type == 1) {
            ss = new SpannableString((int) Float.parseFloat(s) + "礼品分");
        } else {
            ss = new SpannableString(NumberFormatUtils.getNewFloat(Float.parseFloat(s)) + "福利券");
        }
        ss.setSpan(new RelativeSizeSpan(0.58f), ss.length() - 3, ss.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private String getBtmSpanStr(float scoreStr, float addMoney) {
        StringBuilder sb = new StringBuilder();
        if (type == 1) {
            sb.append((int) scoreStr)
                    .append("礼品分");
        } else {
            sb.append(NumberFormatUtils.getNewFloat(scoreStr))
                    .append("福利券");
        }
        if (addMoney + yunfei > 0) {
            sb.append("+").append(NumberFormatUtils.getNewFloat(addMoney + yunfei)).append("元");
        }
        return sb.toString();
    }


}
