package org.cuieney.videolife.ui.fragment.video;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.konifar.fab_transformation.FabTransformation;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseBackFragment;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.common.utils.DateUtil;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.ToutiaoBean.MultiNewsArticleDataBean;
import org.cuieney.videolife.ui.video.JumpUtils;


/**
 * Created by cuieney on 17/2/25.
 */

public class ToutiaoVideoDetailFragment extends BaseBackFragment {

	ImageView mImgDetail;
	Toolbar mToolbar;
	ImageView bgImage;
	TextView title;
	TextView type;
	TextView description;
	FloatingActionButton mFab;

	private MultiNewsArticleDataBean videoBean;
	private CollapsingToolbarLayout collToolBar;


	public static ToutiaoVideoDetailFragment newInstance(MultiNewsArticleDataBean videoBean) {
		Bundle args = new Bundle();
		args.putParcelable(ARG_ITEM, videoBean);
		ToutiaoVideoDetailFragment fragment = new ToutiaoVideoDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		videoBean = getArguments().getParcelable(ARG_ITEM);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.video_home_detail_fragment, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mToolbar = ((Toolbar) view.findViewById(R.id.toolbar));
		collToolBar = ((CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout));
		mImgDetail = (ImageView) view.findViewById(R.id.img_detail);
		bgImage = (ImageView) view.findViewById(R.id.bg_image);
		title = (TextView) view.findViewById(R.id.title);
		type = (TextView) view.findViewById(R.id.type);
		description = (TextView) view.findViewById(R.id.description);
		mFab = (FloatingActionButton) view.findViewById(R.id.fab);


		mFab.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00}));

		mToolbar.setTitle("");
		initToolbarNav(mToolbar);

		ImageLoader.loadAll(getActivity(), videoBean.getVideo_detail_info().getDetail_video_large_image().getUrl(), mImgDetail);
		//ImageLoader.loadAll(getActivity(), videoBean.getVideo_detail_info().getDetail_video_large_image().getUrl(), bgImage);
		title.setText(videoBean.getTitle());
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("#").append(videoBean.getSource())
				.append(" ")
				.append(" / ")
				.append(" ")
				.append(DateUtil.formatTime2(videoBean.getVideo_duration()));
		type.setText(stringBuilder.toString());
		description.setText(videoBean.getAbstractX());

		mFab.setOnClickListener(v -> {
			if (mFab.getVisibility() == View.VISIBLE) {
				FabTransformation.with(mFab).setListener(new FabTransformation.OnTransformListener() {
					@Override
					public void onStartTransform() {
					}

					@Override
					public void onEndTransform() {
						JumpUtils.goToToutiaoVideoPlayer(getActivity(), mImgDetail, videoBean);
					}
				}).transformTo(mImgDetail);
			}
		});

		initColor();

	}

	int color = 0xffffcc00;

	private void initColor() {

		Glide.with(getContext()).load(videoBean.getVideo_detail_info().getDetail_video_large_image().getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
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
