package com.welu.compose_nav_destinations_ksp.mapper

import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo
import com.welu.compose_nav_destinations_ksp.model.components.rawcomponents.RawNavComponentInfo

interface NavComponentMapper<D: RawNavComponentInfo, T: NavComponentInfo> {

    fun map(component: D): T

}
