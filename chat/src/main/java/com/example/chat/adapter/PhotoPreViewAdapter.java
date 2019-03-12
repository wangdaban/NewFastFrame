package com.example.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat.R;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.utils.DensityUtil;
import com.example.commonlibrary.utils.SystemUtil;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/20     20:28
 * QQ:         1981367757
 */

public class PhotoPreViewAdapter extends PagerAdapter {

    private OnPhotoViewClickListener mOnPhotoViewClickListener;
    private List<SystemUtil.ImageItem> data;
    private Context mContext;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;

    public SystemUtil.ImageItem getData(int position) {
        return data.get(position);
    }

    public interface OnPhotoViewClickListener {
        void onPhotoViewClick(View view, int position);
    }

    public PhotoPreViewAdapter(Context context, List<SystemUtil.ImageItem> previewList) {
        this.mContext = context;
        screenWidth = DensityUtil.getScreenWidth(context);
        screenHeight = DensityUtil.getScreenHeight(context);
        if (previewList == null) {
            data = new ArrayList<>();
        } else {
            data = previewList;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity_photo_preview, null);
        PhotoView photoView = view.findViewById(R.id.pv_item_activity_photo_preview_display);
        photoView.setOnPhotoTapListener((view1, x, y) -> {
            if (mOnPhotoViewClickListener != null) {
                mOnPhotoViewClickListener.onPhotoViewClick(view1, position);
            }
        });
        SystemUtil.ImageItem imageItem = data.get(position);
        String url = imageItem.getPath();
        if (url != null) {
            if (url.endsWith(".gif")) {
                BaseApplication
                        .getAppComponent()
                        .getImageLoader().loadImage(mContext
                        , GlideImageLoaderConfig.newBuild().cacheStrategy(GlideImageLoaderConfig.CACHE_SOURCE)
                                .asGif().override(screenWidth, screenHeight).imageView(photoView).url(url).build());
            } else {
                BaseApplication
                        .getAppComponent()
                        .getImageLoader().loadImage(mContext
                        , GlideImageLoaderConfig.newBuild().cacheStrategy(GlideImageLoaderConfig.CACHE_RESULT).centerInside().url(url).imageView(photoView).build());
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setOnPhotoViewClickListener(OnPhotoViewClickListener onPhotoViewClickListener) {
        mOnPhotoViewClickListener = onPhotoViewClickListener;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ViewGroup) object);
    }
}
