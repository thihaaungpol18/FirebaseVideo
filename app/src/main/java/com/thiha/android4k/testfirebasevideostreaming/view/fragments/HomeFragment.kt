package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.data.Source
import com.thiha.android4k.testfirebasevideostreaming.models.Collection
import com.thiha.android4k.testfirebasevideostreaming.models.Video
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.CollectionAdapter
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.VideoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.math.abs

class HomeFragment : Fragment(), CollectionAdapter.OnClick {

    private lateinit var myAdapter: CollectionAdapter

    private lateinit var rvPopular: RecyclerView
    private lateinit var recentAdapter: VideoAdapter
    private lateinit var collectionViewPager: ViewPager2
    private lateinit var shimmerLayoutCollection: ShimmerFrameLayout
    private lateinit var shimmerLayoutCollection2: ShimmerFrameLayout
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            //Views Initialization
            rvPopular = findViewById(R.id.rvPopular)
            collectionViewPager = findViewById(R.id.idViewPagerForCollection)
            shimmerLayoutCollection = findViewById(R.id.shimmer_layout_collection)
            shimmerLayoutCollection2 = findViewById(R.id.shimmer_layout_collection2)
            tabLayout = findViewById(R.id.tab_layout)

            myAdapter =
                CollectionAdapter(this@HomeFragment)
            collectionViewPager.adapter = myAdapter

            startShimmer()
            startShimmer2()

            //Get All Categories
            Source.getCollections().observe(this@HomeFragment.viewLifecycleOwner) { items ->
                if (items.isNotEmpty()) {
                    stopShimmer()
                    showCollectionViewPagerAndTabLayout()
                }
                myAdapter.submitList(items)
//              ViewPager Auto Slide Feature
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

            }

            Source.getOneVideoOfEachCategory()
                .observe(this@HomeFragment.viewLifecycleOwner) { items ->

                    if (items.isNotEmpty()) {
                        stopShimmer2()
                        showRecentRV()
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
                            adapter.submitList(items)
                        }

                    rvPopular.adapter = recentAdapter
                    rvPopular.layoutManager =
                        LinearLayoutManager(
                            this.context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                }

        }
    }

    private fun startShimmer() {
        shimmerLayoutCollection.showShimmer(true)
        shimmerLayoutCollection.startShimmer()
    }

    private fun startShimmer2() {
        shimmerLayoutCollection2.showShimmer(true)
        shimmerLayoutCollection2.startShimmer()
    }

    private fun showCollectionViewPagerAndTabLayout() {
        collectionViewPager.visibility = View.VISIBLE
        tabLayout.visibility = View.VISIBLE
        shimmerLayoutCollection.visibility = View.INVISIBLE
    }

    private fun showRecentRV() {
        rvPopular.visibility = View.VISIBLE
        shimmerLayoutCollection2.visibility = View.INVISIBLE
    }

    private fun stopShimmer() {
        shimmerLayoutCollection.hideShimmer()
        shimmerLayoutCollection.stopShimmer()
    }

    private fun stopShimmer2() {
        shimmerLayoutCollection2.hideShimmer()
        shimmerLayoutCollection2.stopShimmer()
    }

    override fun onClicked(item: Collection) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(
                item
            )
        )
    }

}