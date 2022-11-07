package com.welu.composenavdestinations.model

import kotlinx.serialization.Serializable

@Serializable
data class KotlinSerializableClass(
    val age: Int,
    val enumTest: TestEnum
)