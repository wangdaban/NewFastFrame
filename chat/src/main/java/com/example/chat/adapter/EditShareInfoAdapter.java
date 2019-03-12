package com.example.chat.adapter;

import android.util.SparseIntArray;

import com.example.chat.R;
import com.example.commonlibrary.baseadapter.adapter.BaseMultipleRecyclerAdapter;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.utils.SystemUtil;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     22:10
 * QQ:         1981367757
 */

public class EditShareInfoAdapter extends BaseMultipleRecyclerAdapter<SystemUtil.ImageItem, BaseWrappedViewHolder> {
    @Override
    protected SparseIntArray getLayoutIdMap() {
        SparseIntArray sparseArray = new SparseIntArray();
        sparseArray.put(SystemUtil.ImageItem.ITEM_CAMERA, R.layout.item_activity_edit_share_info_camera);
        sparseArray.put(SystemUtil.ImageItem.ITEM_NORMAL, R.layout.item_activity_edit_share_info_normal);
        return sparseArray;
    }

    @Override
    protected void convert(BaseWrappedViewHolder holder, SystemUtil.ImageItem data) {
        if (holder.getItemViewType() == SystemUtil.ImageItem.ITEM_CAMERA) {
            holder.setImageResource(R.id.iv_item_activity_edit_share_info_camera, R.drawable.selector_image_add);
        } else {
            holder.setImageUrl(R.id.iv_item_activity_edit_share_info_normal, data.getPath())
                    .setOnItemChildClickListener(R.id.iv_item_activity_edit_share_info_normal_delete);
        }
        holder.setOnItemClickListener();
    }
}
