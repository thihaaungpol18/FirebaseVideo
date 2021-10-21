package com.thiha.android4k.testfirebasevideostreaming.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.thiha.android4k.testfirebasevideostreaming.models.Collection
import com.thiha.android4k.testfirebasevideostreaming.models.Video

object Source {
    private val fireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getCollections(): LiveData<ArrayList<Collection>> {
        val mutableLiveData = MutableLiveData<ArrayList<Collection>>()
        fireStore.collection("collections").get()
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
                mutableLiveData.postValue(items)
            }
        return mutableLiveData
    }

    fun getOneVideoOfEachCategory(): LiveData<ArrayList<Video>> {
        val mutableLiveData = MutableLiveData<ArrayList<Video>>()
        val base = fireStore.collection("collections")
        val items = arrayListOf<Video>()

        base.document("Myanmar").collection("videos").limit(1).get()
            .addOnSuccessListener { burmese ->
                for (i in burmese) {
                    items.add(
                        generateVideo(i)
                    )
                }


                base.document("Japan").collection("videos").limit(1)
                    .get()
                    .addOnSuccessListener { japanese ->
                        for (i in japanese) {
                            items.add(
                                generateVideo(i)
                            )
                        }

                        base.document("Chinese").collection("videos")
                            .limit(1).get()
                            .addOnSuccessListener { chineses ->
                                for (i in chineses) {
                                    items.add(
                                        generateVideo(i)
                                    )
                                }

                                base.document("English")
                                    .collection("videos").limit(1).get()
                                    .addOnSuccessListener { english ->
                                        for (i in english) {
                                            items.add(
                                                generateVideo(i)
                                            )
                                        }
                                    }

                            }
                    }
                mutableLiveData.postValue(items)
            }
        return mutableLiveData
    }

    private fun generateVideo(i: QueryDocumentSnapshot) = Video(
        title = i.data["title"].toString(),
        url = i.data["url"].toString(),
        thumbnail = i.data["thumbnail"].toString(),
        categoryName = i.data["category"].toString()
    )
}