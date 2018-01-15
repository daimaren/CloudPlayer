package org.cuieney.videolife.ui.fragment.live;

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
import org.cuieney.videolife.entity.LiveBean.LiveListBean;
import org.cuieney.videolife.presenter.PcLiveHomePresenter;
import org.cuieney.videolife.presenter.contract.PcLiveHomeContract;
import org.cuieney.videolife.ui.adapter.PcLiveAdapter;
import org.cuieney.videolife.ui.widget.AutoLayoutManager;
import org.cuieney.videolife.ui.widget.EndLessOnScrollListener;
import org.cuieney.videolife.ui.widget.MetroViewBorderImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by cuieney on 17/2/27.
 */

public class PcLiveHomeFragment extends BaseFragment<PcLiveHomePresenter> implements PcLiveHomeContract.View {

	@BindView(R.id.recycler)
	RecyclerView recycler;
	@BindView(R.id.refresh)
	SwipeRefreshLayout refresh;

	private String date;
	private String mVideoPlayUrl;
	private PcLiveAdapter adapter;
	private List<LiveListBean> mLiveAllList;
	private boolean isRunningOnTV = false;
	private MetroViewBorderImpl mMetroViewBorderImpl;
	public static PcLiveHomeFragment newInstance() {
		Bundle bundle = new Bundle();
		PcLiveHomeFragment videoFragment = new PcLiveHomeFragment();
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
			mPresenter.getAllLiveList(0,40, mLiveAllList);
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
		mLiveAllList = new ArrayList<>();

		adapter = new PcLiveAdapter(getActivity(), mLiveAllList);
		adapter.setOnItemClickListener((position, view, vh) -> startChildFragment(mLiveAllList.get(position), (PcLiveAdapter.MyHolder) vh));
		recycler.setAdapter(adapter);
		recycler.addOnScrollListener(new EndLessOnScrollListener(layout,0) {
			@Override
			public void onLoadMore() {
				mPresenter.getAllLiveList(0,40, mLiveAllList);
			}
		});
		mPresenter.getAllLiveList(0,40, mLiveAllList);
	}
	@Override
	public void showContent(List<LiveListBean> liveAllLists) {
		if (refresh.isRefreshing()) {
			refresh.setRefreshing(false);
			mLiveAllList.clear();
			adapter.clear();
			adapter.addAll(liveAllLists);
			recycler.setAdapter(adapter);
		}else{
			adapter.addAll(liveAllLists);
		}
		mLiveAllList.addAll(liveAllLists);
	}
	private void startChildFragment(LiveListBean liveListBean, PcLiveAdapter.MyHolder vh) {
		EventUtil.sendEvent(true + "");
		LiveDetailFragment fragment = LiveDetailFragment.newInstance(
				liveListBean);
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
