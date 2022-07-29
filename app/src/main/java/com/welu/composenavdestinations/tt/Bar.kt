package com.welu.composenavdestinations.tt

import android.os.Parcel
import android.os.Parcelable

class Bar(
    val id: String,
    val name: String
): Entity {

    constructor(parcel: Parcel) : this(parcel.readString()!!, "") {
    }

    companion object CREATOR : Parcelable.Creator<Bar> {
        override fun createFromParcel(parcel: Parcel): Bar {
            return Bar(parcel)
        }

        override fun newArray(size: Int): Array<Bar?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }
}