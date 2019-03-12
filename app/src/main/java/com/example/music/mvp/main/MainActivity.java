package com.example.music.mvp.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.example.chat.adapter.MenuDisplayAdapter;
import com.example.chat.base.ChatBaseActivity;
import com.example.chat.base.ConstantUtil;
import com.example.chat.bean.WeatherInfoBean;
import com.example.chat.events.DragLayoutEvent;
import com.example.chat.events.LocationEvent;
import com.example.chat.events.RefreshMenuEvent;
import com.example.chat.manager.NewLocationManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.chat.mvp.main.friends.FriendsActivity;
import com.example.chat.mvp.main.invitation.InvitationActivity;
import com.example.chat.mvp.main.recent.RecentFragment;
import com.example.chat.mvp.person.PersonFragment;
import com.example.chat.mvp.searchFriend.SearchFriendActivity;
import com.example.chat.mvp.settings.SettingsActivity;
import com.example.chat.mvp.shareinfo.ShareInfoFragment;
import com.example.chat.mvp.skin.SkinListActivity;
import com.example.chat.mvp.step.RecordStepActivity;
import com.example.chat.mvp.wallpaper.WallPaperActivity;
import com.example.chat.mvp.weather.WeatherInfoActivity;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.adapter.ViewPagerAdapter;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.SkinEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.customview.RoundAngleImageView;
import com.example.commonlibrary.customview.WrappedViewPager;
import com.example.commonlibrary.customview.draglayout.DragLayout;
import com.example.commonlibrary.customview.draglayout.OnDragDeltaChangeListener;
import com.example.commonlibrary.imageloader.glide.GlideImageLoaderConfig;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.skin.SkinManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.PermissionPageUtils;
import com.example.commonlibrary.utils.PermissionUtil;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.music.R;
import com.example.music.mvp.center.CenterFragment;
import com.example.video.mvp.index.IndexFragment;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends ChatBaseActivity implements OnDragDeltaChangeListener, View.OnClickListener {

    private WrappedViewPager display;
    private RadioGroup bottomContainer;
    private DragLayout dragLayout;
    private TextView nick;
    private TextView signature;
    private RoundAngleImageView avatar;
    private SuperRecyclerView menuDisplay;
    private WeatherInfoBean mWeatherInfoBean = new WeatherInfoBean();
    private TextView weatherCity;
    private TextView weatherTemperature;
    private MenuDisplayAdapter menuDisplayAdapter;
    private ArrayList<Fragment> fragmentList;


    private RadioButton chat, post, center, news, person;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (currentFragment instanceof RecentFragment) {
            ((RecentFragment) currentFragment).notifyNewIntentCome(intent);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onProfileSignIn(UserManager.getInstance().getCurrentUserObjectId());
    }

    @Override
    protected boolean needStatusPadding() {
        return false;
    }

    @Override
    public void updateData(Object o) {

    }


    @Override
    protected boolean needSlide() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = (String) item.getTitle();
        switch (title) {
            case "搜索":
                //                Intent intent = new Intent(this, SearchActivity.class);
                //                startActivity(intent);
                break;
            case "添加好友":
                ToastUtils.showShortToast("点击了添加好友111");
                SearchFriendActivity.start(this);
                break;
            case "建群":
                ToastUtils.showShortToast("创建群由于后台实时数据服务收费问题暂未开放");
                break;
            case "背景":
                WallPaperActivity.start(this, ConstantUtil.WALLPAPER);
                break;
            case "设置":
                SettingsActivity.start(this);
                break;
            case "重置皮肤":
                SkinEntity currentSkinEntity = UserDBManager.getInstance()
                        .getCurrentSkin();
                if (currentSkinEntity != null) {
                    currentSkinEntity.setHasSelected(false);
                    UserDBManager.getInstance().getDaoSession()
                            .getSkinEntityDao().update(currentSkinEntity);
                    SkinManager.getInstance().update(null);
                }
                break;
            case "皮肤中心":
                SkinListActivity.start(this);
                break;
            case "计步器":
                RecordStepActivity.start(this);
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu_layout, menu);
        return true;
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
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        bottomContainer = findViewById(R.id.rg_activity_main_bottom_container);
        display = findViewById(R.id.wvp_activity_main_display);
        dragLayout = findViewById(R.id.dl_activity_main_drag_container);
        menuDisplay = findViewById(R.id.srcv_menu_display);
        nick = findViewById(com.example.chat.R.id.tv_menu_nick);
        signature = findViewById(com.example.chat.R.id.tv_menu_signature);
        avatar = findViewById(com.example.chat.R.id.riv_menu_avatar);
        weatherCity = findViewById(com.example.chat.R.id.tv_menu_weather_city);
        weatherTemperature = findViewById(com.example.chat.R.id.tv_menu_weather_temperature);
        chat = findViewById(R.id.rb_activity_main_bottom_chat);
        post = findViewById(R.id.rb_activity_main_bottom_public);
        center = findViewById(R.id.rb_activity_main_bottom_center);
        news = findViewById(R.id.rb_activity_main_bottom_index);
        person = findViewById(R.id.rb_activity_main_bottom_person);
        RelativeLayout headLayout = findViewById(com.example.chat.R.id.rl_menu_head_layout);
        findViewById(R.id.ll_menu_bottom_container).setOnClickListener(this);
        headLayout.setOnClickListener(this);
        dragLayout.setListener(this);
        bottomContainer.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_activity_main_bottom_index) {
                display.setCurrentItem(3, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_public) {
                display.setCurrentItem(1, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_center) {
                display.setCurrentItem(2, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_person) {
                display.setCurrentItem(4, false);
            } else if (checkedId == R.id.rb_activity_main_bottom_chat) {
                display.setCurrentItem(0, false);
            }
        });
    }

    @Override
    protected void initData() {
        initRxBus();
        startSearchLiveWeather(BaseApplication.getAppComponent().getSharedPreferences().getString(ConstantUtil.CITY, null));
        updateUserInfo(UserDBManager.getInstance().getUser(UserManager.getInstance().getCurrentUserObjectId()));
        initMenu();
        fragmentList = new ArrayList<>();
        fragmentList.add(RecentFragment.newInstance());
        fragmentList.add(ShareInfoFragment.newInstance(UserManager
                .getInstance().getCurrentUserObjectId(), true));
        fragmentList.add(CenterFragment.newInstance());
        fragmentList.add(IndexFragment.newInstance());
        fragmentList.add(PersonFragment.newInstance());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setTitleAndFragments(null, fragmentList);
        display.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dragLayout.setIntercept(true);
                switch (position) {
                    case 0:
                        chat.setChecked(true);
                        dragLayout.setIntercept(false);
                        show(bottomContainer);
                        break;
                    case 1:
                        post.setChecked(true);
                        break;
                    case 2:
                        center.setChecked(true);
                        show(bottomContainer);
                        break;
                    case 3:
                        news.setChecked(true);
                        break;
                    case 4:
                        person.setChecked(true);
                        show(bottomContainer);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        display.setAdapter(viewPagerAdapter);
        //        addDisposable(RxBusManager.getInstance().registerEvent(SkinUpdateEvent.class, new Consumer<SkinUpdateEvent>() {
        //            @Override
        //            public void accept(SkinUpdateEvent skinUpdateEvent) throws Exception {
        //                chat.setCompoundDrawablesRelativeWithIntrinsicBounds(null, SkinManager.getInstance().getDrawable(R.drawable.ic_demo_five), null, null);
        //                post.setCompoundDrawablesRelativeWithIntrinsicBounds(null, SkinManager.getInstance().getDrawable(R.drawable.ic_demo_two), null, null);
        //                center.setCompoundDrawablesRelativeWithIntrinsicBounds(null, SkinManager.getInstance().getDrawable(R.drawable.ic_demo_three), null, null);
        //                news.setCompoundDrawablesRelativeWithIntrinsicBounds(null, SkinManager.getInstance().getDrawable(R.drawable.ic_demo_one), null, null);
        //                person.setCompoundDrawablesRelativeWithIntrinsicBounds(null, SkinManager.getInstance().getDrawable(R.drawable.ic_demo_four), null, null);
        //
        //                bottomContainer.setBackgroundColor(SkinManager.getInstance().getColor(R.color.custom_color_app_title_bg));
        //                bg.setBackgroundColor(SkinManager.getInstance().getColor(R.color.custom_color_app_bg));
        //            }
        //        }));
    }


    private void show(final View view) {
        if (view.getTranslationY() != 0) {
            ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0).setDuration(200).start();
        }
    }

    private void initMenu() {
        menuDisplay.setLayoutManager(new WrappedLinearLayoutManager(this));
        menuDisplayAdapter = new MenuDisplayAdapter();
        menuDisplay.setAdapter(menuDisplayAdapter);
        List<String> list = new ArrayList<>();
        list.add("好友");
        list.add("邀请");
        menuDisplayAdapter.refreshData(list);
        menuDisplayAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    FriendsActivity.start(MainActivity.this);
                } else {
                    InvitationActivity.start(MainActivity.this);
                }
            }
        });
    }

    private void initRxBus() {
        addDisposable(RxBusManager.getInstance().registerEvent(RefreshMenuEvent.class, refreshMenuEvent -> {
            if (refreshMenuEvent.getPosition() == -1) {
                menuDisplayAdapter.notifyDataSetChanged();
            } else {
                notifyMenuUpdate(refreshMenuEvent.getPosition());
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(UserEntity.class, this::updateUserInfo));
        addDisposable(RxBusManager.getInstance().registerEvent(LocationEvent.class, locationEvent -> {
            if (!locationEvent.getCity().equals(mWeatherInfoBean.getCity())) {
                startSearchLiveWeather(locationEvent.getCity());
            }
        }));
        addDisposable(RxBusManager.getInstance().registerEvent(DragLayoutEvent.class, dragLayoutEvent -> dragLayout.openMenu()));
    }


    private void updateUserInfo(UserEntity user) {
        nick.setText(user.getName());
        if (user.getSignature() == null) {
            signature.setText("^_^1设置属于你的个性签名吧^_^");
        } else {
            signature.setText(user.getSignature());
        }
        ToastUtils.showShortToast("保存个人信息成功");
        getAppComponent()
                .getImageLoader().loadImage(this, GlideImageLoaderConfig
                .newBuild().cacheStrategy(GlideImageLoaderConfig.CACHE_ALL).url(UserManager.getInstance().getCurrentUser().getWallPaper()).imageView(dragLayout).build());
        getAppComponent()
                .getImageLoader().loadImage(this, GlideImageLoaderConfig
                .newBuild().url(UserManager.getInstance().getCurrentUser().getAvatar()).imageView(avatar).build());
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if (!ListVideoManager.getInstance().onBackPressed()) {
            if (dragLayout.getCurrentState() == DragLayout.DRAG_STATE_OPEN) {
                dragLayout.closeMenu();
            } else if (bottomContainer.getTranslationY() != 0) {
                show(bottomContainer);
            } else if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.showShortToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


    public void notifyMenuUpdate(int position) {
        if (position < 2) {
            menuDisplayAdapter.notifyItemChanged(position);
        }
    }


    private void startSearchLiveWeather(String city) {
        if (city == null) {
            NewLocationManager.getInstance().startLocation();
            return;
        }
        mWeatherInfoBean.setCity(city);
        WeatherSearchQuery query = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch weatherSearch = new WeatherSearch(this);
        weatherSearch.setQuery(query);
        weatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                if (i == 1000) {
                    if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive localWeatherLive = localWeatherLiveResult.getLiveResult();
                        mWeatherInfoBean.setRealTime(localWeatherLive.getReportTime());
                        mWeatherInfoBean.setWeatherStatus(localWeatherLive.getWeather());
                        mWeatherInfoBean.setTemperature(localWeatherLive.getTemperature() + "°");
                        mWeatherInfoBean.setWind(localWeatherLive.getWindDirection() + "风       " + localWeatherLive
                                .getWindPower() + "级");
                        mWeatherInfoBean.setHumidity("湿度       " + localWeatherLive.getHumidity() + "%");
                        weatherCity.setText(mWeatherInfoBean.getCity());
                        weatherTemperature.setText(mWeatherInfoBean.getTemperature());
                    } else {
                        CommonLogger.e("获取到的天气信息为空");
                    }
                } else {
                    CommonLogger.e("获取到的天气信息失败" + i);
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                if (i == 1000) {
                    if (localWeatherForecastResult != null && localWeatherForecastResult.getForecastResult() != null
                            && localWeatherForecastResult.getForecastResult().getWeatherForecast() != null
                            && localWeatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                        LocalWeatherForecast localWeatherForecast = localWeatherForecastResult.getForecastResult();
                        mWeatherInfoBean.setForecastTime(localWeatherForecast.getReportTime());
                        mWeatherInfoBean.setForecastInfoList(localWeatherForecast.getWeatherForecast());
                    } else {
                        LogUtil.e("查询不到天气预报的结果");
                    }
                } else {
                    LogUtil.e("查询天气预报的结果失败" + i);
                }

            }
        });
        weatherSearch.searchWeatherAsyn();
    }


    @Override
    public void onDrag(View view, float delta) {
        if (fragmentList.get(display.getCurrentItem()) instanceof RecentFragment) {
            View icon = ((RecentFragment) fragmentList.get(display.getCurrentItem())).getIcon();
            if (icon != null) {
                ViewHelper.setAlpha(icon, (1 - delta));
            }
        }
    }

    @Override
    public void onCloseMenu() {

    }

    @Override
    public void onOpenMenu() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_menu_bottom_container) {

            PermissionUtil.requestLocation(new PermissionUtil.RequestPermissionCallBack() {
                @Override
                public void onRequestPermissionSuccess() {
                    WeatherInfoActivity.start(MainActivity.this, mWeatherInfoBean);
                    CommonLogger.e("onRequestPermissionSuccess");
                    ToastUtils.showShortToast("授权成功");
                }

                @Override
                public void onRequestPermissionFailure() {
                    CommonLogger.e("onRequestPermissionFailure");
                    ToastUtils.showShortToast("授权失败");
                    showBaseDialog("权限跳转", "是否需要跳转到界面开启权限", "取消", "跳转"
                            , null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PermissionPageUtils.jumpPermissionPage(MainActivity.this);
                                }
                            }
                    );
                }
            }, this);
        } else if (id == R.id.rl_menu_head_layout) {
            UserDetailActivity.start(this, UserManager.getInstance().getCurrentUserObjectId(), ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(avatar, "avatar")
                    , Pair.create(signature, "signature"), Pair.create(nick, "name")
            ));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ConstantUtil.REQUEST_CODE_WEATHER_INFO:
                    WeatherInfoBean weatherInfoBean = (WeatherInfoBean) data.getSerializableExtra(ConstantUtil.DATA);
                    if (weatherInfoBean != null) {
                        mWeatherInfoBean = weatherInfoBean;
                        weatherCity.setText(mWeatherInfoBean.getCity());
                        weatherTemperature.setText(mWeatherInfoBean.getTemperature());
                    }
                    break;
            }
        }
    }
}
