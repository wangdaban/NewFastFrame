package com.example.chat.mvp.settings;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.account.AccountManageActivity;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.customview.ToolBarOption;

import androidx.appcompat.widget.SwitchCompat;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/5      16:43
 * QQ:             1981367757
 */

public class SettingsActivity extends ChatBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {


    private RoundAngleImageView avatar;
    private TextView account;
    private TextView nick;


    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_settings;
    }


    @Override
    public void initView() {
        RelativeLayout headerLayout = findViewById(R.id.rl_setting_header);
        account = findViewById(R.id.tv_setting_account);
        nick = findViewById(R.id.tv_tv_setting_nick);
        avatar = findViewById(R.id.riv_setting_avatar);
        SwitchCompat notification = findViewById(R.id.sc_activity_settings_notify);
        RelativeLayout clear = findViewById(R.id.rl_activity_settings_clear);
        RelativeLayout accountManage = findViewById(R.id.rl_activity_settings_account_manage);
        Button logout = findViewById(R.id.btn_setting_logout);
        headerLayout.setOnClickListener(this);
        clear.setOnClickListener(this);
        accountManage.setOnClickListener(this);
        logout.setOnClickListener(this);
        notification.setOnCheckedChangeListener(this);

    }


    @Override
    public void initData() {
        nick.setText(UserManager.getInstance().getCurrentUser().getName());
        account.setText("帐号：" + UserManager.getInstance().getCurrentUser().getUsername());
        Glide.with(this).load(UserManager.getInstance().getCurrentUser().getAvatar()).into(avatar);
        initActionBar();
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        toolBarOption.setTitle("设置");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_setting_header) {
        } else if (i == R.id.rl_activity_settings_clear) {
        } else if (i == R.id.btn_setting_logout) {
            showBaseDialog("下线", "确定要退出吗?", "取消", "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserManager.getInstance().logout();
                    finish();
                }
            });
        } else if (i == R.id.rl_activity_settings_account_manage) {
            AccountManageActivity.start(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //                BaseApplication.getAppComponent().getSharedPreferences().edit().putBoolean(ChatUtil.PUSH_STATUS,isChecked).apply();
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void updateData(Object o) {

    }
}
