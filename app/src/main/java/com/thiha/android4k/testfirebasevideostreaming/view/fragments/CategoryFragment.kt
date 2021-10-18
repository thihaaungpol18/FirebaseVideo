package com.thiha.android4k.testfirebasevideostreaming.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.thiha.android4k.testfirebasevideostreaming.R
import com.thiha.android4k.testfirebasevideostreaming.models.Collection
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.CategoryAdapter
import com.thiha.android4k.testfirebasevideostreaming.view.adapters.CollectionAdapter

class CategoryFragment : Fragment() {

    private lateinit var tvCategoryName: TextView
    private lateinit var rvCategory: RecyclerView
    private lateinit var collectionAdapter: CategoryAdapter
    private lateinit var db: FirebaseFirestore

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
            tvCategoryName = findViewById(R.id.tv_collection_name)

            db = FirebaseFirestore.getInstance()

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

                    collectionAdapter =
                        CategoryAdapter(onClickInterface = object : CategoryAdapter.OnClick {
                            override fun onClicked(item: Collection) {
                                findNavController().navigate(
                                    CategoryFragmentDirections.actionCategoryFragmentToCategoryDetailFragment(
                                        item
                                    )
                                )
                            }
                        })
                            .also {
                                it.submitList(items)
                            }


                    rvCategory.apply {
                        layoutManager = GridLayoutManager(this@CategoryFragment.context, 3)
                        collectionAdapter.submitList(items)
                        adapter = collectionAdapter
                    }
                }
        }
    }

}