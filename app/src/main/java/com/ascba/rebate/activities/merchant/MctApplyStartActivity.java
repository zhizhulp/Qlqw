package com.ascba.rebate.activities.merchant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ascba.rebate.R;
import com.ascba.rebate.activities.company_identification.ComMsgActivity;
import com.ascba.rebate.activities.company_identification.InPutCNActivity;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.ComMsg;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.yanzhenjie.nohttp.RequestMethod;

/**
 * Created by 李平 on 2017/12/6 16:12
 * Describe: 商家入驻需知
 */

public class MctApplyStartActivity extends BaseDefaultNetActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mct_apply_start);

        findViewById(R.id.btn_apply).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int companyStatus = AppConfig.getInstance().getInt("company_status", 0);
        if (companyStatus == 3) {//公司认证已通过
            MctEnterActivity.start(this,0);
        } else if (companyStatus == 0) {//未通过
            startActivity(InPutCNActivity.class, null);
        } else if (companyStatus == 1) {//审核中
            showToast("公司资质审核中，请审核通过后再试。");
        } else if (companyStatus == 2) {//资料有误
            findCompanyInfo();
        }
    }

    private void findCompanyInfo(){
        AbstractRequest request = buildRequest(UrlUtils.getCompanyInfo, RequestMethod.GET, ComMsg.class);
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if(what==0){
            ComMsg comMsg = (ComMsg) result.getData();
            Bundle b = new Bundle();
            b.putParcelable("company_msg", comMsg);
            AppConfig.getInstance().putInt("company_status", comMsg.getStatus());
            startActivity(ComMsgActivity.class, b);
        }
    }
}
