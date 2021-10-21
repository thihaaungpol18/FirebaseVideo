package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.data.Source
import com.thiha.android4k.testfirebasevideostreaming.models.Collection
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.CategoryAdapter

class CategoryFragment : Fragment(), CategoryAdapter.OnClick {

    private lateinit var rvCategory: RecyclerView
    private lateinit var collectionAdapter: CategoryAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            rvCategory = findViewById(R.id.rvCateogry)
            shimmerLayout = findViewById(R.id.shimmer_layout)
            shimmerLayout.startShimmer()
            rvCategory.visibility = View.GONE

            Source.getCollections().observe(viewLifecycleOwner) { items ->
                collectionAdapter =
                    CategoryAdapter(this@CategoryFragment)
                        .also {
                            it.submitList(items)
                            if (items.isNotEmpty()) {
                                shimmerLayout.visibility = View.GONE
                                rvCategory.visibility = View.VISIBLE
                            }
                        }
                rvCategory.apply {
                    layoutManager = GridLayoutManager(this@CategoryFragment.context, 2)
                    adapter = collectionAdapter
                }
            }
        }
    }

    override fun onClicked(item: Collection) {
        findNavController().navigate(
            CategoryFragmentDirections.actionCategoryFragmentToCategoryDetailFragment(
                item
            )
        )
    }
}