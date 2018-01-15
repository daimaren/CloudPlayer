package org.cuieney.videolife.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseRecycerViewAdapter;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicItemBean;

import java.util.List;

/**
 * Created by cuieney on 17/3/4.
 */

public class CloudMusicAdapter extends BaseRecycerViewAdapter<CloudMusicItemBean, RecyclerView.ViewHolder> {
    public CloudMusicAdapter(Context context, List<CloudMusicItemBean> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder getCreateViewHolder(ViewGroup parent, int viewType) {
        return new CloudMusicAdapter.MyViewHoler(inflater.inflate(viewType, parent, false));
    }

    @Override
    public void getBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHoler) {
            MyViewHoler viewHoler = (MyViewHoler) holder;
            CloudMusicItemBean cloudMusicItemBean = list.get(position);

            viewHoler.num.setText((position+1)+"");
            viewHoler.title.setText(cloudMusicItemBean.getName());
            viewHoler.dis.setText(cloudMusicItemBean.getAr().get(0).getName());
            viewHoler.itemView.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onItemClick(position,v,holder);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.music_detail_item;
    }

    public static class MyViewHoler extends RecyclerView.ViewHolder {
        private final TextView num;
        private final TextView title;
        private final TextView dis;
        private final ImageView playIcon;

        public MyViewHoler(View itemView) {
            super(itemView);
            num = ((TextView) itemView.findViewById(R.id.title_num));
            title = ((TextView) itemView.findViewById(R.id.title));
            dis = ((TextView) itemView.findViewById(R.id.title_dis));
            playIcon = ((ImageView) itemView.findViewById(R.id.songdetail_play));
        }
    }
}
