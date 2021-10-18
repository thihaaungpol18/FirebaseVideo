package com.thiha.android4k.testfirebasevideostreaming.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Collection(
    val name: String,
    val thumbnail: String,
) : Parcelable {
    override fun toString(): String = name

}