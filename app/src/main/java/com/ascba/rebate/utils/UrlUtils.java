package com.ascba.rebate.utils;

import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.appconfig.AppConfig;

/**
 * Created by lenovo on 2017/8/21.
 * 接口地址
 */

public class UrlUtils {



    static {
        if (BuildConfig.DEBUG)
            baseWebsite = AppConfig.getInstance().getString("debug_url", BuildConfig.BASE_URL);
        else
            baseWebsite = BuildConfig.BASE_URL;
    }

    public static String baseWebsite;
    public static String getLoginVerify = baseWebsite + "getLoginVerify";
    public static String getRegisterVerify = baseWebsite + "getRegisterVerify";
    public static String checkRegisterVerify = baseWebsite + "checkRegisterVerify";
    public static String login = baseWebsite + "login";
    public static String my = baseWebsite + "my";
    public static String set = baseWebsite + "user/set";
    public static String register = baseWebsite + "register";
    public static String registerProtocol = baseWebsite + "agreement/register";
    public static String findCompanyInfo = baseWebsite + "company/findCompanyInfo";
    public static String add = baseWebsite + "company/add";
    public static String update = baseWebsite + "company/update";
    public static String getCompanyInfo = baseWebsite + "company/getCompanyInfo";
    public static String nameSeek = baseWebsite + "user/nameSeek";
    public static String detect = "https://api.faceid.com/faceid/v1/detect";
    public static String ocridcard = "https://api.faceid.com/faceid/v1/ocridcard";
    public static String verify = "https://api.megvii.com/faceid/v2/verify";
    public static String nameAuth = baseWebsite + "user/nameAuth";
    public static String index = baseWebsite + "balance/index";
    public static String payment = baseWebsite + "recharge/payment";
    public static String checkPayPassword = baseWebsite + "checkPayPassword";
    public static String getBankList = baseWebsite + "bank/getBankList";
    public static String delbanks = baseWebsite + "bank/delbanks";
    public static String verifyBankCard = baseWebsite + "bank/verifyBankCard";
    public static String indexCash = baseWebsite + "cash/index";
    public static String indexRecharge = baseWebsite + "recharge/index";
    public static String tillsMoney = baseWebsite + "cash/tillsMoney";
    public static String cashresult = baseWebsite + "cash/cashresult";
    public static String about = baseWebsite + "user/about";
    public static String payPassword = baseWebsite + "set/payPassword";
    public static String verifyMobile = baseWebsite + "VerifyMobile";
    public static String getVerify = baseWebsite + "getVerify";
    public static String billList = baseWebsite + "bill/list";
    public static String messageClass = baseWebsite + "messageClass";
    public static String messageList = baseWebsite + "messageList";
    public static String balanceRechargeList = baseWebsite + "balance/balanceRechargeList";
    public static String balanceTillList = baseWebsite + "balance/balanceTillList";
    public static String addressGetList = baseWebsite + "memberAddress/getList";
    public static String addressAdd = baseWebsite + "memberAddress/add";
    public static String addressEdit = baseWebsite + "memberAddress/edit";
    public static String addressSetDefault = baseWebsite + "memberAddress/setDefault";
    public static String addressDel = baseWebsite + "memberAddress/del";
    public static String scoreGoodsList = baseWebsite + "scoreGoodsList";
    public static String getGoodsDetail = baseWebsite + "getGoodsDetail";
    public static String scoreExchange = baseWebsite + "scoreExchange";
    public static String openProtocol = baseWebsite + "openProtocol";
    public static String protocol = baseWebsite + "protocol";
    public static String billscore = baseWebsite + "billscore/list";
    public static String exchangeLog = baseWebsite + "exchangeLog";
    public static String indexHome = baseWebsite + "index";
    public static String moreNav = baseWebsite + "moreNav";
    public static String navClick = baseWebsite + "navClick";
    public static String setNavMember = baseWebsite + "setNavMember";
    public static String navSearch = baseWebsite + "navSearch";
    public static String purchaseIndex = baseWebsite + "Purchase/index";
    public static String purchasePay = baseWebsite + "Purchase/pay";
    public static String purchasePayment = baseWebsite + "Purchase/payment";
    public static String PurchaseGiveCreate = baseWebsite + "Purchase/giveCreate";
    public static String PurchaseGiveSave = baseWebsite + "Purchase/giveSave";
    public static String receivable_url = baseWebsite + "qrcode/receivable_url";
    public static String checkSeller = baseWebsite + "Transaction/checkSeller";
    public static String submit = baseWebsite + "Transaction/submit";
    public static String affirm = baseWebsite + "Transaction/affirm";
    public static String cancel = baseWebsite + "Transaction/cancel";
    public static String list = baseWebsite + "TransactionLog/list";
    public static String info = baseWebsite + "TransactionLog/info";
    public static String CapitalProfitList = baseWebsite + "CapitalProfitList";
    public static String CapitalProfitdetail = baseWebsite + "CapitalProfitdetail";
    public static String Wechatlogin = baseWebsite + "Wechatlogin";
    public static String getWechatAppInfo = baseWebsite + "getWechatAppInfo";
    public static String WechatLoginBind = baseWebsite + "WechatLoginBind";
    public static String WechatLoginGetVerify = baseWebsite + "WechatLoginGetVerify";
    public static String startPic = baseWebsite + "startPic";
    public static String billvoucher = baseWebsite + "billvoucher/list";
    public static String invite = baseWebsite + "user/invite";
    public static String getUpgrade = baseWebsite + "app/getUpgrade";
    public static String invoiceCreate = baseWebsite + "invoice/create";
    public static String invoiceCheck = baseWebsite + "invoice/check";
    public static String invoiceAdd = baseWebsite + "invoice/add";
    public static String invoiceList = baseWebsite + "invoice/list";
    public static String sellerSetting = baseWebsite + "seller/setting";
    public static String mctPay = baseWebsite + "seller/pay";
    public static String sellerPayment = baseWebsite + "seller/payment";
    public static String interests = baseWebsite + "seller/interests";
    public static String perfect = baseWebsite + "seller/perfect";

}
