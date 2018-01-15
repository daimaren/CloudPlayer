package org.cuieney.videolife.ui.fragment.stream;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseBackFragment;
import org.cuieney.videolife.ui.act.LandscapeActivity;
import org.cuieney.videolife.ui.act.PartActivity;
import org.cuieney.videolife.ui.act.PortraitActivity;
import org.cuieney.videolife.ui.act.ScreenActivity;

/**
 * Created by daimaren on 2017/11/8
 * 975808394@qq.com
 */

public class StreamPublishHomeFragment extends BaseBackFragment{
	public static StreamPublishHomeFragment newInstance(){
		StreamPublishHomeFragment publishHomeFragment = new StreamPublishHomeFragment();
		return publishHomeFragment;
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
		gridView.setAdapter(new HoloTilesAdapter());
	}
	public class HoloTilesAdapter extends BaseAdapter {

		private static final int TILES_COUNT = 4;

		private final int[] DRAWABLES = {
				R.drawable.blue_tile,
				R.drawable.green_tile,
				R.drawable.purple_tile,
				R.drawable.yellow_tile
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
			RelativeLayout v;
			if (convertView == null) {
				v = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.grid_item, parent, false);
			} else {
				v = (RelativeLayout) convertView;
			}
			v.setBackgroundResource(DRAWABLES[position % 5]);

			TextView textView1 = (TextView) v.findViewById(R.id.textView1);
			TextView textView2 = (TextView) v.findViewById(R.id.textView2);

			String string1 = "", string2 = "";
			if(position == 0) {
				string1 = "Portrait";
				string2 = "Flv + Local";
			} else if(position == 1) {
				string1 = "Landscape";
				string2 = "Rtmp";
			} else if(position == 2) {
				string1 = "Portrait";
				string2 = "Part";
			} else if(position == 3) {
				string1 = "Portrait";
				string2 = "Screen + Rtmp";
			}
			textView1.setText(string1);
			textView2.setText(string2);

			final int currentPosition = position;
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(currentPosition == 0) {
						goPortraitAndLocal();
					} else if(currentPosition == 1) {
						goLandscapeAndRtmp();
					} else if(currentPosition == 2) {
						goPart();
					} else if(currentPosition == 3) {
						goScreen();
					}
				}
			});
			return v;
		}
	}
	private void goPortraitAndLocal() {
		//保存成本地flv文件
		Intent intent = new Intent(getActivity(), PortraitActivity.class);
		startActivity(intent);
	}

	private void goLandscapeAndRtmp() {
		//RTMP推流
		Intent intent = new Intent(getActivity(), LandscapeActivity.class);
		startActivity(intent);
	}

	private void goPart() {
		Intent intent = new Intent(getActivity(), PartActivity.class);
		startActivity(intent);
	}

	private void goScreen() {
		//可以修改为录屏直播
		Intent intent = new Intent(getActivity(), ScreenActivity.class);
		startActivity(intent);
	}
	@Override
	public void onResume() {
		super.onResume();
	}
}
