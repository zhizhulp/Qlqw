package com.ascba.rebate.activities.user_data;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascba.rebate.BuildConfig;
import com.ascba.rebate.R;
import com.ascba.rebate.appconfig.AppConfig;
import com.ascba.rebate.base.activity.BaseDefaultNetActivity;
import com.ascba.rebate.bean.LoginNextEntity;
import com.ascba.rebate.bean.Result;
import com.ascba.rebate.net.AbstractRequest;
import com.ascba.rebate.utils.CodeUtils;
import com.ascba.rebate.utils.FileUtils;
import com.ascba.rebate.utils.UrlUtils;
import com.ascba.rebate.view.SelectIconDialog;
import com.ascba.rebate.view.picasso.MaskTransformation;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.RequestMethod;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.ascba.rebate.utils.CodeUtils.REQUEST_ALBUM_ICON;
import static com.ascba.rebate.utils.CodeUtils.REQUEST_CAMERA_ICON;
import static com.ascba.rebate.utils.CodeUtils.REQUEST_NICK_NAME;

/**
 * 个人资料
 */
public class UserDataActivity extends BaseDefaultNetActivity implements View.OnClickListener {
    private File upLoadFile;
    private ImageView imHead;
    private TextView tvNickName;
    private TextView tvSex;
    private TextView tvAge;
    private TextView tvBorn;
    private String nickName;
    private boolean updateHead;//是否需要更新头像
    private boolean updateName;//是否需要更新昵称

    @Override
    protected int bindLayout() {
        return R.layout.activity_user_data;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        fv(R.id.lat_user_head).setOnClickListener(this);
        fv(R.id.lat_nick_name).setOnClickListener(this);
        fv(R.id.lat_sex).setOnClickListener(this);
        fv(R.id.lat_age).setOnClickListener(this);
        fv(R.id.lat_born_place).setOnClickListener(this);
        imHead = fv(R.id.im_user_icon);
        tvNickName = fv(R.id.tv_nick_name);
        tvSex = fv(R.id.tv_sex);
        tvAge = fv(R.id.tv_age);
        tvBorn = fv(R.id.tv_born_place);

        mMoneyBar.setCallBack(mMoneyBar.new CallbackImp() {
            @Override
            public void clickBack(View back) {
                backResults();
            }
        });
        AppConfig config = AppConfig.getInstance();
        setImageHead(config.getString("avatar", null));
        tvNickName.setText(config.getString("nickname", "未设置"));
        tvSex.setText(config.getString("sex", "保密"));
        tvAge.setText(String.valueOf(config.getInt("age", 18)));
        tvBorn.setText(config.getString("location", "待认证"));
    }

    private void setImageHead(String avatar) {
        Picasso.with(this).load(avatar)
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lat_user_head:
                checkAndRequestAllPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionCallback() {
                    @Override
                    public void requestPermissionAndBack(boolean isOk) {
                        if (isOk) {
                            createFile("qlqw201708211559.jpg");
                            SelectIconDialog dialog = new SelectIconDialog(UserDataActivity.this, upLoadFile, R.style.dialog);
                            dialog.show();
                        }
                    }
                });
                break;
            case R.id.lat_nick_name:
                startActivityForResult(ModifyNickNameActivity.class, null, REQUEST_NICK_NAME);
                break;
            case R.id.lat_born_place:
                break;
            case R.id.lat_age:
                break;
            case R.id.lat_sex:
                break;
        }
    }

    private void requestNetwork() {
        AbstractRequest request = buildRequest(UrlUtils.set, RequestMethod.POST, LoginNextEntity.class);
        if (upLoadFile != null) {
            request.add("avatar", new FileBinary(upLoadFile));
        }
        if (nickName != null) {
            request.add("nickname", nickName);
        }
        executeNetwork(0, "请稍后", request);
    }

    @Override
    protected <T> void mHandle200(int what, Result<T> result) {
        super.mHandle200(what, result);
        if (what == 0) {
            showToast(result.getMsg());
            LoginNextEntity loginNextEntity = (LoginNextEntity) result.getData();
            LoginNextEntity.UserInfoBean userInfo = loginNextEntity.getUser_info();
            if (upLoadFile != null) {
                AppConfig.getInstance().putString("avatar", userInfo.getAvatar());
                setImageHead(userInfo.getAvatar());
                if (!updateHead) {
                    updateHead = true;
                }
            }
            if (nickName != null) {
                AppConfig.getInstance().putString("nickname", userInfo.getNickname());
                tvNickName.setText(nickName);
                if (!updateName) {
                    updateName = true;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        backResults();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NICK_NAME && resultCode == RESULT_OK) {
            nickName = data.getStringExtra("nick_name");
            upLoadFile = null;
            requestNetwork();
        } else if (requestCode == REQUEST_CAMERA_ICON && resultCode == RESULT_OK) {
            Uri uri;
            if (Build.VERSION.SDK_INT > 23) {
                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", upLoadFile);
            } else {
                uri = Uri.fromFile(upLoadFile);
            }
            cropImage(uri, Uri.fromFile(upLoadFile));
        } else if (requestCode == REQUEST_ALBUM_ICON && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Uri uri;
                if (Build.VERSION.SDK_INT > 23) {
                    uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(picturePath));
                } else {
                    uri = Uri.parse("file://" + picturePath);
                }
                cropImage(uri, Uri.fromFile(upLoadFile));
            }
        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            requestNetwork();
        } else if (requestCode == CodeUtils.REQUEST_CROP && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult:" + upLoadFile.getAbsolutePath());
            Luban.with(this).load(upLoadFile)
                    .ignoreBy(350)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            upLoadFile = file;
                            nickName = null;
                            requestNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    }).launch();
        }
    }

    //裁剪图片
    private void cropImage(Uri inUri, Uri outUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置输出的宽高
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 720);
        intent.putExtra("scale", true);//去除黑边
        intent.putExtra("scaleUpIfNeeded", true);//去除黑边
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, CodeUtils.REQUEST_CROP);
    }

    /**
     * 创建拍照图片文件
     *
     * @param fileName 文件名字
     */
    private void createFile(String fileName) {
        File file = FileUtils.getAppFile(this, Environment.DIRECTORY_PICTURES);
        upLoadFile = new File(file, fileName);
    }

}
