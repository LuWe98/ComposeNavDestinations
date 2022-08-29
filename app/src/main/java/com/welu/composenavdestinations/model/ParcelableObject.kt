package com.welu.composenavdestinations.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableObject(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable
