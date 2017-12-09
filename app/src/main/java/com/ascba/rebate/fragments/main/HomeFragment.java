package com.ascba.rebate.fragments.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ascba.rebate.activities.merchant.MctApplyStartActivity;
import com.ascba.rebate.activities.merchant.MctEnterActivity;
import com.ascba.rebate.activities.merchant.MctPayActivity;
import com.ascba.rebate.activities.seller.SellerActivity;
import com.ascba.rebate.manager.DialogManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ascba.rebate.R;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.home.ModulesAllActivity;
import com.ascba.rebate.activities.home.PlayVideoActivity;
import com.ascba.rebate.activities.trade.ReceiveCodeActivity;
import com.ascba.rebate.activities.trade.SweepActivity;
import com.ascba.rebate.adapter.HomeGridAdapter;
import com.ascba.rebate.adapter.HomeListAdapter;
import com.ascba.rebate.base.fragment.BaseDefaultNetFragment;
import com.ascba.rebate.bean.HomeBean;
import com.ascba.rebate.bean.ModuleEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.BannerImageLoader;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.ModulesUtils;
import com.ascba.rebate.utils.ScreenDpiUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.NestGridView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * 首页
 */
public class HomeFragment extends BaseDefaultNetFragment implements View.OnClickListener {
    private List<HomeBean.BillBean> records = new ArrayList<>();//支付助手
    private List<ModuleEntity> navs = new ArrayList<>();//导航数据
    private ArrayList<HomeBean.VideoBean> imgs = new ArrayList<>();//图片轮播
    private HomeListAdapter adapter;
    private HomeGridAdapter gridAdapter;
    private Banner banner;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews() {
        super.initViews();
        final View imSweep = fv(R.id.lat_sweep);
        final View imReceive = fv(R.id.lat_receive);
        final ImageView imSweepTitle = fv(R.id.im_sweep_title);
        final ImageView imReceiveTitle = fv(R.id.im_receive_title);
        imSweep.setOnClickListener(this);
        imReceive.setOnClickListener(this);
        imSweepTitle.setOnClickListener(this);
        imReceiveTitle.setOnClickListener(this);

        ((AppBarLayout) fv(R.id.home_appbar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int absOff = Math.abs(verticalOffset);
                float range = appBarLayout.getTotalScrollRange();//滑动最大值
                float rate = absOff / range;
                imSweepTitle.setAlpha(rate);
                imReceiveTitle.setAlpha(rate);
            }
        });

