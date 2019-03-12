package com.example.video.dagger.news.othernews.photolist;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.video.adapter.PhotoListAdapter;
import com.example.video.mvp.news.othernew.photolist.PhotoListFragment;
import com.example.video.mvp.news.othernew.photolist.PhotoListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      17:49
 * QQ:             1981367757
 */
@Module
public class PhotoListModule {

    private PhotoListFragment photoListFragment;

    public PhotoListModule(PhotoListFragment photoListFragment) {
        this.photoListFragment = photoListFragment;
    }


    @Provides
    public PhotoListAdapter providePhotoListAdapter(){
        return new PhotoListAdapter();
    }


    @Provides
    public PhotoListPresenter providePhotoListPresenter(DefaultModel photoListModel){
        return new PhotoListPresenter(photoListFragment,photoListModel);
    }

}
