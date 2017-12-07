package com.ascba.rebate.activities.user_msg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.company_identification.CISuccessActivity;
import com.ascba.rebate.activities.company_identification.ComMsgActivity;
import com.ascba.rebate.activities.company_identification.InPutCNActivity;
import com.ascba.rebate.activities.personal_identification.PIStartActivity;
import com.ascba.rebate.activities.personal_identification.PISuccessActivity;
import com.ascba.rebate.activities.user_data.UserDataActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ComMsg;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.picasso.MaskTransformation;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * 个人信息
 */
public class UserMsgActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    private ImageView imHead;
    private TextView tvNameAuthon;
    private TextView tvComAuthon;
    private boolean updateHead;
    private boolean updateName;
    private int cardStatus;
    private int companyStatus;

    @Override
    protected int bindLayout() {
        return R.layout.activity_user_msg;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        fv(R.id.lat_user_head).setOnClickListener(this);
        fv(R.id.lat_name_authon).setOnClickListener(this);
        fv(R.id.lat_company_authon).setOnClickListener(this);
        fv(R.id.lat_rec).setOnClickListener(this);
        fv(R.id.lat_award).setOnClickListener(this);
        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                backResults();
            }
        });

        imHead = fv(R.id.im_user_head);
        tvNameAuthon = fv(R.id.tv_name_authon);
        tvComAuthon = fv(R.id.tv_company_authen);

        AppConfig appConfig = AppConfig.getInstance();
        setHead();
        cardStatus = appConfig.getInt("card_status", 0);
        tvNameAuthon.setText(cardStatus == 0 ? "待认证" : "已认证");
        setCompanyText(appConfig);
    }

    private void setCompanyText(AppConfig config) {
        companyStatus = config.getInt("company_status", 0);
        if (companyStatus == 0) {
            tvComAuthon.setText("未申请");
        } else if (companyStatus == 1) {
            tvComAuthon.setText("等待审核中");
        } else if (companyStatus == 2) {
            tvComAuthon.setText("资料有误");
        } else if (companyStatus == 3) {
            tvComAuthon.setText("审核已通过");
        }
    }

    @Override
    public void onClick(View v) {
        int cardStatus = AppConfig.getInstance().getInt("card_status", 0);//用户是否进行实名认证.(0：待认证，1：已认证)
        switch (v.getId()) {
            case R.id.lat_user_head:
                startActivityForResult(UserDataActivity.class, null, CodeUtils.REQUEST_USER_HEAD);
                break;
            case R.id.lat_name_authon:
                if (cardStatus == 0) {
                    startActivity(PIStartActivity.class, null);
                } else {
                    startActivity(PISuccessActivity.class, null);
                }
                break;
            case R.id.lat_company_authon:

                int companyStatus = AppConfig.getInstance().getInt("company_status", 0);//用户是否进行企业认证.(0：待认证，1：已认证)
                if (cardStatus == 0) {
                    showToast("个人认证未通过，请先认证");
                } else {//已实名认证
                    if (companyStatus == 0) {//企业未认证
                        startActivityForResult(InPutCNActivity.class, null, CodeUtils.REQUEST_COM_AUTHON);
                    } else if (companyStatus == 1 || companyStatus == 2 || companyStatus == 3) {//等待审核/资料有误/审核已通过
                        AbstractRequest request = buildRequest(UrlUtils.getCompanyInfo, RequestMethod.GET, ComMsg.class);
                        executeNetwork(0, "请稍后", request);
                    }
                }
                break;
            case R.id.lat_rec:
                break;
            case R.id.lat_award:
                break;
        }
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            ComMsg comMsg = (ComMsg) result.getData();
            Bundle b = new Bundle();
            b.putParcelable("company_msg", comMsg);
            int status = comMsg.getStatus();
            AppConfig.getInstance().putInt("company_status", status);
            if (status == 1 || status == 2) {
                startActivityForResult(ComMsgActivity.class, b, CodeUtils.REQUEST_COM_ERROR);
            } else if (status == 3) {
                startActivity(CISuccessActivity.class,b);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.REQUEST_USER_HEAD && resultCode == Activity.RESULT_OK) {//修改用户头像
            boolean updateHead = data.getBooleanExtra("update_head", false);
            boolean updateName = data.getBooleanExtra("update_name", false);
            this.updateHead = updateHead;
            this.updateName = updateName;
            if (updateHead) {
                setHead();
            }
        } else if (requestCode == CodeUtils.REQUEST_COM_AUTHON && resultCode == Activity.RESULT_OK) {
            tvComAuthon.setText("等待审核中");
        } else if (requestCode == CodeUtils.REQUEST_COM_ERROR && resultCode == Activity.RESULT_OK) {
            setCompanyText(AppConfig.getInstance());
        }
    }

    /**
     * 更新用户头像
     */
    private void setHead() {
        Picasso.with(this).load(AppConfig.getInstance().getString("avatar", null))
                .transform(new MaskTransformation(this, R.mipmap.head_loading))
                .placeholder(R.mipmap.head_loading).into(imHead);
    }

    /**
     * 返回更新数据
     */
    private void backResults() {
        Intent intent = getIntent();
        intent.putExtra("update_head", updateHead);
        intent.putExtra("update_name", updateName);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backResults();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig instance = AppConfig.getInstance();
        int cardStatus = instance.getInt("card_status", 0);
        int companyStatus = instance.getInt("company_status", 0);
        if (this.cardStatus != cardStatus) {
            tvNameAuthon.setText(cardStatus == 0 ? "待认证" : "已认证");
        }

        if (this.companyStatus != companyStatus) {
            setCompanyText(instance);
        }
    }
}
