package org.cuieney.videolife.ui.act;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.format.DateUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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
import org.cuieney.videolife.common.lrc.DefaultLrcParser;
import org.cuieney.videolife.common.lrc.LrcRow;
import org.cuieney.videolife.common.lrc.LrcView;
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
import org.cuieney.videolife.ui.widget.AlbumViewPager;
import org.cuieney.videolife.ui.widget.PlayerSeekBar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by daimaren on 2017/11/16
 * 975808394@qq.com
 */

public class PlayCloudMusicActivity extends BaseActivity<CloudMusicHomePresenter> implements CloudMusicHomeContract.View {
	public static final String DATA = "DATA";
	public static final String PLAY_INDEX = "PLAY_INDEX";
	public static final String TRANSITION = "TRANSITION";
	public final static String IMG_TRANSITION = "IMG_TRANSITION";
	private static final int RANDOM_PLAY = 0;
	private static final int CIRCLE_PLAY = 1;
	private static final int SINGLE_PLAY = 2;
	//BindView
	@BindView(R.id.backAlbum)
	ImageView ivBgImg;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.volume_seek)
	SeekBar sbVolumeSeek;
	@BindView(R.id.album_layout)
	FrameLayout flAlbumLayout;
	@BindView(R.id.view_pager)
	AlbumViewPager vpRecord;
	@BindView(R.id.needle)
	ImageView ivNeedle;
	@BindView(R.id.lrcviewContainer)
	RelativeLayout rlLrcViewContainer;
	@BindView(R.id.tragetlrc)
	TextView tvGetLrc;
	@BindView(R.id.lrcview)
	LrcView lrcView;
	@BindView(R.id.music_tool)
	LinearLayout llMusicTool;
	@BindView(R.id.playing_fav)
	ImageView ivFav;
	@BindView(R.id.playing_download)
	ImageView ivDownLoad;
	@BindView(R.id.playing_comment)
	ImageView ivComment;
	@BindView(R.id.playing_more)
	ImageView ivMore;
	@BindView(R.id.playing_current_time)
	TextView tvCurrentTime;
	@BindView(R.id.play_seek)
	PlayerSeekBar sbProgress;
	@BindView(R.id.playing_total_time)
	TextView tvTotalTime;
	@BindView(R.id.playing_mode)
	ImageView ivPlayMode;
	@BindView(R.id.playing_pre)
	ImageView ivPlayPrev;
	@BindView(R.id.playing_play)
	ImageView ivPlayPause;
	@BindView(R.id.playing_next)
	ImageView ivPlayNext;
	@BindView(R.id.playing_playlist)
	ImageView ivPlayList;
	// 复用音乐播放界面，针对不同的数据源
	private CommonMusicBean mCommonMusicBean;
	private List<TracksBean> mCurrentPlayList;
	private boolean mIsTransition;
	private CoverFlowAdapter adapter;
	private FragmentAdapter fAdapter;
	//实现毛玻璃效果
	private BitmapFactory.Options newOpts;
	//唱片旋转动画
	private Fragment fragment;
	private WeakReference<View> viewWeakReference;
	private View activeView;
	private ObjectAnimator needleAnim, rotateAnim;
	private AnimatorSet animatorSet;
	private IjkMediaPlayer player;
	//Activity过渡动画
	private Transition mTransition;
	private ActionBar ab;

	public Handler mHandler;
	public Runnable mRunnable;
	private int mCurrentPlayIndex = 0;
	private boolean isContainue = true;
	private int mode = CIRCLE_PLAY;//默认循环播放
	//暂存播放位置，避免播放暂停时进度条跳动
	private long position = 0;
	private boolean isRunningOnTV = false;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		detectDevice();
	}

	@Override
	protected void initInject() {
		getActivityComponent().inject(this);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_play_cloud_music;
	}

	@Override
	protected void initEventAndData() {
		mCommonMusicBean = getIntent().getExtras().getParcelable(PlayMusicActivityEx.DATA);
		mCurrentPlayIndex = getIntent().getIntExtra(PlayMusicActivityEx.PLAY_INDEX, 0);
		mIsTransition = getIntent().getBooleanExtra(TRANSITION, false);
		initGallery();
		initListener();
		initMeida();
		initAnim();
		initLrcView();
		initLrc();
		//如果是运行在TV上，要重新设置needle的layout_marginRightPercent为33
	}

	private void initGallery() {
		final List<TracksBean> nowPlayList = mCommonMusicBean.getTracks();

		if (nowPlayList == null || nowPlayList.size() == 0) {
			return;
		}
		this.mCurrentPlayList = nowPlayList;

		fAdapter = new PlayCloudMusicActivity.FragmentAdapter(getSupportFragmentManager());
		vpRecord.setAdapter(fAdapter);
		vpRecord.setPageTransformer(true, new PlayCloudMusicActivity.PlaybarPagerTransformer());
		vpRecord.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(final int position) {
				if (position < mCurrentPlayIndex)
					changeMusic(true);
				else if (position > mCurrentPlayIndex)
					changeMusic(false);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_IDLE) {

				}
			}
		});
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			ab = getSupportActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setHomeAsUpIndicator(R.drawable.actionbar_back);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
		ab.setTitle(nowPlayList.get(mCurrentPlayIndex).getSongname());
		ab.setSubtitle(nowPlayList.get(mCurrentPlayIndex).getSonger());
	}

	private void initListener() {
		final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		sbVolumeSeek.setMax(streamMaxVolume);
		sbVolumeSeek.setProgress(streamVolume);
		sbVolumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,AudioManager.ADJUST_SAME);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					progress = (int) (progress * player.getDuration() / 100);
					player.seekTo((long) progress);
					tvCurrentTime.setText(DateUtils.formatElapsedTime(progress / 1000));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				player.pause();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//如果已经暂停，界面恢复播放状态
				if (!player.isPlaying())
					ivPlayPause.setImageResource(R.drawable.play_rdi_btn_pause);
				double time = player.getDuration() * (seekBar.getProgress() * 0.01);
				player.seekTo((long) time);
			}
		});
		ivPlayPause.setOnClickListener(v -> {
			//增加暂停播放时动画控制
			if(rotateAnim == null)
				initRotateAnim();
			if (player.isPlaying()) {
				ivPlayPause.setImageResource(R.drawable.play_rdi_btn_play);
				player.pause();
				if (rotateAnim != null)
					rotateAnim.pause();
				if (needleAnim != null) {
					needleAnim.reverse();
				}
			} else {
				player.start();
				ivPlayPause.setImageResource(R.drawable.play_rdi_btn_pause);
				if (rotateAnim != null)
					rotateAnim.resume();
				if (needleAnim != null) {
					needleAnim.start();
				}
			}
		});

		ivPlayMode.setOnClickListener(v -> {
			switch (mode) {
				case RANDOM_PLAY:
					mode = CIRCLE_PLAY;
					ivPlayMode.setImageResource(R.drawable.play_icn_loop);
					break;
				case CIRCLE_PLAY:
					mode = SINGLE_PLAY;
					ivPlayMode.setImageResource(R.drawable.play_icn_one);
					break;
				case SINGLE_PLAY:
					mode = RANDOM_PLAY;
					ivPlayMode.setImageResource(R.drawable.play_icn_shuffle);
					break;
			}
		});

		ivPlayNext.setOnClickListener(v -> {
			changeMusic(false);
		});
		ivPlayPrev.setOnClickListener(v -> {
			changeMusic(true);
		});
		updateProgress();
	}

	private void initMeida() {
		player = new IjkMediaPlayer();
		player.reset();
		player.setOnPreparedListener(iMediaPlayer -> {
			iMediaPlayer.start();
			ivPlayPause.setImageResource(R.drawable.play_rdi_btn_pause);
			tvCurrentTime.setText("00:00");
			//progress reset
			position = 0;
			sbProgress.setProgress(0);
			tvTotalTime.setText(DateUtils.formatElapsedTime(iMediaPlayer.getDuration() / 1000));
		});
		player.setOnErrorListener((iMediaPlayer, i, i1) -> {
			iMediaPlayer.pause();
			return false;
		});
		player.setOnCompletionListener(iMediaPlayer -> {
			changeMusic(false);
		});
		player.setOnSeekCompleteListener(IMediaPlayer::start);
		new Thread(new Runnable() {
			@Override
			public void run() {
				playMusic();
				instantiateItem();
			}
		}).start();
		updateTrackInfo();
		vpRecord.setCurrentItem(mCurrentPlayIndex);
	}

	private void initAnim() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				needleAnim = ObjectAnimator.ofFloat(ivNeedle, "rotation", -30, 0);
				needleAnim.setDuration(60);
				needleAnim.setRepeatMode(0);
				needleAnim.setInterpolator(new LinearInterpolator());
			}
		}).start();
	}

	private void initLrcView() {
		lrcView.setOnSeekToListener(onSeekToListener);
		lrcView.setOnLrcClickListener(onLrcClickListener);
		vpRecord.setOnSingleTouchListener(new AlbumViewPager.OnSingleTouchListener() {
			@Override
			public void onSingleTouch(View v) {
				if (flAlbumLayout.getVisibility() == View.VISIBLE) {
					flAlbumLayout.setVisibility(View.INVISIBLE);
					rlLrcViewContainer.setVisibility(View.VISIBLE);
					llMusicTool.setVisibility(View.INVISIBLE);
				}
			}
		});

		tvGetLrc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initLrc();
			}
		});

	}

	private void initLrc(){
		mPresenter.getLrcInfo("lyric", mCurrentPlayList.get(mCurrentPlayIndex).getId());
	}

	private void detectDevice(){
		isRunningOnTV = App.isRunningOnTV();
		if (isRunningOnTV){
			//获取设备当前方向
			int currentO = getResources().getConfiguration().orientation;
			//强制设置为横屏
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	LrcView.OnLrcClickListener onLrcClickListener = new LrcView.OnLrcClickListener() {
		@Override
		public void onClick() {

			if (rlLrcViewContainer.getVisibility() == View.VISIBLE) {
				rlLrcViewContainer.setVisibility(View.INVISIBLE);
				flAlbumLayout.setVisibility(View.VISIBLE);
				llMusicTool.setVisibility(View.VISIBLE);
			}
		}
	};

	LrcView.OnSeekToListener onSeekToListener = new LrcView.OnSeekToListener() {
		@Override
		public void onSeekTo(int progress) {
			player.seekTo(progress);
		}
	};

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

	@Override
	public void showContent(MusicPlayListBean musicPlayListBean) {

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
	public void showLrcInfo(CloudMusicLrcInfo cloudMusicLrcInfo) {
		List<LrcRow> list = null;
		String lyric = cloudMusicLrcInfo.getLrc().getLyric();
		list = DefaultLrcParser.getIstance().getLrcRows(lyric);
		if (list != null && list.size() > 0) {
			tvGetLrc.setVisibility(View.INVISIBLE);
			lrcView.setLrcRows(list);
		} else {
			tvGetLrc.setVisibility(View.VISIBLE);
			lrcView.reset();
		}
	}

	private List<LrcRow> getLrcRows() {
		List<LrcRow> rows = null;
		InputStream is = null;
		try {
			is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
					"/remusic/lrc/" + mCurrentPlayList.get(mCurrentPlayIndex).getId());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (is == null) {
				return null;
			}
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			// System.out.println(sb.toString());
			rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public void error(Throwable throwable) {

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
					dataSource = imagePipeline.fetchDecodedImage(imageRequest, PlayCloudMusicActivity.this);

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
				drawable = ImageUtils.createBlurredImageFromBitmap(mBitmap, PlayCloudMusicActivity.this.getApplication(), 3);
			}
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			setDrawable(result);
		}

	}

	private void changeMusic(boolean isPlayPrev) {
		if (rotateAnim != null){
			rotateAnim.cancel();
			rotateAnim = null;
		}
		//假设10首歌，index从0到9，切歌方式：手动滑动、上一首、下一首、自动播放
		int index = mCurrentPlayIndex;
		int size = mCurrentPlayList.size();
		switch (mode) {
			case CIRCLE_PLAY:
				if (isPlayPrev)
					index = mCurrentPlayIndex - 1;
				else
					index = mCurrentPlayIndex + 1;
				break;
			case SINGLE_PLAY:
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
		if (index < 0)
			index = mCurrentPlayList.size() - 1;
		else if (index >= size)
			index = 0;
		mCurrentPlayIndex = index;
		new Thread(new Runnable() {
			@Override
			public void run() {
				playMusic();
				instantiateItem();
				initLrc();
			}
		}).start();
		updateTrackInfo();
		vpRecord.setCurrentItem(mCurrentPlayIndex);
	}

	private void updateProgress() {
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (isContainue) {
					mHandler.postDelayed(this, 200);
				}
				runOnUiThread(() -> {
					if (player != null) {
						if (position < player.getCurrentPosition()) {
							position = player.getCurrentPosition();
							int progress = (int) ((position * 1f / player.getDuration() * 1f) * 100);
							if (lrcView != null)
								lrcView.seekTo((int)position, true, false);
							if (sbProgress != null)
								sbProgress.setProgress(progress);
							if (tvCurrentTime != null)
								tvCurrentTime.setText(DateUtils.formatElapsedTime(position / 1000));
						}
					}
				});
			}
		};
		mHandler = new Handler();
		if (isContainue) {
			mHandler.postDelayed(mRunnable, 1000);
		}
	}

	//playMusic功能分解，首先判断当前item播放地址是否为空，如果为空调用获取地址接口，回调函数里做setDataSource和prepareAsync
	private void playMusic() {
		LogUtil.d("mCurrentPlayIndex:" + mCurrentPlayIndex);
		player.reset();
		if (mCurrentPlayList.get(mCurrentPlayIndex).getFilename().equals("")) {
			mPresenter.getMusicInfo("song", mCurrentPlayList.get(mCurrentPlayIndex).getId());
		} else {
			try {
				player.setDataSource(mCurrentPlayList.get(mCurrentPlayIndex).getFilename());
				player.prepareAsync();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateTrackInfo() {
		Log.e("playing", " updateTrackInfo");
		ab.setTitle(mCurrentPlayList.get(mCurrentPlayIndex).getSongname());
		ab.setSubtitle(mCurrentPlayList.get(mCurrentPlayIndex).getSonger());
		new Thread(new Runnable() {
			@Override
			public void run() {
				new PlayCloudMusicActivity.setBlurredAlbumArt().execute();
			}
		}).start();
	}

	public void instantiateItem() {
		fragment = (RoundFragment) vpRecord.getAdapter().instantiateItem(vpRecord, mCurrentPlayIndex);
	}
	private void initRotateAnim(){
		viewWeakReference = new WeakReference<View>(fragment.getView());
		activeView = viewWeakReference.get();
		if (activeView != null) {
			rotateAnim = (ObjectAnimator) activeView.getTag(R.id.tag_animator);
		}
	}
}
