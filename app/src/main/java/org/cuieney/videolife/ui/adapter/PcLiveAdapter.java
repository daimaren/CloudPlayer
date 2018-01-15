package org.cuieney.videolife.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseRecycerViewAdapter;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.entity.LiveBean.LiveListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cuieney on 17/2/24.
 */

public class PcLiveAdapter extends BaseRecycerViewAdapter<LiveListBean, RecyclerView.ViewHolder> {

	public List<Bitmap> mBitmap;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	public PcLiveAdapter(Context context, List<LiveListBean> list) {
		super(context, list);
		mBitmap = new ArrayList<>();
	}

	@Override
	public int getItemViewType(int position) {
		return R.layout.video_item;
	}

	@Override
	public RecyclerView.ViewHolder getCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == R.layout.video_item) {
			return new MyHolder(inflater.inflate(viewType, parent, false));
		}
		return new TopHolder(inflater.inflate(viewType, parent, false));
	}

	@Override
	public void getBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		LiveListBean liveAllList = list.get(position);
		if (holder instanceof MyHolder) {
			MyHolder myHolder = (MyHolder) holder;
			ViewCompat.setTransitionName(myHolder.imageView, String.valueOf(position) + "_image");
			// 处理数据,显示UI
			ImageLoader.loadAllWithCache(context, liveAllList.getRoom_src(), myHolder.imageView);
			myHolder.itemView.setOnClickListener(v -> {
				if (mClickListener != null) {
					mClickListener.onItemClick(position, v, myHolder);
				}
			});
			myHolder.textView.setText(liveAllList.getRoom_name());
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("#").append(liveAllList.getGame_name())
					.append(" ")
					.append(" / ")
					.append(" ")
					.append("在线:" + liveAllList.getOnline());
			myHolder.description.setText(stringBuilder.toString());
		}
	}
	public static class MyHolder extends RecyclerView.ViewHolder {
		public TextView textView;
		public TextView description;
		public ImageView imageView;

		public MyHolder(View itemView) {
			super(itemView);
			textView = ((TextView) itemView.findViewById(R.id.title));
			description = ((TextView) itemView.findViewById(R.id.description));
			imageView = ((ImageView) itemView.findViewById(R.id.img));
		}
	}

	public static class TopHolder extends RecyclerView.ViewHolder {
		private TextView textView;

		public TopHolder(View itemView) {
			super(itemView);
			textView = ((TextView) itemView.findViewById(R.id.name));
		}
	}

}
