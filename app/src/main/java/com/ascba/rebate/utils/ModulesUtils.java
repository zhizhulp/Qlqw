package com.ascba.rebate.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.agent.AgentActivity;
import com.ascba.rebate.activities.agent.CityAgentActivity;
import com.ascba.rebate.activities.arround.ArroundListActivity;
import com.ascba.rebate.activities.benefits.SellBenefitActivity;
import com.ascba.rebate.activities.bill.BillActivity;
import com.ascba.rebate.activities.bill.ScoreBillActivity;
import com.ascba.rebate.activities.cash_get.CashGetActivity;
import com.ascba.rebate.activities.personal_identification.PIStartActivity;
import com.ascba.rebate.activities.recharge.RechargeActivity;
import com.ascba.rebate.activities.score_shop.GiftShopActivity;
import com.ascba.rebate.activities.seller.SellerActivity;
import com.ascba.rebate.activities.seller.SellerGiveCreateActivity;
import com.ascba.rebate.activities.seller.SellerPurchaseActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.ModuleEntity;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.manager.ToastManager;

import java.util.List;

/**
 * Created by Jero on 2017/10/13 0013.
 */

public class ModulesUtils {
    public static final String HOME = "HOME";
    public static final String LIST = "LIST";
    public static int navActivity = 0;

    public static boolean itemGo(final Activity activity, ModuleEntity moduleEntity) {
        switch (moduleEntity.getNav_label()) {
            case "GiftsStoredValue":// 礼品储值
                goActivity(activity, SellerPurchaseActivity.class, null);
                break;
            case "PresentsGiving":// 礼品赠送
                goActivity(activity, SellerGiveCreateActivity.class, null);
                break;
            case "GiftExchange":// 礼品兑换
                goActivity(activity, GiftShopActivity.class, null);
                break;
            case "Recharge":// 充值
                goActivity(activity, RechargeActivity.class, null);
                break;
            case "Withdraw":// 提现
                if (AppConfig.getInstance().getInt("card_status", 0) == 0) {
                    new DialogManager(activity).showAlertDialog2(activity.getString(R.string.no_pi), null, null, new DialogManager.Callback() {
                        @Override
                        public void handleLeft() {
                            goActivity(activity, PIStartActivity.class, null);
                        }
                    });
                } else {
                    goActivity(activity, CashGetActivity.class, null);
                }
                break;
            case "Store":// 商城
                ToastManager.show("敬请期待");
                break;
            case "CashBills":// 现金账单
                goActivity(activity, BillActivity.class, null);
                break;
            case "IntegralBills":// 积分账单
                goActivity(activity, ScoreBillActivity.class, null);
                break;
            case "TelephoneRecharging":// 话费充值
                ToastManager.show("敬请期待");
                break;
            case "ConsignmentEarnings":// 寄卖收益
                goActivity(activity, SellBenefitActivity.class, null);
                break;
            case "MerchantServices":// 商家服务
                goActivity(activity, SellerActivity.class, null);
                break;
            case "AroundMerchants":// 周边商家
                goActivity(activity, ArroundListActivity.class, null);
                break;
            case "RegionalPartner":// 区域合伙人
                goActivity(activity, CityAgentActivity.class, null);
                break;
            case "AgentLeague":// 代理加盟
                goActivity(activity, AgentActivity.class, null);
                break;
            case "More":// 更多
                return false;
            case "HttpAgreement":// 更多
                goWeb(activity, moduleEntity);
                return false;
            default:
                ToastManager.show("敬请期待");
                break;
        }
        return true;
    }

    private static void goActivity(Activity activity, Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    private static void goWeb(Activity activity, ModuleEntity moduleEntity) {
        WebViewBaseActivity.start(activity, moduleEntity.getNav_name(), moduleEntity.getHttpurl());
    }

    public static void selectItem(List<ModuleEntity> homeList, List<ModuleEntity> moduleList) {
        for (ModuleEntity a : moduleList) {
            if (a.getItemType() == 1)
                for (ModuleEntity b : homeList) {
                    if (b.getNav_id() == a.getNav_id()) {
                        a.setSelect(1);
                        break;
                    }
                }
        }
    }

    public static void delItem(ModuleEntity homeitem, List<ModuleEntity> moduleList) {
        for (ModuleEntity a : moduleList) {
            if (a.getItemType() == 1 && a.isSelect() == 1) {
                if (homeitem.getNav_id() == a.getNav_id()) {
                    a.setSelect(0);
                }
            }
        }
    }

    public static void addItem(ModuleEntity homeitem, List<ModuleEntity> moduleList) {
        for (ModuleEntity a : moduleList) {
            if (a.getItemType() == 1 && a.isSelect() == 0) {
                if (homeitem.getNav_id() == a.getNav_id()) {
                    a.setSelect(1);
                }
            }
        }
    }

    public static boolean equalList(List list1, List list2) {
        if (list1.size() != list2.size())
            return false;
        int i = 0;
        for (Object o : list1) {
            if (!o.equals(list2.get(i)))
                return false;
            i++;
        }
        return true;
    }
}
