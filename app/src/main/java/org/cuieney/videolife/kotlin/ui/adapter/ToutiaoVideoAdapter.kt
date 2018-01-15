package org.cuieney.videolife.kotlin.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cuieney.videolife.R
import org.cuieney.videolife.entity.ToutiaoBean.ToutiaoItemListBean

/**
 * Created by cuieney on 2017/6/2.
 *
 */
class ToutiaoVideoAdapter(t: List<ToutiaoItemListBean>) : BaseAdapter<ToutiaoItemListBean>(t) {


    override fun getBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyHolder) {
            holder.bindData(items[position],position)
            holder.itemView.setOnClickListener({ v ->
                if (clickListener != null) {
                    clickListener?.onItemClick(position, v, holder)
                }
            })
        }
    }

    override fun getCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.video_item) {
            return MyHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
        } else {
            return TopHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
        }
    }


    override fun getItemViewType(position: Int): Int {
            return R.layout.video_item
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(data: ToutiaoItemListBean,position: Int) {

        }
    }


    class TopHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}