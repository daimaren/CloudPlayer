package org.cuieney.videolife.ui.fragment.live;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.konifar.fab_transformation.FabTransformation;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.LiveBean.LiveListBean;
import org.cuieney.videolife.entity.LiveBean.TempLiveVideoBean;
import org.cuieney.videolife.presenter.LiveVideoInfoPresenter;
import org.cuieney.videolife.presenter.contract.LiveVideoInfoContract;
import org.cuieney.videolife.ui.video.JumpUtils;

import butterknife.BindView;

/**
 * Created by daimaren on 2017/10/27
 * 975808394@qq.com
 */

public class LiveDetailFragment extends BaseFragment<LiveVideoInfoPresenter> implements LiveVideoInfoContract.View{
	@BindView(R.id.img_detail)
	ImageView mImgDetail;
	@BindView(R.id.toolbar)
	Toolbar mToolbar;
	@BindView(R.id.bg_image)
	ImageView bgImage;
	@BindView(R.id.title)
	TextView title;
	@BindView(R.id.type)
	TextView type;
	@BindView(R.id.description)
	TextView description;
	@BindView(R.id.fab)
	FloatingActionButton mFab;
	@BindView(R.id.toolbar_layout)
	CollapsingToolbarLayout collToolBar;

	protected static final String ARG_LIVE_LIST_BEAN = "arg_live_list_bean";
	private LiveListBean mLiveListBean;

	public static LiveDetailFragment newInstance(LiveListBean liveListBean){
		Bundle bundle = new Bundle();
		//类对象序列化，传递数据
		bundle.putParcelable(ARG_LIVE_LIST_BEAN,liveListBean);
		LiveDetailFragment fragmentEx = new LiveDetailFragment();
		fragmentEx.setArguments(bundle);
		return fragmentEx;
	}
	@Override
	protected void initInject() {
		getFragmentComponent().inject(this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.video_home_detail_fragment;
	}
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取出数据
		mLiveListBean = getArguments().getParcelable(ARG_LIVE_LIST_BEAN);
	}
	@Override
	protected void initEventAndData() {
		mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00}));

		mToolbar.setTitle("");
		initToolbarNav(mToolbar);

		ImageLoader.loadAll(getActivity(), mLiveListBean.getRoom_src(), mImgDetail);
		title.setText(mLiveListBean.getRoom_name());
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("#").append(mLiveListBean.getGame_name())
				.append(" ")
				.append(" / ")
				.append(" ")
				.append("在线:" + mLiveListBean.getOnline());
		type.setText(stringBuilder.toString());
		description.setText(mLiveListBean.getRoom_name());

		mFab.setOnClickListener(v -> {
			if (mFab.getVisibility() == View.VISIBLE) {
				FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
					@Override
					public void onStartTransform() {
					}

					@Override
					public void onEndTransform() {
						JumpUtils.goToDouyuVideoPlayer(getActivity(), mImgDetail, mLiveListBean);
					}
				}).transformTo(mImgDetail);
			}
		});

		initColor();
		mPresenter.getLiveVideoInfo(mLiveListBean.getRoom_id());
	}

	@Override
	public void SetLiveVideoInfo(TempLiveVideoBean liveVideoInfo) {
		String url = liveVideoInfo.getData().getHls_url();
		mLiveListBean.setUrl(url);
	}

	@Override
	public void error(Throwable throwable) {
		Log.e("oye", "error: ", throwable);
	}

	protected void initToolbarNav(android.support.v7.widget.Toolbar toolbar) {
		toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
		toolbar.setNavigationOnClickListener(v -> {
			_mActivity.onBackPressed();
		});
		toolbar.inflateMenu(R.menu.video_menu);
	}
	int color = 0xffffcc00;

	private void initColor() {

		Glide.with(getContext()).load(mLiveListBean.getRoom_src()).asBitmap().into(new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

				Palette.from(resource).generate(palette -> {
					try {
						color = palette.getDarkMutedSwatch().getRgb();
					} catch (Exception e) {
						LogUtil.d(e.getMessage());
					}
					collToolBar.setContentScrimColor(color);
					mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{color}));

				});

			}
		});
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
