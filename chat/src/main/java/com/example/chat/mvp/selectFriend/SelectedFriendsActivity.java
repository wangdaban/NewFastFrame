package com.example.chat.mvp.selectFriend;

import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.FriendsAdapter;
import com.example.chat.base.ConstantUtil;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.view.IndexView;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/27      20:36
 * QQ:             1981367757
 */
public class SelectedFriendsActivity extends ChatBaseActivity implements IndexView.MyLetterChangeListener {
    SuperRecyclerView display;
    IndexView indexView;
    private TextView index;
    private LinearLayout bottomContainer;
    private ArrayList<String> selectedContacts = new ArrayList<>();
    private FriendsAdapter adapter;
    private WrappedLinearLayoutManager mLinearLayoutManager;
    private String from;


    @Override
    public void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_selected_friend_display);
        indexView = (IndexView) findViewById(R.id.index_activity_selected_friend_index);
        index = (TextView) findViewById(R.id.tv_activity_selected_friend_index);
        bottomContainer = (LinearLayout) findViewById(R.id.ll_activity_selected_friend_bottom);
    }

    @Override
    public void initData() {
        adapter = new FriendsAdapter();
        adapter.setHasSelected(true);
        display.setLayoutManager(mLinearLayoutManager = new WrappedLinearLayoutManager(this));
        display.setAdapter(adapter);
        ArrayList<UserEntity> list = (ArrayList<UserEntity>) getIntent().getSerializableExtra(ConstantUtil.DATA);
        from = getIntent().getStringExtra(ConstantUtil.FROM);
        adapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {
            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.cb_item_fragment_friends_check) {
                    CheckBox checkBox = (CheckBox) view;
                    UserEntity userEntity = adapter.getData(position);
                    if (checkBox.isChecked()) {
                        selectedContacts.add(userEntity.getUid());
                        bottomContainer.addView(getImageView(userEntity.getAvatar()
                                , userEntity.getUid()));
                    } else {
                        selectedContacts.remove(userEntity.getUid());
                        if (bottomContainer.findViewWithTag(userEntity.getUid()) != null) {
                            bottomContainer.removeView(bottomContainer.findViewWithTag(userEntity.getUid()));
                        }
                    }
                }
            }
        });
        adapter.addData(list);
        initActionBar();
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setTitle("选择好友");
        toolBarOption.setRightText("完成");
        toolBarOption.setRightListener(v -> {
            if (selectedContacts.size() == 0) {
                ToastUtils.showShortToast("选择好友不能为空");
                return;
            }
                Intent intent = new Intent();
                intent.putExtra(ConstantUtil.DATA, selectedContacts);
                setResult(Activity.RESULT_OK, intent);
                finish();


        });
        toolBarOption.setAvatar(null);
        setToolBar(toolBarOption);
    }

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
        return R.layout.activity_selected_friend;
    }

    @Override
    public void onLetterChanged(String s) {
        index.setVisibility(View.VISIBLE);
        index.setText(s);
        int size = adapter.getData().size();
        for (int i = 0; i < size; i++) {
            UserEntity bean = adapter.getData(i);
            if (AppUtil.getSortedKey(bean.getName()).equals(s)) {
                mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }

    @Override
    public void onFinished() {
        index.setVisibility(View.GONE);
    }

    private ImageView getImageView(String url, String tag) {
        ImageView avatar = new ImageView(SelectedFriendsActivity.this);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        avatar.setLayoutParams(params);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        avatar.setPadding(5, 5, 5, 5);
        avatar.setTag(tag);
        Glide.with(this).load(url).into(avatar);
        return avatar;
    }


    @Override
    public void updateData(Object o) {

    }


    public static void start(Activity activity, String from, ArrayList<UserEntity> list, int requestCode) {
        Intent intent = new Intent(activity, SelectedFriendsActivity.class);
        intent.putExtra(ConstantUtil.DATA, list);
        intent.putExtra(ConstantUtil.FROM, from);
        activity.startActivityForResult(intent, requestCode);
    }


}
