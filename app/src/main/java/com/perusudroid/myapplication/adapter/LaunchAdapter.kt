package com.perusudroid.myapplication.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.perusudroid.myapplication.R
import com.perusudroid.myapplication.adapter.listener.IBaseListener
import com.perusudroid.myapplication.model.VideoListResponse

class LaunchAdapter(list : List<VideoListResponse>, val listener : IBaseListener) : RecyclerView.Adapter<LaunchAdapter.ViewHolder>() {

    private var mList : List<VideoListResponse> = list

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LaunchAdapter.ViewHolder {
        return LaunchAdapter.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.inflater_video_list, viewGroup, false), listener)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(viewHolder: LaunchAdapter.ViewHolder, position: Int) {
        viewHolder.bindData(mList[position])
    }

    class ViewHolder(itemView: View, listener: IBaseListener) : RecyclerView.ViewHolder(itemView){

        private var ivBg : ImageView = itemView.findViewById(R.id.ivPic)
        private var tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        private var tvDesc : TextView = itemView.findViewById(R.id.tvDesc)
        private var rootLay : CardView = itemView.findViewById(R.id.rootLay)


        init {
            rootLay.setOnClickListener {
               listener.onClick(itemView.tag as VideoListResponse, adapterPosition)
            }
        }

        fun bindData(videoListResponse: VideoListResponse) {
            itemView.tag = videoListResponse
            tvTitle.text = videoListResponse.title
            tvDesc.text = videoListResponse.description

            Glide.with(itemView.context)
                    .load(videoListResponse.thumb)
                    .centerCrop()
                    .into(ivBg)

        }

    }

}