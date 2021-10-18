package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.models.Collection
import com.thiha.android4k.testfirebasevideostreaming.models.Video
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.CollectionAdapter
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.VideoAdapter
import java.util.*
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: CollectionAdapter

    //    private lateinit var videoRV: RecyclerView
    private lateinit var tvCollection: TextView
    private lateinit var rvPopular: RecyclerView
    private lateinit var recentAdapter: VideoAdapter
    private lateinit var collectionViewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recentList = arrayListOf<Video>()

        view.apply {

            db = FirebaseFirestore.getInstance()
//            videoRV = findViewById(R.id.rvVideos)
            tvCollection = findViewById(R.id.tv_collection_name)
            rvPopular = findViewById(R.id.rvPopular)
            collectionViewPager = findViewById(R.id.idViewPagerForCollection)

            tvCollection.text = getString(R.string.category)

            db.collection("collections").get()
                .addOnSuccessListener { documents ->
                    val items = arrayListOf<Collection>()
                    for (document in documents) {
                        items.add(
                            Collection(
                                name = document.data["name"].toString(),
                                thumbnail = document.data["thumbnail"].toString()
                            )
                        )
                    }

                    myAdapter =
                        CollectionAdapter(onClickInterface = object : CollectionAdapter.OnClick {
                            override fun onClicked(item: Collection) {
                                findNavController().navigate(
                                    HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(
                                        item
                                    )
                                )
                            }
                        })
                            .also {
                                it.submitList(items)
                            }

                    collectionViewPager.adapter = myAdapter

                    var currentPage = 0

                    val handler = Handler()

                    val runnable = Runnable {
                        if (currentPage == items.size) {
                            currentPage = 0
                        }
                        collectionViewPager.setCurrentItem(currentPage++, true)
                    }

                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            handler.post(runnable)
                        }
                    }, 500, 2500)

                    val compositePageTransformer = CompositePageTransformer()
                    compositePageTransformer.addTransformer(MarginPageTransformer(40))
                    compositePageTransformer.addTransformer { page, position ->
                        val r = 1 - abs(position)
                        page.scaleY = (0.85f + r * 0.15f)
                    }

                    collectionViewPager.setPageTransformer(compositePageTransformer)
                    collectionViewPager.apply {
                        offscreenPageLimit = 3
                        clipToPadding = false
                        clipChildren = false
                    }

                    collectionViewPager.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            currentPage = position
                        }
                    })

                    TabLayoutMediator(
                        findViewById(R.id.tab_layout),
                        collectionViewPager
                    ) { _, _ -> }.attach()

                }
                .addOnFailureListener {
                    Toast.makeText(this.context, "Fail", Toast.LENGTH_SHORT).show()
                }
            db.collection("collections").document("Myanmar").collection("videos").limit(1).get()
                .addOnSuccessListener { burmese ->
                    for (i in burmese) {
                        recentList.add(
                            Video(
                                title = i.data["title"].toString(),
                                url = i.data["url"].toString(),
                                thumbnail = i.data["thumbnail"].toString(),
                                categoryName = i.data["category"].toString()
                            )
                        )
                    }


                    db.collection("collections").document("Japan").collection("videos").limit(1)
                        .get()
                        .addOnSuccessListener { japanese ->
                            for (i in japanese) {
                                recentList.add(
                                    Video(
                                        title = i.data["title"].toString(),
                                        url = i.data["url"].toString(),
                                        thumbnail = i.data["thumbnail"].toString(),
                                        categoryName = i.data["category"].toString()
                                    )
                                )
                            }

                            db.collection("collections").document("Chinese").collection("videos")
                                .limit(1).get()
                                .addOnSuccessListener { chineses ->
                                    for (i in chineses) {
                                        recentList.add(
                                            Video(
                                                title = i.data["title"].toString(),
                                                url = i.data["url"].toString(),
                                                thumbnail = i.data["thumbnail"].toString(),
                                                categoryName = i.data["category"].toString()
                                            )
                                        )
                                    }

                                    db.collection("collections").document("English")
                                        .collection("videos").limit(1).get()
                                        .addOnSuccessListener { english ->
                                            for (i in english) {
                                                recentList.add(
                                                    Video(
                                                        title = i.data["title"].toString(),
                                                        url = i.data["url"].toString(),
                                                        thumbnail = i.data["thumbnail"].toString(),
                                                        categoryName = i.data["category"].toString()
                                                    )
                                                )
                                            }


                                            recentAdapter = VideoAdapter(onClickInterface = object :
                                                VideoAdapter.OnClick {
                                                override fun onClicked(item: Video) {
                                                    findNavController().navigate(
                                                        HomeFragmentDirections.actionHomeFragmentToVideoFragment(
                                                            item
                                                        )
                                                    )
                                                }
                                            })
                                                .also { adapter ->
                                                    adapter.submitList(recentList)
                                                }
                                            rvPopular.layoutManager =
                                                LinearLayoutManager(
                                                    this.context,
                                                    LinearLayoutManager.HORIZONTAL,
                                                    false
                                                )
                                            rvPopular.adapter = recentAdapter

                                        }

                                }
                        }
                }
        }
    }
}