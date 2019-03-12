package com.example.music.mvp.center;

import android.view.View;

import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedGridLayoutManager;
import com.example.commonlibrary.baseadapter.decoration.GridSpaceDecoration;
import com.example.commonlibrary.customview.ToolBarOption;
import com.example.commonlibrary.router.Router;
import com.example.commonlibrary.router.RouterConfig;
import com.example.commonlibrary.router.RouterRequest;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.music.R;
import com.example.music.adapter.CenterAdapter;
import com.example.video.bean.CenterBean;
import com.snew.video.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      14:58
 * QQ:             1981367757
 */

public class CenterFragment extends BaseFragment {

    private SuperRecyclerView display;
    private CenterAdapter centerAdapter;

    @Override
    public void updateData(Object o) {

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
        return R.layout.fragment_center;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_center_display);
    }

    @Override
    protected void initData() {
        display.setLayoutManager(new WrappedGridLayoutManager(getContext(), 3));
        centerAdapter = new CenterAdapter();
        display.addItemDecoration(new GridSpaceDecoration(3, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
        display.setAdapter(centerAdapter);
        centerAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    Router.getInstance().deal(RouterRequest.newBuild().provideName(RouterConfig.MUSIC_PROVIDE_NAME)
                            .actionName("enter")
                            .context(getContext()).build());
                } else if (position == 1) {
                    //                    Router.getInstance().deal(RouterRequest.newBuild().provideName(RouterConfig
                    //                            .LIVE_PROVIDE_NAME).actionName("enter").context(getContext()).build());
                    ToastUtils.showShortToast("由于全名直播平台的原因暂时关闭");
                } else if (position == 2) {
                    //                    ToastUtils.showShortToast("暂时不开放");
                    MainActivity.start(getActivity());
                }
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("应用中心");
        toolBarOption.setNeedNavigation(false);
        setToolBar(toolBarOption);
    }

    @Override
    protected void updateView() {
        centerAdapter.addData(getDefaultData());
    }

    private List<CenterBean> getDefaultData() {
        List<CenterBean> result = new ArrayList<>();
        CenterBean library = new CenterBean();
        library.setTitle("音乐");
        library.setResId(R.drawable.ic_demo_one);
        result.add(library);
        CenterBean card = new CenterBean();
        card.setTitle("直播");
        card.setResId(R.drawable.ic_demo_two);
        result.add(card);
        CenterBean system = new CenterBean();
        system.setTitle("视频");
        system.setResId(R.drawable.ic_demo_three);
        result.add(system);
        return result;
    }


    public static CenterFragment newInstance() {
        return new CenterFragment();
    }
}
