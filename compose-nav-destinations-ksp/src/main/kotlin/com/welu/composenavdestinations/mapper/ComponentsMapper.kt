package com.welu.composenavdestinations.mapper

interface ComponentsMapper<D, T> {

    fun map(component: D): T

}
