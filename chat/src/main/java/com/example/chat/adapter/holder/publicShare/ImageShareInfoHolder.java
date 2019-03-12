package com.example.chat.adapter.holder.publicShare;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.example.chat.R;
import com.example.chat.bean.post.PostDataBean;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.BaseRecyclerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.viewholder.BaseWrappedViewHolder;
import com.example.commonlibrary.baseadapter.decoration.GridSpaceDecoration;
import com.example.commonlibrary.utils.DensityUtil;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     15:26
 * QQ:         1981367757
 */

public class ImageShareInfoHolder extends BaseShareInfoViewHolder {
    private SuperRecyclerView display;
    private GridSpaceDecoration itemDecoration;

    public ImageShareInfoHolder(View itemView) {
        super(itemView);
        ViewStub viewStub = itemView.findViewById(R.id.vs_item_fragment_share_info_stub);
        viewStub.setLayoutResource(R.layout.item_fragment_share_info_image);
        display = (SuperRecyclerView) viewStub.inflate();
        display.setNestedScrollingEnabled(false);
    }


    public SuperRecyclerView getDisplay() {
        return display;
    }


    @Override
    protected void initData(PostDataBean data) {
        if (data != null) {
            int size = data.getImageList().size();
            if (itemDecoration != null) {
                display.removeItemDecoration(itemDecoration);
            }
            if (size <= 4) {
                display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 2));
                display.addItemDecoration(itemDecoration = new GridSpaceDecoration(2, DensityUtil.toDp(5), false));
            } else {
                display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
                display.addItemDecoration(itemDecoration = new GridSpaceDecoration(3, DensityUtil.toDp(5), false));
            }
            final ImageShareAdapter adapter = new ImageShareAdapter();
            display.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnSimpleItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    view.setTag(adapter.getData(position));
                    getAdapter().getOnItemClickListener()
                            .onItemChildClick(getAdapterPosition() - getAdapter().getItemUpCount(), view, position);
                }
            });
            adapter.refreshData(data.getImageList());
        }
    }


    public static class ImageShareAdapter extends BaseRecyclerAdapter<String, BaseWrappedViewHolder> {

        @Override
        protected int getLayoutId() {
            return R.layout.item_image_share_holder;
        }


        @Override
        public BaseWrappedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //           return super.onCreateViewHolder(parent, viewType);
            BaseWrappedViewHolder baseWrappedViewHolder = super.onCreateViewHolder(parent, viewType);
            int size = getSizeBySpanCount(parent);
            baseWrappedViewHolder.itemView.setLayoutParams(new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size));
            return baseWrappedViewHolder;
        }

        private int getSizeBySpanCount(ViewGroup parent) {
            int spanSize = ((GridLayoutManager) layoutManager).getSpanCount();
            return (DensityUtil.getScreenWidth(parent.getContext())) / spanSize;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
            holder.setImageUrl(R.id.iv_item_image_share_holder_display, data)
                    .setOnItemClickListener();

        }
    }


}
