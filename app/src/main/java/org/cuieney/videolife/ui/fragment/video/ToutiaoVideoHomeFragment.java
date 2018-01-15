package org.cuieney.videolife.ui.fragment.video;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;

import org.cuieney.videolife.App;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.base.DetailTransition;
import org.cuieney.videolife.common.component.EventUtil;
import org.cuieney.videolife.entity.ToutiaoBean.MultiNewsArticleDataBean;
import org.cuieney.videolife.presenter.ToutiaoVideoHomePresenter;
import org.cuieney.videolife.presenter.contract.ToutiaoVideoHomeContract;
import org.cuieney.videolife.ui.adapter.ToutiaoVideoAdapter;
import org.cuieney.videolife.ui.widget.AutoLayoutManager;
import org.cuieney.videolife.ui.widget.EndLessOnScrollListener;
import org.cuieney.videolife.ui.widget.MetroViewBorderImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cuieney on 17/2/27.
 */

public class ToutiaoVideoHomeFragment extends BaseFragment<ToutiaoVideoHomePresenter> implements ToutiaoVideoHomeContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private String date;
    private String mVideoPlayUrl;
    private ToutiaoVideoAdapter adapter;
    private List<MultiNewsArticleDataBean> mVideoListBean;
    private boolean isRunningOnTV = false;
    private MetroViewBorderImpl mMetroViewBorderImpl;
    public static ToutiaoVideoHomeFragment newInstance() {
        Bundle bundle = new Bundle();
        ToutiaoVideoHomeFragment videoFragment = new ToutiaoVideoHomeFragment();
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_home_fragment;
    }

    @Override
    protected void initEventAndData() {
        isRunningOnTV = App.isRunningOnTV();
        refresh.setProgressViewOffset(false, 100, 200);
        refresh.setOnRefreshListener(() -> {
            mPresenter.getVideoData("1508239793",mVideoListBean);
        });

        LinearLayoutManager layout = null;
        if (isRunningOnTV){
            mMetroViewBorderImpl = new MetroViewBorderImpl(getContext());
            mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);
            layout = new AutoLayoutManager(getContext(), 1);//this
            layout.setOrientation(GridLayoutManager.HORIZONTAL);
            recycler.setLayoutManager(layout);
            recycler.setFocusable(false);
            mMetroViewBorderImpl.attachTo(recycler);
        }
        else {
            layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recycler.setLayoutManager(layout);
        }
        mVideoListBean = new ArrayList<>();
        adapter = new ToutiaoVideoAdapter(getActivity(), mVideoListBean);
        adapter.setOnItemClickListener((position, view, vh) -> startChildFragment(mVideoListBean.get(position), (ToutiaoVideoAdapter.MyHolder) vh));
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new EndLessOnScrollListener(layout,0) {
            @Override
            public void onLoadMore() {
                mPresenter.getVideoData("1508239793",mVideoListBean);
            }
        });
        mPresenter.getVideoData("1508239793",mVideoListBean);
    }
    @Override
    public void showContent(List<MultiNewsArticleDataBean> dataBeen) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            mVideoListBean.clear();
            adapter.clear();
            adapter.addAll(dataBeen);
            recycler.setAdapter(adapter);
        }else{
            adapter.addAll(dataBeen);
        }
        mVideoListBean.addAll(dataBeen);
    }
    private void startChildFragment(MultiNewsArticleDataBean videoBean, ToutiaoVideoAdapter.MyHolder vh) {
        EventUtil.sendEvent(true + "");
        /*ToutiaoVideoDetailFragment fragment = ToutiaoVideoDetailFragment.newInstance(
                videoBean);*/
        ToutiaoVideoDetailFragmentEx fragment = ToutiaoVideoDetailFragmentEx.newInstance(
                videoBean);
        // 这里是使用SharedElement的用例

        // LOLLIPOP(5.0)系统的 SharedElement支持有 系统BUG， 这里判断大于 > LOLLIPOP
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setExitTransition(new Fade());
            fragment.setEnterTransition(new Slide());
            fragment.setSharedElementReturnTransition(new DetailTransition());
            fragment.setSharedElementEnterTransition(new DetailTransition());

            // 25.1.0以下的support包,Material过渡动画只有在进栈时有,返回时没有;
            // 25.1.0+的support包，SharedElement正常
            fragment.transaction()
                    .addSharedElement(vh.imageView, getString(R.string.image_transition))
//                        .addSharedElement(((VideoAdapter.MyHolder) vh).textView,"tv")
                    .commit();
        }
        start(fragment);
    }

    @Override
    public void error(Throwable throwable) {
        Log.e("oye", "error: ", throwable);
    }
}
