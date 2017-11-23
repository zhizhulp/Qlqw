package com.ascba.rebate.activities.score_shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.bill.VoucherBillActivity;
import com.ascba.rebate.adapter.GiftShopAdapter;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ScoreHome;
import com.ascba.rebate.fragments.GiftExchangeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李平 on 2017/9/20 11:35
 * Describe: 礼品商城
 */

public class GiftShopActivity extends BaseDefaultNetActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    public TextView tvScore;
    public TextView tvLevel;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"礼品兑换", "福利券兑换"};
    private List<Fragment> fragmentList;
    public ImageView imStr;
    public TextView tvDetails;
    private ScoreHome scoreHome1;//积分
    private ScoreHome scoreHome2;//福利券
    private int position;

    @Override
    protected int bindLayout() {
        return R.layout.activity_gift_shop;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        tvScore = fv(R.id.tv_score);
        tvLevel = fv(R.id.tv_level);

        imStr = fv(R.id.im_str);
        tvDetails = fv(R.id.tv_details);

        tabLayout = fv(R.id.tab_layout);
        viewPager = fv(R.id.viewPager);
        addTabSetListener();
        addFragments();
        addViewPagerAdapter();
        tabLayout.setupWithViewPager(viewPager);

        fv(R.id.tv_exchange_log).setOnClickListener(this);
        fv(R.id.tv_details).setOnClickListener(this);
    }

    private void addViewPagerAdapter() {
        GiftShopAdapter adapter = new GiftShopAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(adapter);
    }

    private void addFragments() {
        fragmentList = new ArrayList<>();
        GiftExchangeFragment scoreFragment = GiftExchangeFragment.getInstance(1);
        scoreFragment.setListener(new GiftExchangeFragment.Listener() {
            @Override
            public void backData(ScoreHome scoreHome) {
                scoreHome1 = scoreHome;
                setHead(scoreHome, 0);
            }
        });
        fragmentList.add(scoreFragment);
        GiftExchangeFragment ticketFragment = GiftExchangeFragment.getInstance(2);
        ticketFragment.setListener(new GiftExchangeFragment.Listener() {
            @Override
            public void backData(ScoreHome scoreHome) {
                scoreHome2 = scoreHome;
                setHead(scoreHome, 1);
            }
        });
        fragmentList.add(ticketFragment);
    }

    private void addTabSetListener() {
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        this.position = position;
        if (position == 0) {
            setHead(scoreHome1, position);
        } else if (position == 1) {
            setHead(scoreHome2, position);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exchange_log:
                if (position == 0) {//礼品兑换记录
                    GiftExchangeLogActivity.jumpIntent(this, 1);
                } else if (position == 1) {//福利券兑换记录
                    GiftExchangeLogActivity.jumpIntent(this, 2);
                }
                break;
            case R.id.tv_details:
                if (position == 0) {//礼品分明细
                    Bundle b = new Bundle();
                    b.putInt("mine_type", 4);
                    startActivity(ScoreBillActivity.class, b);
                } else if (position == 1) {//福利券明细
                    Bundle b = new Bundle();
                    b.putInt("mine_type", 1);
                    startActivity(VoucherBillActivity.class, b);
                }

                break;
        }
    }

    private void setHead(ScoreHome scoreHome, int initPosition) {
        if (scoreHome != null && position == initPosition) {
            int cattype = scoreHome.getCattype();//1表示积分商品2表示礼品券商品
            if (cattype == 1) {
                imStr.setImageResource(R.mipmap.gift_score_str);
                tvDetails.setText("礼品兑换明细");
                tvScore.setText(scoreHome.getScore());
                tvLevel.setText(scoreHome.getMember_level());
            } else if (cattype == 2) {
                imStr.setImageResource(R.mipmap.gift_ticket_str);
                tvDetails.setText("福利兑换明细");
                tvScore.setText(scoreHome.getScore());
                tvLevel.setText(scoreHome.getMember_level());
            }
        }
    }
}
