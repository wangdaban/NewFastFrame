package com.snew.video.mvp.qq;

import android.os.Bundle;
import android.view.View;

import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.baseadapter.decoration.GridSpaceDecoration;
import com.example.commonlibrary.baseadapter.decoration.ListViewDecoration;
import com.example.commonlibrary.customview.swipe.CustomSwipeRefreshLayout;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.utils.DensityUtil;
import com.snew.video.R;
import com.snew.video.adapter.QQVideoListAdapter;
import com.snew.video.adapter.VideoHeaderAdapter;
import com.snew.video.base.VideoBaseFragment;
import com.snew.video.bean.CommonVideoBean;
import com.snew.video.bean.QQVideoListBean;
import com.snew.video.bean.QQVideoTabListBean;
import com.snew.video.dagger.qq.DaggerQQVideoListComponent;
import com.snew.video.dagger.qq.QQVideoListModule;
import com.snew.video.mvp.qq.detail.QQVideoDetailActivity;
import com.snew.video.util.VideoUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     15:58
 */
public class QQVideoListFragment extends VideoBaseFragment<BaseBean, QQVideoListPresenter> implements CustomSwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {


    private CustomSwipeRefreshLayout refresh;
    private SuperRecyclerView display;

    @Inject
    QQVideoListAdapter mVideoAdapter;


    private int year = -1;
    private int award = -1;
    private int type = -1;
    private int area = -1;
    private int sort = -1;
    private int videoType = -1;
    private VideoHeaderAdapter videoHeaderAdapter;


    private String classify = "全部";
    private String areas = "全部";
    private String years = "全部";
    private String sorts = "按人气";

