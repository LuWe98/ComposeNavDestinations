package com.welu.composenavdestinations.model.mapper

import com.welu.composenavdestinations.model.DefaultValue

data class ValueParameter(
    val name: String,
    val type: String,
    val qualifiedType: String,
    val isNullable: Boolean,
    val defaultValue: DefaultValue? = null
)