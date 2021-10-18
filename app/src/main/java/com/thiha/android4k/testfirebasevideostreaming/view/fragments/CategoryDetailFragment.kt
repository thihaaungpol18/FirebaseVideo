package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.models.Video
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.VideoAdapter

class CategoryDetailFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: VideoAdapter
    private lateinit var videoRV: RecyclerView

    val args: CategoryDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {

            db = FirebaseFirestore.getInstance()
            videoRV = findViewById(R.id.rvCateogry)
            args.collection.also {
                activity?.actionBar?.title = it.name

                db.collection("collections").document(it.name).collection("videos").get()
                    .addOnSuccessListener { documents ->
                        val items = arrayListOf<Video>()
                        for (document in documents) {
                            items.add(
                                Video(
                                    title = document.data["title"].toString(),
                                    url = document.data["url"].toString(),
                                    thumbnail = document.data["thumbnail"].toString(),
                                    categoryName = document.data["category"].toString()
                                )
                            )
                        }
                        myAdapter = VideoAdapter(onClickInterface = object : VideoAdapter.OnClick {
                            override fun onClicked(item: Video) {
                                findNavController().navigate(

                                    CategoryDetailFragmentDirections.actionCategoryDetailFragmentToVideoFragment (
                                            item
                                            )
                                )
                            }
                        })
                            .also { adapter ->
                                adapter.submitList(items)
                            }
                        videoRV.layoutManager = GridLayoutManager(this.context, 3)
                        videoRV.adapter = myAdapter

                    }
                    .addOnFailureListener {
                        Toast.makeText(this.context, "Fail", Toast.LENGTH_SHORT).show()
                    }
            }

        }

    }


}