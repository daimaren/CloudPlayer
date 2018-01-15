package org.cuieney.videolife.ui.act;

import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.cuieney.videolife.App;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;
import org.cuieney.videolife.common.base.SimpleActivity;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.ui.fragment.live.LiveFragment;
import org.cuieney.videolife.ui.fragment.music.MusicFragment;
import org.cuieney.videolife.ui.fragment.stream.StreamPublishHomeFragment;
import org.cuieney.videolife.ui.fragment.video.VideoFragment;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_RIPPLE;
import static com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED;

public class MainActivity extends SimpleActivity implements BaseMainFragment.OnBackToFirstListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mNavigationView;
    List<SupportFragment> mFragments;
    private boolean isRunningOnTV = false;
    @Override
    protected int getLayout() {
        return R.layout.activity_main3;
    }

    @Override
    protected void initEventAndData() {
        isRunningOnTV = App.isRunningOnTV();
        mFragments = new ArrayList<>();
        mFragments.add(VideoFragment.newInstance());
        mFragments.add(LiveFragment.newInstance());
        mFragments.add(MusicFragment.newInstance());
        mFragments.add(StreamPublishHomeFragment.newInstance());
        loadMultipleRootFragment(R.id.act_container, 0
                , mFragments.get(0)
                , mFragments.get(1)
                , mFragments.get(2)
                , mFragments.get(3)
        );
        initView();
        initListener();
        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentSupportVisible(SupportFragment fragment) {
                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }
       });
    }

    private void initView() {
        mSearchView.setVisibility(View.GONE);
        if (isRunningOnTV){
            //获取设备当前方向
            int orientation = getResources().getConfiguration().orientation;
            //强制设置为横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            //Running On Phone
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mNavigationView
                .addItem(new BottomNavigationItem(R.drawable.movie_icon, "video").setActiveColor("#008867").setInActiveColor("#CCCCCC"))
                .addItem(new BottomNavigationItem(R.drawable.newspaper_icon, "live").setActiveColor("#008867"))
                .addItem(new BottomNavigationItem(R.drawable.music_icon, "music").setActiveColor("#008867"))
                .addItem(new BottomNavigationItem(R.drawable.book_icon, "stream").setActiveColor("#008867"))
                .initialise();
        mNavigationView.setBackgroundStyle(BACKGROUND_STYLE_RIPPLE);
        mNavigationView.setMode(MODE_FIXED);
        mNavigationView.setAutoHideEnabled(true);
        mNavigationView.setFocusable(false);
    }

    private void initListener(){
        mNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                showHideFragment(mFragments.get(position));
                switch (position) {
                    case 0:
                        mSearchView.setVisibility(View.GONE);
                        //StatusBarUtil.setColor(MainActivity.this, Color.parseColor("#6c4a41"));//#6c4a41
                        break;
                    case 1:
                        mSearchView.setVisibility(View.GONE);
                        //StatusBarUtil.setColor(MainActivity.this, Color.parseColor("#6c4a41"));//#008867
                        break;
                    case 2:
                        mSearchView.setVisibility(View.GONE);
                        //StatusBarUtil.setColor(MainActivity.this, Color.parseColor("#6c4a41"));//8b6b64
                        break;
                    case 3:
                        mSearchView.setVisibility(View.GONE);
                        //StatusBarUtil.setColor(MainActivity.this, Color.parseColor("#6c4a41"));//#485A66
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add:
                        //todo 2018/1/8
                        break;
                    case R.id.delete:
                        //todo 2018/1/8
                        break;
                    case R.id.setting:
                        //跳转到设置界面
                        break;
                    case R.id.about_me:
                        //todo 2018/1/8
                        break;
                }
            }
        });
    }

    @Subscribe
    public void hide(String isHide) {
        if (isHide.equals("true")) {
            mSearchView.setVisibility(View.GONE);
        } else {
            mSearchView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventUtil.register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }

    @Override
    public void onBackToFirstFragment() {
        mNavigationView.selectTab(0);
    }
}