    static QQVideoListFragment newInstance(int originType, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(VideoUtil.VIDEO_TYPE, type);
        bundle.putInt(VideoUtil.VIDEO_URL_TYPE, originType);
        QQVideoListFragment qqVideoListFragment = new QQVideoListFragment();
        qqVideoListFragment.setArguments(bundle);
        return qqVideoListFragment;
    }


    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }


    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_qq_video_list;
    }

    @Override
    protected void initView() {
        refresh = findViewById(R.id.refresh_fragment_qq_video_list_refresh);
        display = findViewById(R.id.srcv_fragment_qq_video_list_display);
        refresh.setOnRefreshListener(this);

    }


    private int videoUrlType;

    @Override
    protected void initData() {
        DaggerQQVideoListComponent.builder().qQVideoListModule(new QQVideoListModule(this))
                .videoComponent(getComponent()).build().inject(this);
        videoType = getArguments().getInt(VideoUtil.VIDEO_TYPE);
        videoUrlType = getArguments().getInt(VideoUtil.VIDEO_URL_TYPE);
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        display.addHeaderView(getHeaderView());
        display.addItemDecoration(new GridSpaceDecoration(3, DensityUtil.toDp(3), DensityUtil.toDp(15), true));
        display.setAdapter(mVideoAdapter);
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        mVideoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                QQVideoListBean.JsonvalueBean.ResultsBean resultsBean = mVideoAdapter.getData(position);
                String id;
                if (resultsBean.getFields().getColumn_info() != null) {
                    QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean.ColumnInfo columnInfo = getAppComponent().getGson().fromJson(resultsBean.getFields().getColumn_info(), QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean.ColumnInfo.class);
                    id = columnInfo.getColumn_id();
                } else {
                    id = resultsBean.getId();
                }
                CommonVideoBean commonVideoBean=new CommonVideoBean();
                commonVideoBean.setVideoType(videoType);
                commonVideoBean.setId(id);
                commonVideoBean.setTitle(resultsBean.getFields().getTitle());
                commonVideoBean.setImage(resultsBean.getFields().getHorizontal_pic_url());
                commonVideoBean.setUrl(VideoUtil.getParseUrl(resultsBean.getId(),videoUrlType));
                QQVideoDetailActivity.start(getActivity(),commonVideoBean);
            }
        });

    }

    private View getHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.view_fragment_video_list_header, display.getHeaderContainer(), false);
        SuperRecyclerView display = headerView.findViewById(R.id.srcv_view_fragment_video_list_header_display);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.addItemDecoration(new ListViewDecoration(DensityUtil.toDp(5)));
        videoHeaderAdapter = new VideoHeaderAdapter();
        display.setAdapter(videoHeaderAdapter);
        videoHeaderAdapter.setOnInnerItemClickListener((position, innerPosition, bean) -> {
            QQVideoTabListBean.IndexBean.OptionBean item = bean.getOption().get(innerPosition);
            if (videoUrlType == VideoUtil.VIDEO_URL_TYPE_QQ) {
                int value = Integer.parseInt(item.getValue());
                switch (bean.getName()) {
                    case "iawards":
                        if (award != value) {
                            award = value;
                            break;
                        }
                        return;
                    case "iarea":
                        if (area != value) {
                            area = value;
                            break;
                        }
                        return;
                    case "iyear":
                        if (year != value) {
                            year = value;
                            break;
                        }
                        return;
                    case "itype":
                        if (type != value) {
                            type = value;
                            break;
                        }
                        return;
                    case "sort":
                        if (sort != value) {
                            sort = value;
                            break;
                        }
                        return;
                }
            } else {
                if ("地区".equals(bean.getDisplay_name())) {
                    if (areas.equals(item.getDisplay_name())) {
                        return;
                    } else {
                        areas = item.getDisplay_name();
                    }
                } else if ("分类".equals(bean.getDisplay_name())) {
                    if (classify.equals(item.getDisplay_name())) {
                        return;
                    } else {
                        classify = item.getDisplay_name();
                    }

                } else if ("年代".equals(bean.getDisplay_name())) {
                    if (years.equals(item.getDisplay_name())) {
                        return;
                    } else {
                        years = item.getDisplay_name();
                    }
                } else {
                    if (sorts.equals(item.getDisplay_name())) {
                        return;
                    } else {
                        sorts = item.getDisplay_name();
                    }
                }
            }
            onRefresh();
        });
        return headerView;
    }

    @Override
    protected void updateView() {
        presenter.getHeaderListData(videoUrlType, videoType);
    }

    @Override
    public void updateData(BaseBean baseBean) {
        if (baseBean.getType() == VideoUtil.BASE_TYPE_VIDEO_LIST_DATA) {
            if (refresh.isRefreshing()) {
                mVideoAdapter.refreshData((List<QQVideoListBean.JsonvalueBean.ResultsBean>) baseBean.getData());
            } else {
                mVideoAdapter.addData((List<QQVideoListBean.JsonvalueBean.ResultsBean>) baseBean.getData());
            }
        } else if (baseBean.getType() == VideoUtil.BASE_TYPE_VIDEO_LIST_HEADER) {
            QQVideoTabListBean qqVideoTabListBean = (QQVideoTabListBean) baseBean.getData();
            videoHeaderAdapter.refreshData(qqVideoTabListBean.getIndex());
            if (qqVideoTabListBean.getIndex() != null && qqVideoTabListBean.getIndex().size() > 0) {
                if (videoUrlType == VideoUtil.VIDEO_URL_TYPE_QQ) {
                    for (QQVideoTabListBean.IndexBean bean :
                            qqVideoTabListBean.getIndex()) {
                        switch (bean.getName()) {
                            case "iawards":
                                award = bean.getDefault_value();
                                break;
                            case "iarea":
                                area = bean.getDefault_value();
                                break;
                            case "iyear":
                                year = bean.getDefault_value();
                                break;
                            case "itype":
                                type = bean.getDefault_value();
                                break;
                            case "sort":
                                sort = bean.getDefault_value();
                                break;
                        }
                    }
                    onRefresh();
                }
            }
        }
    }


    @Override
    public void showLoading(String loadingMsg) {
        super.showLoading(loadingMsg);
        refresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        if (videoUrlType == VideoUtil.VIDEO_URL_TYPE_QQ) {
            presenter.getData(true, videoType, type, year, area, award, sort);
        } else {
            presenter.getUpdateData(true, videoType, classify, areas, years, sorts);
        }
    }

    @Override
    public void loadMore() {
        if (videoUrlType == VideoUtil.VIDEO_URL_TYPE_QQ) {
            presenter.getData(false, videoType, type, year, area, award, sort);
        } else {
            presenter.getUpdateData(false, videoType, classify, areas, years, sorts);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ListVideoManager.getInstance().release();
    }
}
