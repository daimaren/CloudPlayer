package org.cuieney.videolife.ui.act;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.transition.Transition;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.cuieney.videolife.App;
import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseActivity;
import org.cuieney.videolife.common.utils.ImageUtils;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicInfo;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicLrcInfo;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;
import org.cuieney.videolife.entity.CommonMusicBean;
import org.cuieney.videolife.entity.wyBean.TracksBean;
import org.cuieney.videolife.presenter.CloudMusicHomePresenter;
import org.cuieney.videolife.presenter.contract.CloudMusicHomeContract;
import org.cuieney.videolife.ui.adapter.CoverFlowAdapter;
import org.cuieney.videolife.ui.fragment.cloudmusic.RoundFragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by daimaren on 2017/11/7
 * 975808394@qq.com
 */

public class PlayMusicActivityEx extends BaseActivity<CloudMusicHomePresenter> implements CloudMusicHomeContract.View{
	public static final String DATA = "DATA";
	public static final String PLAY_INDEX = "PLAY_INDEX";
	public static final String TRANSITION = "TRANSITION";
	public final static String IMG_TRANSITION = "IMG_TRANSITION";
	private static final int RANDOM_PLAY = 0;
	private static final int CIRCLE_PLAY = 1;
	private static final int SINGLE_PLAY = 2;
	@BindView(R.id.needle)
	ImageView ivNeedle;
	@BindView(R.id.backAlbum)
	ImageView ivBgImg;
	@BindView(R.id.back)
	ImageView ivBack;
	@BindView(R.id.play_pause)
	ImageView ivPlayPause;
	@BindView(R.id.model)
	ImageView ivPlayMode;
	@BindView(R.id.next)
	ImageView ivPlayNext;
	@BindView(R.id.share)
	ImageView ivShare;
	@BindView(R.id.headline)
	TextView tvTitle;
	@BindView(R.id.name)
	TextView tvName;
	@BindView(R.id.controller)
	RelativeLayout rlController;
	@BindView(R.id.current_time)
	TextView tvStart;
	@BindView(R.id.progress)
	SeekBar tvSeekbar;
	@BindView(R.id.total_time)
	TextView tvTotalTime;
	@BindView(R.id.progress_controller)
	LinearLayout llProgressController;
	@BindView(R.id.viewpager)
	ViewPager vpAlbum;
	@BindView(R.id.progressBar1)
	ProgressBar progressBar;

	// 复用音乐播放界面，针对不同的数据源
	private CommonMusicBean mCommonMusicBean;
	private List<TracksBean> mCurrentPlayList;
	private boolean mIsTransition;
	private CoverFlowAdapter adapter;
	private FragmentAdapter fAdapter;
	//实现毛玻璃效果
	private BitmapFactory.Options newOpts;
	//
	private WeakReference<View> viewWeakReference;
	private View activeView;
	private ObjectAnimator needleAnim, rotateAnim;
	private AnimatorSet animatorSet;
	private IjkMediaPlayer player;
	private Transition mTransition;

