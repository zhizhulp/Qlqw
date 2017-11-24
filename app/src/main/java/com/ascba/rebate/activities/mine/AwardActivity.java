package com.ascba.rebate.activities.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.AwardAdapter;
import com.ascba.rebate.application.MyApplication;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.InviteAll;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.BitmapUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/11/7 15:34
 * Describe:邀请奖励
 */

public class AwardActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private AwardAdapter awardAdapter;
    private List<InviteAll.RecAward> data = new ArrayList<>();
    private int paged = 1;
    private View headView;
    private ViewFlipper flipper;
    private ImageView imHead;
    private TextView tvPeople;
    private TextView tvScore;
    private InviteAll.MemberFriends friends;
    private InviteAll.MemberCircle circle;
    private BottomSheetDialog btmDialog;
    private String ruleUrl;
    private int totalY;
    private View barLat;
    private View tvGo;

    @Override
    protected int bindLayout() {
        return R.layout.activity_award;
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_TRANSPARENT_NOTALL;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvGo = fv(R.id.tv_go);
        tvGo.setOnClickListener(this);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickTail() {
                WebViewBaseActivity.start(AwardActivity.this, "活动规则", ruleUrl);
            }
        });
        barLat = fv(R.id.lat_bar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        awardAdapter = new AwardAdapter(R.layout.award_item, data);
        awardAdapter.bindToRecyclerView(mRecyclerView);
        awardAdapter.setEmptyView(R.layout.no_award);
        awardAdapter.setHeaderFooterEmpty(true, false);
        mRecyclerView.setAdapter(awardAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                paged = 1;
                requestData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                paged++;
                requestData();
            }
        });
        mRefreshLayout.setPrimaryColorsId(R.color.bg, R.color.grey_black_tv2);
        //setMoneyBarAlpha();
        requestData();
    }

    private void setMoneyBarAlpha(final String colorStr) {
        if (!TextUtils.isEmpty(colorStr) && colorStr.length() == 6) {
            final String redStr = colorStr.substring(0, 2);
            final String greenStr = colorStr.substring(2, 4);
            final String blueStr = colorStr.substring(4, 6);
            final int maxY = getResources().getDisplayMetrics().widthPixels;
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    totalY += dy;
                    if (totalY <= maxY) {
                        Log.d(TAG, "onScrolled: alpha "+totalY * 255 / maxY);
                        barLat.setBackgroundColor(Color.argb(totalY * 255 / maxY,
                                Integer.parseInt(redStr, 16),
                                Integer.parseInt(greenStr, 16),
                                Integer.parseInt(blueStr, 16)));
                    } else {
                        barLat.setBackgroundColor(Color.parseColor("#" + colorStr));
                    }

                }
            });
        }
    }

    private void requestData() {
        AbstractRequest request = buildRequest(UrlUtils.invite, RequestMethod.GET, InviteAll.class);
        request.add("page", paged);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            InviteAll all = (InviteAll) result.getData();
            if (mRefreshLayout.getState() == RefreshState.Refreshing ||
                    mRefreshLayout.getState() == RefreshState.None) {
                if (data.size() > 0) {
                    data.clear();
                }
            }
            List<InviteAll.RecAward> memInvite = all.getMemInvite();
            if (paged != 1) {
                if (memInvite == null || memInvite.size() == 0) {
                    loadMoreOver();
                }
            }
            data.addAll(memInvite);
            setHeadData(all);
            awardAdapter.notifyDataSetChanged();
        }
    }

    private void setHeadData(InviteAll all) {
        if (all != null) {
            if (headView == null) {
                headView = getLayoutInflater().inflate(R.layout.award_head, mRecyclerView, false);
                imHead = (ImageView) headView.findViewById(R.id.im_head);
                flipper = (ViewFlipper) headView.findViewById(R.id.flipper);
                tvPeople = (TextView) headView.findViewById(R.id.tv_people);
                tvScore = (TextView) headView.findViewById(R.id.tv_score);
                ViewGroup.LayoutParams params = imHead.getLayoutParams();
                params.height = getResources().getDisplayMetrics().widthPixels * 1080 / 1121;
                awardAdapter.addHeaderView(headView);
                setMoneyBarAlpha(all.getBgcolor());
                tvGo.setBackgroundColor(Color.parseColor("#" + all.getBgcolor()));
            }
            Picasso.with(this).load(all.getInvitePic()).into(imHead);
            setTotalPeople(all.getPersonInvite());
            setTotalScore(all.getSumInvite());
            setViewFlipper(all.getAllInvite());
            friends = all.getMemberFriends();
            circle = all.getMemberCircle();
            ruleUrl = all.getInviteRules();
        }
    }

    private void setViewFlipper(List<InviteAll.Attention> allInvite) {
        flipper.removeAllViews();
        for (int i = 0; i < allInvite.size(); i++) {
            TextView tv = new TextView(this);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            tv.setLayoutParams(lp);
            tv.setText(allInvite.get(i).getDetails());
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(ContextCompat.getColor(this, R.color.black));
            tv.setTextSize(11);
            tv.setSingleLine(true);
            tv.setPadding((int) ScreenDpiUtils.dp2px(this, 10), 0,
                    (int) ScreenDpiUtils.dp2px(this, 10), 0);
            flipper.addView(tv);
        }
    }

    //设置总人数
    private void setTotalScore(int sumInvite) {
        SpannableString ss = new SpannableString(sumInvite + "礼品分");
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.grey_black_tv3))
                , ss.length() - 3, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(0.64f), ss.length() - 3, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvScore.setText(ss);
    }

    //设置累计奖励分数
    private void setTotalPeople(int personInvite) {
        SpannableString ss = new SpannableString(personInvite + "人");
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.grey_black_tv3))
                , ss.length() - 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(0.64f), ss.length() - 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPeople.setText(ss);
    }

    //立即邀请
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go:
                showPop();
                break;
            case R.id.lat_friends:
                downPicture(true);
                break;
            case R.id.lat_circle:
                downPicture(false);
                break;
        }
    }

    private void showPop() {
        if (btmDialog == null) {
            btmDialog = new BottomSheetDialog(this);
            btmDialog.setContentView(R.layout.share_type_btm);
            btmDialog.findViewById(R.id.lat_friends).setOnClickListener(this);
            btmDialog.findViewById(R.id.lat_circle).setOnClickListener(this);
            btmDialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btmDialog.dismiss();
                }
            });
        }
        btmDialog.show();
    }

    /**
     * 微信分享
     *
     * @param flag 0,分享给朋友，1分享给朋友圈
     */
    private void wechatShare(int flag, String url, String title, String content, Bitmap bitmap) {
        IWXAPI wxapi = MyApplication.getInstance().getWXAPI();
        if (!wxapi.isWXAppInstalled()) {
            showToast("请安装微信客户端");
            return;
        }
        //下载图片并且压缩到32k
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        //发送到聊天界面——WXSceneSession
        //发送到朋友圈——WXSceneTimeline
        //添加到微信收藏——WXSceneFavorite
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxapi.sendReq(req);
    }

    @Override
    protected void mHandleFailed(int what) {
        super.mHandleFailed(what);
        if (what == 0) {
            finish();
        }
    }

    private void downPicture(final boolean image) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap logo = null;
                try {
                    URL url = new URL(image ? friends.getCourtesy_img() : circle.getCourtesy_img());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        logo = BitmapFactory.decodeStream(inputStream);
                    } else {
                        logo = ((BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.mipmap.share_icon)).getBitmap();
                    }

                } catch (IOException e) {
                    logo = ((BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.mipmap.share_icon)).getBitmap();
                    e.printStackTrace();
                }
                final Bitmap finalLogo = logo;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapUtils.compressBitmap(finalLogo, 32);
                        btmDialog.dismiss();
                        wechatShare(image ? 0 : 1, image ? friends.getCourtesy_url() : circle.getCourtesy_url(),
                                image ? friends.getTitle() : circle.getTitle(),
                                image ? friends.getSubtitle() : circle.getSubtitle(), bitmap);
                    }
                });
            }
        }).start();

    }
}
