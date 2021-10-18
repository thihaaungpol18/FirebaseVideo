package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.models.Video
import com.thiha.android4k.testfirebasevideostreaming.view.activity.FullscreenVideoActivity
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.VideoAdapter

class VideoFragment : Fragment(), VideoAdapter.OnClick {

    private val args: VideoFragmentArgs by navArgs()

    private lateinit var playFAB: FloatingActionButton
    private lateinit var ivVidItem: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var catTvVideo: TextView
    private lateinit var tvVideo: TextView

    private lateinit var rvMore: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            ivVidItem = findViewById(R.id.ivVideo)
            tvVideo = findViewById(R.id.titleText)
            catTvVideo = findViewById(R.id.categoryText)
            playFAB = findViewById(R.id.playBtnFAB)
            rvMore = findViewById(R.id.rvMore)
            db = FirebaseFirestore.getInstance()
            initializeViews()
            initializeMoreVideosRV()
        }
    }

    private fun initializeMoreVideosRV() {

        rvMore.apply {
            layoutManager = LinearLayoutManager(
                this@VideoFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = VideoAdapter(this@VideoFragment).also { adapter ->
                db.collection("collections").document(args.video.categoryName).collection("videos")
                    .get()
                    .addOnSuccessListener { items ->
                        val list = arrayListOf<Video>()
                        for (i in items) {
                            list.add(
                                Video(
                                    title = i.data["title"].toString(),
                                    url = i.data["url"].toString(),
                                    thumbnail = i.data["thumbnail"].toString(),
                                    categoryName = i.data["category"].toString()
                                )
                            )
                        }
                        adapter.submitList(list)
                    }

            }
        }
    }

    private fun initializeViews() {
        args.video.also {
            Glide.with(view?.context!!)
                .load(it.thumbnail)
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .into(ivVidItem)
            tvVideo.text = it.title
            catTvVideo.text = it.categoryName
            ivVidItem.animation = AnimationUtils.loadAnimation(view?.context, R.anim.zoom_in)
            ivVidItem.animate()
            playFAB.setOnClickListener {
                val intent = Intent(this.activity, FullscreenVideoActivity::class.java)
                intent.putExtra("url", args.video.url)
                startActivity(intent)
            }
        }
    }

    override fun onClicked(item: Video) {
        findNavController().navigate(VideoFragmentDirections.actionVideoFragmentSelf(item))
    }
}