	public Handler mHandler;
	public Runnable mRunnable;
	private int mCurrentPlayIndex = 0;
	private boolean isContainue = true;
	private int mode = CIRCLE_PLAY;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void initInject() {
		getActivityComponent().inject(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.acitivty_play_music;
	}

	@Override
	protected void initEventAndData() {
		mCommonMusicBean = getIntent().getExtras().getParcelable(PlayMusicActivityEx.DATA);
		mCurrentPlayIndex = getIntent().getIntExtra(PlayMusicActivityEx.PLAY_INDEX, 0);
		mIsTransition = getIntent().getBooleanExtra(TRANSITION, false);
		initGallery();
		initListener();
		initMeida();
		InitAnim();
	}
	private void initMeida() {
		player = new IjkMediaPlayer();
		player.reset();
		player.setOnPreparedListener(iMediaPlayer -> {
			iMediaPlayer.start();
			ivPlayPause.setImageResource(R.drawable.pause_msc_icon);
			tvStart.setText("00:00");
			tvTotalTime.setText(DateUtils.formatElapsedTime(iMediaPlayer.getDuration() / 1000));
		});
		player.setOnErrorListener((iMediaPlayer, i, i1) -> {
			iMediaPlayer.pause();
			return false;
		});
		player.setOnCompletionListener(iMediaPlayer -> {
			changeMusic();
		});
		player.setOnSeekCompleteListener(IMediaPlayer::start);
		vpAlbum.setCurrentItem(mCurrentPlayIndex, false);
	}

	private void changeMusic() {
		int index = mCurrentPlayIndex;
		switch (mode) {
			case CIRCLE_PLAY:
				index += 1;
				break;
			case SINGLE_PLAY:
				playMusic();
				break;
			case RANDOM_PLAY:
				Random random = new Random();
				int anInt = random.nextInt(mCurrentPlayList.size());
				if (anInt == index) {
					anInt = random.nextInt(mCurrentPlayList.size());
				}
				index = anInt;
				break;
		}
		vpAlbum.setCurrentItem(index, true);
	}

	private void initListener() {
		ivBack.setOnClickListener(v -> finish());
		tvSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				player.pause();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//如果已经暂停，界面恢复播放状态
				if (!player.isPlaying())
					ivPlayPause.setImageResource(R.drawable.pause_msc_icon);
				double time = player.getDuration() * (seekBar.getProgress() * 0.01);
				player.seekTo((long) time);
			}
		});
		ivPlayPause.setOnClickListener(v -> {
			//增加暂停播放时动画控制
			if (player.isPlaying()) {
				ivPlayPause.setImageResource(R.drawable.play_msc_icon);
				player.pause();
				if (rotateAnim != null)
					rotateAnim.pause();
			} else {
				player.start();
				ivPlayPause.setImageResource(R.drawable.pause_msc_icon);
				if (rotateAnim != null)
					rotateAnim.resume();
			}
		});

		ivPlayMode.setOnClickListener(v -> {
			switch (mode) {
				case RANDOM_PLAY:
					mode = CIRCLE_PLAY;
					ivPlayMode.setImageResource(R.drawable.circle_icon);
					break;
				case CIRCLE_PLAY:
					mode = SINGLE_PLAY;
					ivPlayMode.setImageResource(R.drawable.single_play_icon);
					break;
				case SINGLE_PLAY:
					mode = RANDOM_PLAY;
					ivPlayMode.setImageResource(R.drawable.random_icon);
					break;
			}
		});

		ivPlayNext.setOnClickListener(v -> {
			changeMusic();
		});
		updateProgress();
	}

	private void updateProgress() {
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (isContainue) {
					mHandler.postDelayed(this, 1000);
				}
				runOnUiThread(() -> {
					if (player != null) {
						int progress = (int) ((player.getCurrentPosition() * 1f / player.getDuration() * 1f) * 100);
						if (tvSeekbar != null)
							tvSeekbar.setProgress(progress);
						if (tvStart != null)
							tvStart.setText(DateUtils.formatElapsedTime(player.getCurrentPosition() / 1000));
					}
				});
			}
		};
		mHandler = new Handler();
		if (isContainue) {
			mHandler.postDelayed(mRunnable, 1000);
		}
	}

	private void initGallery() {
		final List<TracksBean> nowPlayList = mCommonMusicBean.getTracks();

		if (nowPlayList == null || nowPlayList.size() == 0) {
			return;
		}
		this.mCurrentPlayList = nowPlayList;

		//adapter = new CoverFlowAdapter(nowPlayList, this);
		fAdapter = new FragmentAdapter(getSupportFragmentManager());
		vpAlbum.setAdapter(fAdapter);
		//vpAlbum.setOffscreenPageLimit(1);
		vpAlbum.setPageTransformer(true, new PlayMusicActivityEx.PlaybarPagerTransformer());
		tvTitle.setText(nowPlayList.get(mCurrentPlayIndex).getSongname());
		tvName.setText(nowPlayList.get(mCurrentPlayIndex).getSonger());
		vpAlbum.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}
			@Override
			public void onPageSelected(final int position) {
				//假设10首歌，index从0到9，三种方式：手动滑动、下一首、自动播放
				//末尾之后，回到第一个
				if (position >= mCurrentPlayList.size()){
					mCurrentPlayIndex = 0;
				}
				else
					mCurrentPlayIndex = position;
				tvTitle.setText(nowPlayList.get(mCurrentPlayIndex).getSongname());
				tvName.setText(nowPlayList.get(mCurrentPlayIndex).getSonger());
				playMusic();
				new setBlurredAlbumArt().execute();
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {

				}
			}
		});
	}
	private void InitAnim() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				needleAnim = ObjectAnimator.ofFloat(ivNeedle, "rotation", -60, -30);
				needleAnim.setDuration(60);
				needleAnim.setRepeatMode(0);
				needleAnim.setInterpolator(new LinearInterpolator());
			}
		}).start();

	}
	public class PlaybarPagerTransformer implements ViewPager.PageTransformer {
		@Override
		public void transformPage(View view, float position) {
			if (position == 0) {
				rotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
				if (rotateAnim != null && !rotateAnim.isRunning()) {
					animatorSet = new AnimatorSet();
					animatorSet.play(needleAnim).before(rotateAnim);
					animatorSet.start();
				}
			} else if (position == -1 || position == -2 || position == 1) {
				rotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
				if (rotateAnim != null) {
					rotateAnim.setFloatValues(0);
					rotateAnim.end();
					rotateAnim = null;
				}
			} else {
				if (needleAnim != null) {
					needleAnim.reverse();
					needleAnim.end();
				}
				rotateAnim = (ObjectAnimator) view.getTag(R.id.tag_animator);
				if (rotateAnim != null) {
					rotateAnim.cancel();
					float valueAvatar = (float) rotateAnim.getAnimatedValue();
					rotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);
				}
			}
		}
	}
	private void stopAnim() {
		activeView = null;

		if (rotateAnim != null) {
			rotateAnim.end();
			rotateAnim = null;
		}
		if (needleAnim != null) {
			needleAnim.end();
			needleAnim = null;
		}
		if (animatorSet != null) {
			animatorSet.end();
			animatorSet = null;
		}
	}
	public class ScalePageTransformer implements ViewPager.PageTransformer {
		private static final String TAG = "ScalePageTransformer";
		public static final float MAX_SCALE = 1.0f;
		public static final float MIN_SCALE = 0.8f;

		@Override
		public void transformPage(View page, float position) {

			if (position < -1) {
				position = -1;
			} else if (position > 1) {
				position = 1;
			}

			float tempScale = position < 0 ? 1 + position : 1 - position;

			float slope = (MAX_SCALE - MIN_SCALE) / 1;
			//一个公式
			float scaleValue = MIN_SCALE + tempScale * slope;

			//设置缩放比例
			page.setScaleX(scaleValue);
			page.setScaleY(scaleValue);
			//设置透明度
			page.setAlpha(scaleValue);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopAnim();
		App.getInstance().removeActivity(this);
		isContainue = false;
		if (player.isPlaying()) {
			player.pause();
			player.stop();
			player.release();
			player = null;
		}
	}
	//playMusic功能分解，首先判断当前item播放地址是否为空，如果为空调用获取地址接口，回调函数里做setDataSource和prepareAsync
	private void playMusic() {
		LogUtil.d("mCurrentPlayIndex:" + mCurrentPlayIndex);
		player.reset();
		if (mCurrentPlayList.get(mCurrentPlayIndex).getFilename().equals("")){
			mPresenter.getMusicInfo("song", mCurrentPlayList.get(mCurrentPlayIndex).getId());
		}
		else {
			try {
				player.setDataSource(mCurrentPlayList.get(mCurrentPlayIndex).getFilename());
				player.prepareAsync();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void Anim(){
		Fragment fragment = (RoundFragment) vpAlbum.getAdapter().instantiateItem(vpAlbum, vpAlbum.getCurrentItem());
		viewWeakReference = new WeakReference<View>(fragment.getView());
		activeView = viewWeakReference.get();
		if (activeView != null) {
			rotateAnim = (ObjectAnimator) activeView.getTag(R.id.tag_animator);
		}
		animatorSet = new AnimatorSet();
		if (player.isPlaying()) {
			if (rotateAnim != null && !rotateAnim.isRunning()) {
				animatorSet.play(needleAnim).before(rotateAnim);
				animatorSet.start();
			}
		} else {
			if (needleAnim != null) {
				needleAnim.reverse();
				needleAnim.end();
			}
			if (rotateAnim != null && rotateAnim.isRunning()) {
				rotateAnim.cancel();
				float valueAvatar = (float) rotateAnim.getAnimatedValue();
				rotateAnim.setFloatValues(valueAvatar, 360f + valueAvatar);
			}
		}
	}
	@Override
	public void showContent(MusicPlayListBean musicPlayListBean) {

	}

	@Override
	public void showLrcInfo(CloudMusicLrcInfo cloudMusicLrcInfo) {

	}

	@Override
	public void showMusicInfo(CloudMusicInfo cloudMusicInfo) {
		try {
			String url = cloudMusicInfo.getData().get(0).getUrl();
			mCurrentPlayList.get(mCurrentPlayIndex).setFilename(url);
			player.setDataSource(url);
			player.prepareAsync();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(Throwable throwable) {

	}
	class FragmentAdapter extends FragmentStatePagerAdapter {

		private int mChildCount = 0;

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == mCurrentPlayList.size()) {
				return RoundFragment.newInstance("");
			}
			return RoundFragment.newInstance(mCurrentPlayList.get(position).getSongphoto());
		}

		@Override
		public int getCount() {
			return mCurrentPlayList.size();
		}

		@Override
		public void notifyDataSetChanged() {
			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			if (mChildCount > 0) {
				mChildCount--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}

	}

	private void setDrawable(Drawable result) {
		if (result != null) {
			if (ivBgImg.getDrawable() != null) {
				final TransitionDrawable td =
						new TransitionDrawable(new Drawable[]{new ColorDrawable(0xfffcfcfc), result});//fff white

				ivBgImg.setImageDrawable(td);
				//去除过度绘制
				td.setCrossFadeEnabled(true);
				td.startTransition(370);

			} else {
				ivBgImg.setImageDrawable(result);
			}
		}
	}
	private Bitmap mBitmap;

	private class setBlurredAlbumArt extends AsyncTask<Void, Void, Drawable> {
		@Override
		protected Drawable doInBackground(Void... loadedImage) {

			Drawable drawable = null;
			mBitmap = null;
			if (newOpts == null) {
				newOpts = new BitmapFactory.Options();
				newOpts.inSampleSize = 6;
				newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
			}
			ImageRequest imageRequest = ImageRequestBuilder
					.newBuilderWithSource(Uri.parse(mCurrentPlayList.get(mCurrentPlayIndex).getSongphoto()))
					.setProgressiveRenderingEnabled(true)
					.build();

			ImagePipeline imagePipeline = Fresco.getImagePipeline();
			DataSource<CloseableReference<CloseableImage>>
					dataSource = imagePipeline.fetchDecodedImage(imageRequest, PlayMusicActivityEx.this);

			dataSource.subscribe(new BaseBitmapDataSubscriber() {
									 @Override
									 public void onNewResultImpl(@Nullable Bitmap bitmap) {
										 // You can use the bitmap in only limited ways
										 // No need to do any cleanup.
										 if (bitmap != null) {
											 mBitmap = bitmap;
											 LogUtil.d("getalbumpath bitmap success");
										 }
									 }

									 @Override
									 public void onFailureImpl(DataSource dataSource) {
										 // No cleanup required here.
										 LogUtil.d("getalbumpath bitmap failed");
										 mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);

									 }
								 },
					CallerThreadExecutor.getInstance());
			if (mBitmap != null) {
				drawable = ImageUtils.createBlurredImageFromBitmap(mBitmap, PlayMusicActivityEx.this.getApplication(), 3);
			}
			return drawable;
		}
		@Override
		protected void onPostExecute(Drawable result) {
			setDrawable(result);
		}

	}
}
