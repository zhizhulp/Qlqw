package com.ascba.rebate.activities.company_identification;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.base.activity.WebViewBaseActivity;
import com.ascba.rebate.bean.ComMsg;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.FileUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconDialog;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.ascba.rebate.utils.CodeUtils.REQUEST_ALBUM_ICON_AUTH;
import static com.ascba.rebate.utils.CodeUtils.REQUEST_ALBUM_ICON_WORK;
import static com.ascba.rebate.utils.CodeUtils.REQUEST_CAMERA_ICON_AUTH;
import static com.ascba.rebate.utils.CodeUtils.REQUEST_CAMERA_ICON_WORK;

/**
 * 获取公司信息
 */
public class ComMsgActivity extends BaseDefaultNetActivity {
    private TextView tvOperName;
    private TextView tvRegMon;
    private TextView tvStatus;
    private TextView tvScope;
    private Button btnCommit;
    private File fileWork;
    private File fileAuth;
    private TextView tvAuthName;
    private String comName;//公司名称
    private View authView;
    private ImageView imWorkIcon;
    private ImageView imAuthIcon;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
    };
    private int type;//区分公司状态
    private View latError;
    private TextView tvError;
    private String workUrl;
    private String authUrl;
    private View viewShowWork;
    private View viewShowAuth;
    private int resultCode = RESULT_CANCELED;

    @Override
    protected int bindLayout() {
        return R.layout.activity_com_msg;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        initViews();
        getParams();
    }

    private void getParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            ComMsg comMsg = extras.getParcelable("company_msg");
            if (comMsg != null) {
                tvOperName.setText(comMsg.getOper_name());
                tvRegMon.setText(comMsg.getRegist_capi());
                tvStatus.setText(comMsg.getCompany_status());
                tvScope.setText(comMsg.getScope());
                this.comName = comMsg.getName();
                this.workUrl = comMsg.getChartered_demo();
                this.authUrl = comMsg.getWarrant_demo();
                Picasso.with(this).load(comMsg.getChartered()).placeholder(R.mipmap.bc_icon).into(imWorkIcon);
                int r = comMsg.getIs_oper_name();//当前登录用户是否是法人。0否，1是
                if (r == 0) {
                    authView.setVisibility(View.VISIBLE);
                    Picasso.with(this).load(comMsg.getWarrant()).placeholder(R.mipmap.bc_icon).into(imAuthIcon);
                    tvAuthName.setText(comMsg.getClientele_name());
                } else if (r == 1) {
                    authView.setVisibility(View.GONE);
                }
                String submitTip = comMsg.getSubmit_tip();
                if (submitTip == null || "".equals(submitTip)) {
                    latError.setVisibility(View.GONE);
                } else {
                    latError.setVisibility(View.VISIBLE);
                    tvError.setText(submitTip);
                }
                int status = comMsg.getStatus();//0：未申请，1：等待审核中，2：资料有误，3：审核已通过
                this.type = status;
                if (status == 2) {
                    viewShowWork.setVisibility(View.VISIBLE);
                    viewShowAuth.setVisibility(View.VISIBLE);
                } else {
                    viewShowWork.setVisibility(View.GONE);
                    viewShowAuth.setVisibility(View.GONE);
                }
                if (status == 0 || status == 2) {
                    btnCommit.setVisibility(View.VISIBLE);
                    btnCommit.setEnabled(true);
                } else if (status == 1) {
                    btnCommit.setVisibility(View.VISIBLE);
                    btnCommit.setEnabled(false);
                } else if (status == 3) {
                    btnCommit.setVisibility(View.GONE);
                }
                setBtnText();
            }
        }
    }
    private void setBtnText(){
        if(type==0){
            btnCommit.setText("提交");
        }else if(type==1){
            btnCommit.setText("审核中");
        }else if(type==2){
            btnCommit.setText("资料有误");
        }
    }

    private void initViews() {
        tvOperName = ((TextView) findViewById(R.id.tv_oper_name));
        tvRegMon = ((TextView) findViewById(R.id.tv_regist_capi));
        tvStatus = ((TextView) findViewById(R.id.tv_company_status));
        tvScope = ((TextView) findViewById(R.id.tv_scope));
        btnCommit = ((Button) findViewById(R.id.btn_commit));
        tvAuthName = ((TextView) findViewById(R.id.ed_auth_name));
        authView = findViewById(R.id.auth_view);
        imWorkIcon = ((ImageView) findViewById(R.id.busi_work_icon));
        imAuthIcon = ((ImageView) findViewById(R.id.busi_auth_icon));

        latError = fv(R.id.lat_error);
        tvError = fv(R.id.tv_error);

        viewShowWork = fv(R.id.tv_show_work);
        viewShowAuth = fv(R.id.tv_show_auth);

        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp(){
            @Override
            public void clickBack(View back) {
                backResult();
            }
        });
    }

    private void backResult() {
        Intent intent = getIntent();
        setResult(resultCode, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backResult();
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        showToast(result.getMsg());
        btnCommit.setEnabled(false);
        btnCommit.setText("审核中");
        resultCode = RESULT_OK;
        AppConfig.getInstance().putInt("company_status", 1);
        type = 1;
    }

    //提交商家资料
    public void goCommit(View view) {
        if (type == 0) {//提交商家资料
            if (fileWork == null || !fileWork.exists()) {
                showToast("请上传营业执照");
                return;
            }
            if (authView.getVisibility() == View.VISIBLE) {
                if (fileAuth == null || !fileAuth.exists()) {
                    showToast("请上传授权书");
                    return;
                }
            }
            AbstractRequest request = buildRequest(UrlUtils.add, RequestMethod.POST, null);
            request.add("company_name", comName);
            request.add("chartered", new FileBinary(fileWork));//营业执照
            request.add("warrant", new FileBinary(fileAuth));//授权书
            executeNetwork(0, "请稍后", request);
        } else if (type == 2) {//资料有误,重新提交
            if ((fileWork == null || !fileWork.exists()) && (fileAuth == null || !fileAuth.exists())) {
                showToast("请上传营业执照或授权书");
                return;
            }
            AbstractRequest request = buildRequest(UrlUtils.update, RequestMethod.POST, ComMsg.class);
            request.add("company_name", comName);
            if (fileWork != null && fileWork.exists()) {
                request.add("chartered", new FileBinary(fileWork));//营业执照
            }
            if (fileAuth != null && fileAuth.exists()) {
                request.add("warrant", new FileBinary(fileAuth));//授权书
            }
            executeNetwork(1, "请稍后", request);
        }

    }

    //选择营业执照
    public void uploadWorkPic(View view) {
        if (type == 0 || type == 2) {
            checkAndRequestAllPermission(permissions, new PermissionCallback() {
                @Override
                public void requestPermissionAndBack(boolean isOk) {
                    if (isOk) {
                        fileWork = createFile("qlqw201709060407work.png");
                        SelectIconDialog dialog = new SelectIconDialog(ComMsgActivity.this, fileWork, R.style.dialog);
                        dialog.setAlbumRequestCode(REQUEST_ALBUM_ICON_WORK);
                        dialog.setCameraRequestCode(REQUEST_CAMERA_ICON_WORK);
                        dialog.show();
                    }
                }
            });
        }
    }

    //选择授权书
    public void uploadAuthPic(View view) {
        if (type == 0 || type == 2) {
            checkAndRequestAllPermission(permissions, new PermissionCallback() {
                @Override
                public void requestPermissionAndBack(boolean isOk) {
                    if (isOk) {
                        fileAuth = createFile("qlqw201709060407auth.png");
                        SelectIconDialog dialog = new SelectIconDialog(ComMsgActivity.this, fileAuth, R.style.dialog);
                        dialog.setAlbumRequestCode(REQUEST_ALBUM_ICON_AUTH);
                        dialog.setCameraRequestCode(REQUEST_CAMERA_ICON_AUTH);
                        dialog.show();
                    }
                }
            });
        }
    }

    //查看demo
    public void toSeeRule01(View view) {
        Intent intent = new Intent(this, WebViewBaseActivity.class);
        intent.putExtra("name", "规范演示");
        intent.putExtra("url", workUrl);
        startActivity(intent);
    }

    //查看demo
    public void toSeeRule02(View view) {
        Intent intent = new Intent(this, WebViewBaseActivity.class);
        intent.putExtra("name", "规范演示");
        intent.putExtra("url", authUrl);
        startActivity(intent);
    }

    /**
     * 创建拍照图片文件
     *
     * @param fileName 文件名字
     */
    private File createFile(String fileName) {
        File file = FileUtils.getAppFile(this, Environment.DIRECTORY_PICTURES);
        return new File(file, fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_ICON_WORK:
                    if (fileWork != null && fileWork.exists()) {
                        handleImage(imWorkIcon, fileWork);
                    }
                    break;
                case REQUEST_ALBUM_ICON_WORK:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        fileWork = new File(picturePath);
                        handleImage(imWorkIcon, fileWork);
                    }
                    break;
                case REQUEST_CAMERA_ICON_AUTH:
                    if (fileAuth != null && fileAuth.exists()) {
                        handleImage(imAuthIcon, fileAuth);
                    }
                    break;
                case REQUEST_ALBUM_ICON_AUTH:
                    Uri selectedImage2 = data.getData();
                    String[] filePathColumn2 = {MediaStore.Images.Media.DATA};
                    Cursor cursor2 = getContentResolver().query(selectedImage2,
                            filePathColumn2, null, null, null);
                    if (cursor2 != null) {
                        cursor2.moveToFirst();
                        int columnIndex = cursor2.getColumnIndex(filePathColumn2[0]);
                        String picturePath = cursor2.getString(columnIndex);
                        cursor2.close();
                        fileAuth = new File(picturePath);
                        handleImage(imAuthIcon, fileAuth);
                    }
                    break;
            }
        }
    }

    private void handleImage(final ImageView im, final File file) {
        Luban.with(this).load(file).setCompressListener(new OnCompressListener() {
            File localFile = file;

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File resultFile) {
                localFile = resultFile;
                im.setImageBitmap(BitmapFactory.decodeFile(resultFile.getAbsolutePath()));
            }

            @Override
            public void onError(Throwable e) {
            }
        }).launch();
    }
}
