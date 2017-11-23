package com.ascba.rebate.activities.personal_identification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.IDCardMsg;
import com.ascba.rebate.bean.LoginNextEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.manager.DialogManager;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.net.CallServer;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.ByteArrayBinary;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

/**
 * 个人认证第三步
 */
public class PICommitActivity extends BaseDefaultNetActivity {
    private byte[] cardData;
    private byte[] faceData;
    private ImageView imCard;
    private TextView tvName;
    private TextView tvNum;
    private IDCardMsg idCardMsg;
    private Button btnApply;

    @Override
    protected int bindLayout() {
        return R.layout.activity_per_iden_step3;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        initView();
        getParams();
        requestCardData();
    }

    private void initView() {
        imCard = fv(R.id.im_id_card);
        tvName = fv(R.id.tv_name);
        tvNum = fv(R.id.tv_number);
        btnApply = fv(R.id.btn_commit);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                startActivity(PIStartActivity.class, null);
                finish();
            }
        });
    }

    private void requestCardData() {
        StringRequest sr = new StringRequest(UrlUtils.ocridcard, RequestMethod.POST);
        sr.add("api_key", CodeUtils.AUTH_API_KEI);
        sr.add("api_secret", CodeUtils.AUTH_API_SECRET);
        sr.add("image", new ByteArrayBinary(cardData, "qlqw19923746.png"));
        final ProgressDialog dialog = new ProgressDialog(PICommitActivity.this, R.style.dialog);
        dialog.setMessage("请稍后");
        CallServer.getInstance().addRequest(0, sr, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                dialog.show();
                mStatusView.empty();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if (response.responseCode() == 200) {
                    String strResponse = response.get();
                    idCardMsg = JSON.parseObject(strResponse, IDCardMsg.class);
                    imCard.setImageBitmap(BitmapFactory.decodeByteArray(cardData, 0, cardData.length));
                    tvName.setText(idCardMsg.getName());
                    tvNum.setText(idCardMsg.getId_card_number());
                    mStatusView.content();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                showToast(getString(R.string.request_failed));
            }

            @Override
            public void onFinish(int what) {
                dialog.dismiss();
            }
        });
    }

    private void getParams() {
        Intent intent = getIntent();
        cardData = intent.getByteArrayExtra("idcardImg");
        faceData = intent.getByteArrayExtra("face_img");
    }

    //提交认证
    public void goCommit(View view) {
        StringRequest sr = new StringRequest(UrlUtils.verify, RequestMethod.POST);
        sr.add("api_key", CodeUtils.AUTH_API_KEI);
        sr.add("api_secret", CodeUtils.AUTH_API_SECRET);
        sr.add("comparison_type", "1");
        sr.add("face_image_type", "facetoken");
        sr.add("idcard_name", idCardMsg.getName());
        sr.add("idcard_number", idCardMsg.getId_card_number());
        sr.add("face_token", AppConfig.getInstance().getString("face_token", null));
        final ProgressDialog dialog = new ProgressDialog(PICommitActivity.this, R.style.dialog);
        dialog.setMessage("请稍后");
        CallServer.getInstance().addRequest(2, sr, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                dialog.show();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.d(TAG, "onSucceed: " + response.get());
                try {
                    JSONObject jObj = new JSONObject(response.get());
                    if (response.responseCode() == 200) {
                        JSONObject pObj = jObj.optJSONObject("result_faceid");
                        double cf = pObj.optDouble("confidence");
                        JSONObject cObj = pObj.optJSONObject("thresholds");
                        double e3 = cObj.optDouble("1e-3");
                        double e4 = cObj.optDouble("1e-4");
                        double e5 = cObj.optDouble("1e-5");
                        double e6 = cObj.optDouble("1e-6");

                        String s = sortValue(cf, e3, e4, e5, e6);
                        if (s != null) {
                            if (s.equals("1e-3")) {//不匹配
                                btnApply.setEnabled(false);
                                dm.showAlertDialog2("检测到认证信息不匹配，是否开始重新采集？", null, null, new DialogManager.Callback() {
                                    @Override
                                    public void handleLeft() {
                                        startActivity(PIStartActivity.class, null);
                                        finish();
                                    }
                                });
                            } else if (s.equals("1e-4") || s.equals("1e-5") || s.equals("1e-6")) {//匹配
                                btnApply.setEnabled(true);
                                AbstractRequest request = buildRequest(UrlUtils.nameAuth, RequestMethod.POST, LoginNextEntity.UserInfoBean.class);
                                request.add("cardid", idCardMsg.getId_card_number());
                                request.add("sex", idCardMsg.getGender().equals("女") ? 0 : 1);
                                request.add("age", Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(idCardMsg.getBirthday().getYear()));
                                request.add("realname", idCardMsg.getName());
                                request.add("location", idCardMsg.getAddress());
                                request.add("realityAvatar", new ByteArrayBinary(faceData, "qlqw201709071516.png"));
                                request.add("cardAvatar", new ByteArrayBinary(cardData, "qlqw201709071514.png"));
                                executeNetwork(1, "请稍后", request);
                            }
                        } else {
                            btnApply.setEnabled(false);
                            showToast("没有匹配到,请重新认证");
                        }

                    } else if (response.responseCode() == 400) {
                        showToast(jObj.optString("error_message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {
                dialog.dismiss();
            }
        });
    }

    private String sortValue(double cf, double e3, double e4, double e5, double e6) {
        double[] ff = new double[]{Math.abs(cf - e3), Math.abs(cf - e4), Math.abs(cf - e5), Math.abs(cf - e6)};
        Arrays.sort(ff);
        for (double aFf : ff) {
            if (aFf == Math.abs(cf - e3)) {
                return "1e-3";
            } else if (aFf == Math.abs(cf - e4)) {
                return "1e-4";
            } else if (aFf == Math.abs(cf - e5)) {
                return "1e-5";
            } else if (aFf == Math.abs(cf - e6)) {
                return "1e-6";
            }
        }
        return null;
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 1) {
            showToast(result.getMsg());
            LoginNextEntity.UserInfoBean userInfo = (LoginNextEntity.UserInfoBean) result.getData();
            AppConfig appConfig = AppConfig.getInstance();
            appConfig.putString("avatar", userInfo.getAvatar());
            appConfig.putString("mobile", userInfo.getMobile());
            appConfig.putString("nickname", userInfo.getNickname());
            appConfig.putString("sex", userInfo.getSex());
            appConfig.putInt("age", userInfo.getAge());
            appConfig.putString("location", userInfo.getLocation());
            appConfig.putInt("card_status", userInfo.getCard_status());
            appConfig.putInt("company_status", userInfo.getCompany_status());
            appConfig.putString("group_name", userInfo.getGroup_name());
            appConfig.putInt("is_level_pwd", userInfo.getIs_level_pwd());
            appConfig.putString("cardid", userInfo.getCardid());
            appConfig.putString("realname", userInfo.getRealname());
            startActivity(PISuccessActivity.class, null);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(PIStartActivity.class, null);
        finish();
    }

    /**
     *  重新扫描
     */
    public void reSweep(View view) {
        startActivity(IDCardScanActivity.class,null);
        finish();
    }
}