        initRecyclerView();
        requestNetwork();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeListAdapter(R.layout.home_item, records);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeBean.BillBean billBean = records.get(position);
                String billType = billBean.getBillType();
                if (billType.equals("billMoney")) {
                    startActivity(BillActivity.class, null);
                } else if (billType.equals("billScore")) {
                    startActivity(ScoreBillActivity.class, null);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2017/11/9  需要详情接口
            }
        });
        View footView = new View(getContext());
        footView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg));
        footView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ScreenDpiUtils.dp2px(getContext(), 9)));
        adapter.addFooterView(footView);
        adapter.setHeaderAndEmpty(true);
        adapter.setEmptyView(R.layout.main_empty, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setPrimaryColorsId(R.color.main_bg, R.color.grey_black_tv2);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                requestNetwork();
            }
        });
    }

    private void addBanner() {
        banner = new Banner(getContext());
        banner.setImageLoader(new BannerImageLoader());
        banner.setDelayTime(3500);
        banner.setIndicatorGravity(Gravity.BOTTOM);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , getContext().getResources().getDisplayMetrics().widthPixels * 10 / 24);
        lp2.setMargins(0, (int) ScreenDpiUtils.dp2px(getContext(), 9), 0, 0);
        banner.setLayoutParams(lp2);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                PlayVideoActivity.newIndexIntent(getContext(), imgs.get(position));
            }
        });
        adapter.addHeaderView(banner);
    }

    private void addNav() {
        NestGridView dv = new NestGridView(getContext());
        dv.setPadding(0, (int) ScreenDpiUtils.dp2px(getContext(), 10), 0, (int) ScreenDpiUtils.dp2px(getContext(), 10));
        dv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        dv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dv.setNumColumns(4);
        dv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        dv.setVerticalSpacing((int) ScreenDpiUtils.dp2px(getContext(), 10));
        gridAdapter = new HomeGridAdapter(getContext(), navs);
        dv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModuleEntity nav = navs.get(position);
                if (nav.getIs_disappear() == 1) {
                    if (nav.getIs_read() != 0) {
                        nav.setIs_read(0);
                        gridAdapter.notifyDataSetChanged();
                    }
                }
                if ("More".equals(nav.getNav_label())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ModulesUtils.HOME, JSON.toJSONString(navs));
                    startActivityForResult(ModulesAllActivity.class, bundle, CodeUtils.REQUEST_EDIT);
                } else {
                    requestClickPoint(nav.getNav_id());
                    ModulesUtils.itemGo(getActivity(), nav);
                }
            }
        });
        dv.setAdapter(gridAdapter);
        adapter.addHeaderView(dv);
    }

    private void requestClickPoint(int nav_id) {
        AbstractRequest request = buildRequest(UrlUtils.navClick, RequestMethod.POST, null);
        request.add("nav_id", nav_id);
        executeNetwork(1, request);
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.indexHome, RequestMethod.GET, HomeBean.class);
        executeNetwork(0, request);
    }
    private void requestSellerStatus() {
        AbstractRequest request = buildRequest(UrlUtils.getSellerStatus, RequestMethod.GET, null);
        executeNetwork(2, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            HomeBean homeBean = (HomeBean) result.getData();
            addData(homeBean.getBill());
            addNavData(homeBean.getNavList());
            addVideoData(homeBean.getVideoList());
            ModulesUtils.navActivity = homeBean.getNav_activity();
        } else if(what==2){
            String data = (String) result.getData();
            JSONObject dataObj = JSON.parseObject(data);
            int memberStatus = dataObj.getIntValue("member_status");
            final String statusText = dataObj.getString("member_status_text");
            //0正常1普通用户2资料审核中3商家过期4资料不全，第一次（跳转到H5）5资料不全第N次（跳转到资料提交）
            if (memberStatus == 0) {
                startActivity(ReceiveCodeActivity.class, null);
            } else if (memberStatus == 1) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(MctPayActivity.class, null);
                    }
                });
            } else if (memberStatus == 2) {
                showToast(statusText);
            } else if (memberStatus == 3) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(MctPayActivity.class, null);
                    }
                });
            } else if (memberStatus == 4) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        startActivity(MctApplyStartActivity.class, null);
                    }
                });
            } else if (memberStatus == 5) {
                dm.showAlertDialog2(statusText, "取消", "确认", new DialogManager.Callback() {
                    @Override
                    public void handleRight() {
                        MctEnterActivity.start(getActivity(),1);
                    }
                });
            }
        }
    }

    @Override
    protected void mHandle404(int what, Result result) {
        if (what != 1) {
            super.mHandle404(what, result);
        }
    }

    private void addVideoData(List<HomeBean.VideoBean> videos) {
        imgs.clear();
        imgs.addAll(videos);
        if (videos != null && videos.size() > 0) {
            if (banner == null) {
                addBanner();
            }
            banner.setImages(imgs).start();
        }

    }

    private void addNavData(List<ModuleEntity> navBeans) {
        navs.clear();
        navs.addAll(navBeans);
        if (navBeans.size() > 0) {
            if (gridAdapter == null) {
                addNav();
            }
        }
        if (gridAdapter != null) {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private void addData(List<HomeBean.BillBean> bills) {
        records.clear();
        records.addAll(bills);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lat_sweep || id == R.id.im_sweep_title) {
            startActivity(SweepActivity.class, null);
        } else if (id == R.id.lat_receive || id == R.id.im_receive_title) {
            requestSellerStatus();
            //startActivity(ReceiveCodeActivity.class, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CodeUtils.REQUEST_EDIT) {
            requestNetwork();
        } else if (resultCode == RESULT_OK && requestCode == 0) {
            requestNetwork();
        }
    }
}
