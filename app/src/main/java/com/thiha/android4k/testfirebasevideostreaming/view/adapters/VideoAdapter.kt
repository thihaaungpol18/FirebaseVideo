package com.thiha.android4k.testfirebasevideostreaming.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.models.Video


class VideoAdapter(val onClickInterface: OnClick) :
    ListAdapter<Video, VideoAdapter.VideoViewHolder>(object : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.title == newItem.title && oldItem.url == newItem.url
        }
    }) {
    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Video) {
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.download)
                .error(R.drawable.error)
                .thumbnail(Glide.with(itemView.context).load(item.thumbnail))
                .into(itemView.findViewById(R.id.ivItem))

            itemView.findViewById<TextView>(R.id.tvName).text = item.title
            itemView.findViewById<TextView>(R.id.tvCategoryName).text = item.categoryName

            itemView.setOnClickListener {
                onClickInterface.onClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(v)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    interface OnClick {
        fun onClicked(item: Video)
    }

}