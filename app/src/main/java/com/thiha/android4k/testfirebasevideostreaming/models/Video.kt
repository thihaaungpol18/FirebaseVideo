package com.thiha.android4k.testfirebasevideostreaming.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Video(
    val title: String,
    val url: String,
    val thumbnail: String,
    val categoryName: String
) : Parcelable {
    override fun toString(): String = title
}