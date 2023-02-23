package com.welu.compose_nav_destinations_app.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableObject(
    val id: String,
    val name: String,
    val age: Int
) : Parcelable
