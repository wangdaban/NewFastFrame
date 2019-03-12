package com.example.chat.adapter;


import com.example.chat.R;
import com.example.commonlibrary.bean.chat.User;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      23:54
 * QQ:             1981367757
 */

public class SearchFriendAdapter extends BaseRecyclerAdapter<User, BaseWrappedViewHolder> {


    @Override
    protected int getLayoutId() {
        return R.layout.search_friend_item;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, User data) {
        holder.setImageUrl(R.id.riv_search_friend_item_avatar, data.getAvatar())
                .setText(R.id.tv_search_friend_item_name, data.getName())
                .setOnItemChildClickListener(R.id.btn_search_friend_item_look);
    }
}
