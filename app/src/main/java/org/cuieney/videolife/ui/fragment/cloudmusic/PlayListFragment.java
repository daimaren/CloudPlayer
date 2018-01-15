package org.cuieney.videolife.ui.fragment.cloudmusic;

import android.app.UiModeManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicInfo;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicItemBean;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicLrcInfo;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;
import org.cuieney.videolife.presenter.CloudMusicHomePresenter;
import org.cuieney.videolife.presenter.contract.CloudMusicHomeContract;
import org.cuieney.videolife.ui.adapter.CloudMusicAdapter;
import org.cuieney.videolife.ui.video.JumpUtils;
import org.cuieney.videolife.ui.widget.MetroViewBorderImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.UI_MODE_SERVICE;
import static org.cuieney.videolife.common.utils.Constants.MY_FAVORITE_MUSIC_BG_URL;
import static org.cuieney.videolife.common.utils.Constants.MY_FAVORITE_MUSIC_ID;
import static org.cuieney.videolife.common.utils.Constants.MY_FAVORITE_MUSIC_NAME;

/**
 * Created by cuieney on 17/3/4.
 */

public class PlayListFragment extends BaseFragment<CloudMusicHomePresenter> implements CloudMusicHomeContract.View  {
    private static final String TAG = PlayListFragment.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collToolBar;
    @BindView(R.id.img_detail)
    ImageView mImgDetail;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.recycler)
    XRecyclerView recycler;
    private List<CloudMusicItemBean> mMusicList;
    private CloudMusicAdapter mMusicAdapter;
    private boolean isRunningOnTV = false;
    private MetroViewBorderImpl mMetroViewBorderImpl;
    private String mPlayListId = MY_FAVORITE_MUSIC_ID;
    private String mPlayListBgUrl = MY_FAVORITE_MUSIC_BG_URL;
    private String mPlayListName = MY_FAVORITE_MUSIC_NAME;
    private static final String PLAY_LIST_ID = "playListId";
    private static final String PLAY_LIST_BG_URL = "playListBgUrl";
    private static final String PLAY_LIST_NAME = "playListName";
    public static PlayListFragment newInstance(String playListId, String playListBgUrl, String playListName) {
        Bundle bundle = new Bundle();
        bundle.putString(PLAY_LIST_ID, playListId);
        bundle.putString(PLAY_LIST_BG_URL, playListBgUrl);
        bundle.putString(PLAY_LIST_NAME, playListName);
        PlayListFragment fragment = new PlayListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取出data
        mPlayListId = getArguments().getString(PLAY_LIST_ID);
        mPlayListBgUrl = getArguments().getString(PLAY_LIST_BG_URL);
        mPlayListName = getArguments().getString(PLAY_LIST_NAME);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.music_home_detail_fragment;
    }

    @Override
    protected void initEventAndData() {
        UiModeManager uiModeManager = (UiModeManager)getActivity().getSystemService(UI_MODE_SERVICE);
        if(uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION){
            isRunningOnTV = true;
            LogUtil.d("running on TV device");
        }
        else
            LogUtil.d("running on non-TV device");
        initView();
        initListener();
    }

    private void initView(){
        GridLayoutManager layout = null;
        if (isRunningOnTV){
            mMetroViewBorderImpl = new MetroViewBorderImpl(getContext());
            mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);
            layout = new GridLayoutManager(getContext(), 2);//this
            layout.setOrientation(GridLayoutManager.HORIZONTAL);
            recycler.setLayoutManager(layout);
            recycler.setFocusable(false);
            mMetroViewBorderImpl.attachTo(recycler);
        }
        else{
            //on Mobile
            recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        initColor();
        mToolbar.setTitle("");
        initToolbarNav(mToolbar);
        ImageLoader.loadAll(getContext(), mPlayListBgUrl, mImgDetail);
        recycler.setLoadingMoreEnabled(false);
        recycler.setPullRefreshEnabled(false);

        mMusicList = new ArrayList<>();
        mMusicAdapter = new CloudMusicAdapter(getActivity(),mMusicList);
        recycler.setAdapter(mMusicAdapter);
        mPresenter.getMusicPlayList("playlist",mPlayListId);
        initHeadView();
    }
    private void initListener(){
        mMusicAdapter.setOnItemClickListener((position, view, vh) -> {
            startChildFragment(position, (CloudMusicAdapter.MyViewHoler)vh);
        });
        mFab.setOnClickListener(v -> {
            JumpUtils.goToCloudMusicPlayer(getActivity(), mMusicList, 0);
        });
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            _mActivity.onBackPressed();
        });
        toolbar.inflateMenu(R.menu.video_menu);
    }
    @Override
    public void showContent(MusicPlayListBean musicPlayListBean) {
        List<CloudMusicItemBean> musicListBean = musicPlayListBean.getPlaylistBean().getCloudMusicItemBean();
        mMusicAdapter.clear();
        mMusicList.clear();
        mMusicAdapter.addAll(musicListBean);
        mMusicList.addAll(musicListBean);
        recycler.scrollToPosition(0);
    }

    private void startChildFragment(int position, CloudMusicAdapter.MyViewHoler vh) {
        JumpUtils.goToCloudMusicPlayer(getActivity(), mMusicList, position);
    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void showMusicInfo(CloudMusicInfo cloudMusicInfo) {

    }

    @Override
    public void showLrcInfo(CloudMusicLrcInfo cloudMusicLrcInfo) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initHeadView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.music_detial_top_item, null);
        TextView title = (TextView) inflate.findViewById(R.id.title);
        ExpandableTextView expandableTextView = (ExpandableTextView) inflate.findViewById(R.id.expand_text_view);
        expandableTextView.setText("");
        title.setText(mPlayListName);
        recycler.addHeaderView(inflate);
    }

    int color = 0xffffcc00;
    private void initColor() {
        Glide.with(getContext()).load(mPlayListBgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                Palette.from(resource).generate(palette -> {
                    try {
                        color = palette.getLightMutedSwatch().getRgb();
                    } catch (Exception e) {
                        LogUtil.d(e.getMessage());
                    }
                    collToolBar.setContentScrimColor(color);
                    mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{color}));
                });

            }
        });
    }
}
