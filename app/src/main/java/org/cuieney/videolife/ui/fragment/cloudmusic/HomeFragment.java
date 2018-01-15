package org.cuieney.videolife.ui.fragment.cloudmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseBackFragment;
import org.cuieney.videolife.common.image.ImageLoader;

import static org.cuieney.videolife.common.utils.Constants.MY_FAVORITE_MUSIC_BG_URL;
import static org.cuieney.videolife.common.utils.Constants.MY_FAVORITE_MUSIC_ID;
import static org.cuieney.videolife.common.utils.Constants.MY_FAVORITE_MUSIC_NAME;
import static org.cuieney.videolife.common.utils.Constants.NICE_MUSIC_BG_URL;
import static org.cuieney.videolife.common.utils.Constants.NICE_MUSIC_ID;
import static org.cuieney.videolife.common.utils.Constants.NICE_MUSIC_NAME;
import static org.cuieney.videolife.common.utils.Constants.REALLY_LIKE_MUSIC_BG_URL;
import static org.cuieney.videolife.common.utils.Constants.REALLY_LIKE_MUSIC_ID;
import static org.cuieney.videolife.common.utils.Constants.REALLY_LIKE_MUSIC_NAME;

/**
 * Created by daimaren on 2018/1/8
 * 975808394@qq.com
 * 网易云音乐歌单界面
 */

public class HomeFragment extends BaseBackFragment{

	private static final String TAG = HomeFragment.class.getSimpleName();
	private HoloTilesAdapter mAdapter;
	public static HomeFragment newInstance(){
		HomeFragment homeFragment = new HomeFragment();
		return homeFragment;
	}
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_stream_publish, container,false);
		initView(view);
		return view;
	}
	private void initView(View view){
		GridView gridView = (GridView) view.findViewById(R.id.grid);
		mAdapter = new HoloTilesAdapter();
		gridView.setAdapter(mAdapter);
	}
	public class HoloTilesAdapter extends BaseAdapter {

		private static final int TILES_COUNT = 3;
		private final String[] PLAY_LIST_ID = {
				MY_FAVORITE_MUSIC_ID,
				NICE_MUSIC_ID,
				REALLY_LIKE_MUSIC_ID
		};
		private final String[] PLAY_LIST_BG_URLS = {
				MY_FAVORITE_MUSIC_BG_URL,
				NICE_MUSIC_BG_URL,
				REALLY_LIKE_MUSIC_BG_URL
		};
		private final String[] PLAY_LIST_NAME = {
				MY_FAVORITE_MUSIC_NAME,
				NICE_MUSIC_NAME,
				REALLY_LIKE_MUSIC_NAME
		};

		@Override
		public int getCount() {
			return TILES_COUNT;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardView v;
			if (convertView == null) {
				v = (CardView) getActivity().getLayoutInflater().inflate(R.layout.music_item, parent, false);
			} else {
				v = (CardView) convertView;
			}

			ImageView ivBg = (ImageView) v.findViewById(R.id.img);
			TextView tvTitle = (TextView) v.findViewById(R.id.title);
			TextView tvDesc = (TextView) v.findViewById(R.id.description);

			if(position == 0) {
				//http://music.163.com/#/my/m/music/playlist?id=126102318
				ImageLoader.loadAllWithCache(getContext(),MY_FAVORITE_MUSIC_BG_URL, ivBg);
				tvTitle.setText(MY_FAVORITE_MUSIC_NAME);
				tvDesc.setText("161首歌");
			} else if(position == 1) {
				//http://music.163.com/#/playlist?id=330223665
				ImageLoader.loadAllWithCache(getContext(),NICE_MUSIC_BG_URL, ivBg);
				tvTitle.setText(NICE_MUSIC_NAME);
				tvDesc.setText("247首歌");
			} else if(position == 2) {
				//http://music.163.com/#/my/m/music/playlist?id=916578573
				ImageLoader.loadAllWithCache(getContext(),REALLY_LIKE_MUSIC_BG_URL, ivBg);
				tvTitle.setText(REALLY_LIKE_MUSIC_NAME);
				tvDesc.setText("3首歌");
			} else if(position == 3) {
				//to  be continue
			}

			final int currentPosition = position;
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startChildFragment(PLAY_LIST_ID[currentPosition],PLAY_LIST_BG_URLS[currentPosition],
							PLAY_LIST_NAME[currentPosition]);
				}
			});
			return v;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
	}

	private void startChildFragment(String playListId, String bgUrl, String playListName){
		PlayListFragment fragment = PlayListFragment.newInstance(playListId, bgUrl, playListName);
		start(fragment);
	}
}
