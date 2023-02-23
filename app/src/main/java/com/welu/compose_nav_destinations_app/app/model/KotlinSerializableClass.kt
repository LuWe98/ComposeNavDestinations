package com.welu.compose_nav_destinations_app.app.model

import kotlinx.serialization.Serializable

@Serializable
data class KotlinSerializableClass(
    val age: Int,
    val enumTest: TestEnum
)