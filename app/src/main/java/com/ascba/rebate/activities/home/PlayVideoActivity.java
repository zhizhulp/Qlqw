package com.ascba.rebate.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ascba.rebate.R;
import com.ascba.rebate.base.activity.BaseUIActivity;
import com.ascba.rebate.bean.HomeBean;
import com.boredream.bdvideoplayer.BDVideoView;
import com.boredream.bdvideoplayer.listener.SimpleOnVideoControlListener;

/**
 * Created by 李平 on 2017/03/22 0022.
 * 视频播放
 */

public class PlayVideoActivity extends BaseUIActivity {

    private HomeBean.VideoBean videoBean = null;
    private static String extras = "url";
    private BDVideoView player;

    @Override
    protected int bindLayout() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        videoBean = getIntent().getParcelableExtra(extras);
        if (videoBean != null) {
            initViews();
        } else {
            finish();
        }
        hideNavigationBar();
    }

    @Override
    protected int setUIMode() {
        return BaseUIActivity.UIMODE_FULLSCREEN;
    }

    public static void newIndexIntent(Context context, HomeBean.VideoBean message) {
        Intent newIntent = new Intent(context, PlayVideoActivity.class);
        newIntent.putExtra(extras, ((Parcelable) message));
        context.startActivity(newIntent);
    }

    private void initViews() {
        player = fv(R.id.player_01);
        player.setOnVideoControlListener(new SimpleOnVideoControlListener() {

            @Override
            public void onRetry(int errorStatus) {
                player.startPlayVideo(videoBean);
            }

            @Override
            public void onBack() {
                onBackPressed();
            }

            @Override
            public void onFullScreen() {
            }
        });
        player.startPlayVideo(videoBean);
    }

    @Override
    public void onBackPressed() {
        player.onDestroy();
        super.onBackPressed();
    }

    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    protected void onPause() {
        player.onStop();
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        player.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void hideNavigationBar() {
        Window window = getWindow();
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            window.getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            WindowManager.LayoutParams params = window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.setAttributes(params);
        }
    }
}
