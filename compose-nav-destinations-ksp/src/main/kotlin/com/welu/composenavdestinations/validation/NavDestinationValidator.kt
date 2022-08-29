package com.welu.composenavdestinations.validation

import com.welu.composenavdestinations.exceptions.DuplicateDestinationException
import com.welu.composenavdestinations.exceptions.DuplicateRouteException
import com.welu.composenavdestinations.model.NavDestinationInfo

object NavDestinationValidator {

    fun validate(infos: Sequence<NavDestinationInfo>) {
        val areDuplicateNamesPresent = infos.groupBy(NavDestinationInfo::name).any { it.value.size > 1 }
        if(areDuplicateNamesPresent) {
            throw DuplicateDestinationException
        }

        val areDuplicateRoutesPresent = infos.groupBy(NavDestinationInfo::route).any { it.value.size > 1 }
        if(areDuplicateRoutesPresent) {
            throw DuplicateRouteException
        }

    }

}