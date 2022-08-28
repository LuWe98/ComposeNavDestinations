package com.welu.composenavdestinations.model

enum class CustomNavArgType(
    val suffix: String
) {
    PARCELABLE("ParcelableType"),
    PARCELABLE_ARRAY("ParcelableArrayType"),
    PARCELABLE_LIST("ParcelableListType"),
    PARCELABLE_SET("ParcelableSetType"),
    ENUM("EnumType"),
    ENUM_ARRAY("EnumArrayType"),
    ENUM_LIST("EnumListType"),
    ENUM_SET("EnumSetType")
}