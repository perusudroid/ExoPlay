package com.perusudroid.exoplay.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.perusudroid.exoplay.R
import com.perusudroid.exoplay.adapter.listener.IBaseListener
import com.perusudroid.exoplay.model.VideoListResponse

class DetailAdapter(list : List<VideoListResponse>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private var mList : List<VideoListResponse> = list

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DetailAdapter.ViewHolder {
        return DetailAdapter.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.inflater_detail_list, viewGroup, false))
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(viewHolder: DetailAdapter.ViewHolder, position: Int) {
        viewHolder.bindData(mList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var ivPic : ImageView = itemView.findViewById(R.id.ivPic)
        private var tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        private var tvDesc : TextView = itemView.findViewById(R.id.tvDesc)



        fun bindData(videoListResponse: VideoListResponse) {
            itemView.tag = videoListResponse
            tvTitle.text = videoListResponse.title
            tvDesc.text = videoListResponse.description

            Glide.with(itemView.context)
                    .load(videoListResponse.thumb)
                    .centerCrop()
                    .into(ivPic)

        }

    }

}