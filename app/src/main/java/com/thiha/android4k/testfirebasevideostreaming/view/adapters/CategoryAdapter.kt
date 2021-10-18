package com.thiha.android4k.testfirebasevideostreaming.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.models.Collection


class CategoryAdapter(val onClickInterface: OnClick) :
    ListAdapter<Collection, CategoryAdapter.VideoViewHolder>(object :
        DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.name == newItem.name && oldItem.thumbnail == newItem.thumbnail
        }
    }) {
    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Collection) {
            Glide.with(itemView.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.download)
                .error(R.drawable.error)
                .thumbnail(Glide.with(itemView.context).load(item.thumbnail))
                .into(itemView.findViewById(R.id.ivItem))
            itemView.findViewById<TextView>(R.id.tvName).text = item.name

            itemView.setOnClickListener {
                onClickInterface.onClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return VideoViewHolder(v)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    interface OnClick {
        fun onClicked(item: Collection)
    }

}