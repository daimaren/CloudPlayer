package org.cuieney.videolife.ui.fragment.cloudmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.konifar.fab_transformation.FabTransformation;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;
import org.cuieney.videolife.presenter.CloudMusicDetailPresenter;
import org.cuieney.videolife.presenter.contract.CloudMusicDetailContract;

import butterknife.BindView;

/**
 * Created by cuieney on 17/3/4.
 */

public class DetailFragment extends BaseFragment<CloudMusicDetailPresenter> implements CloudMusicDetailContract.View{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collToolBar;
    @BindView(R.id.recycler)
    XRecyclerView recycler;
    @BindView(R.id.img_detail)
    ImageView mImgDetail;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    protected static final String PLAY_LIST_ID = "playListId";
    private String mPlayListId;
    private MusicPlayListBean mPlayListBean;

    public static DetailFragment newInstance(String playListId) {
        Bundle args = new Bundle();
        args.putString(PLAY_LIST_ID, playListId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayListId = getArguments().getString(PLAY_LIST_ID,"126102318");
    }

    @Override
    protected void initEventAndData() {
        initView();
    }
    private void initView() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        initColor();
        mToolbar.setTitle("");
        initToolbarNav(mToolbar);
    }

    @Override
    public void showContent(MusicPlayListBean playListBean) {

    }

    @Override
    public void error(Throwable throwable) {

    }

    protected void initToolbarNav(android.support.v7.widget.Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            _mActivity.onBackPressed();
        });
        toolbar.inflateMenu(R.menu.video_menu);
    }
    private void initHeadView() {
    }

    int color = 0xffffcc00;

    private void initColor() {
    }

    @Override
    public void onResume() {
        super.onResume();
        FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
            @Override
            public void onStartTransform() {

            }

            @Override
            public void onEndTransform() {
                if (mImgDetail.getVisibility() == View.INVISIBLE) {
                    mImgDetail.setVisibility(View.VISIBLE);
                }
            }
        }).transformFrom(mImgDetail);
    }

}