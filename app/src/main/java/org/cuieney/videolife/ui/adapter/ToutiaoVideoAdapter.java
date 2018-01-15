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
import org.cuieney.videolife.common.utils.DateUtil;
import org.cuieney.videolife.entity.ToutiaoBean.MultiNewsArticleDataBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cuieney on 17/2/24.
 */

public class ToutiaoVideoAdapter extends BaseRecycerViewAdapter<MultiNewsArticleDataBean, RecyclerView.ViewHolder> {

	public List<Bitmap> mBitmap;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	public ToutiaoVideoAdapter(Context context, List<MultiNewsArticleDataBean> list) {
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
		MultiNewsArticleDataBean itemListBean = list.get(position);
		if (holder instanceof MyHolder) {
			MyHolder myHolder = (MyHolder) holder;
			ViewCompat.setTransitionName(myHolder.imageView, String.valueOf(position) + "_image");
			// 处理数据,显示UI
			ImageLoader.loadAllWithCache(context, itemListBean.getVideo_detail_info().getDetail_video_large_image().getUrl(), myHolder.imageView);
			myHolder.itemView.setOnClickListener(v -> {
				if (mClickListener != null) {
					mClickListener.onItemClick(position, v, myHolder);
				}
			});
			myHolder.textView.setText(itemListBean.getTitle());
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("#").append(itemListBean.getSource())
					.append(" ")
					.append(" / ")
					.append(" ")
					.append(DateUtil.formatTime2(itemListBean.getVideo_duration()));
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
