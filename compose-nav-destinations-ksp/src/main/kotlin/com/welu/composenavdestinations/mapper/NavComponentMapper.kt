package com.welu.composenavdestinations.mapper

import com.welu.composenavdestinations.model.components.NavComponentInfo
import com.welu.composenavdestinations.model.components.rawcomponents.RawNavComponentInfo

interface NavComponentMapper<D: RawNavComponentInfo, T: NavComponentInfo> {

    fun map(component: D): T

}
