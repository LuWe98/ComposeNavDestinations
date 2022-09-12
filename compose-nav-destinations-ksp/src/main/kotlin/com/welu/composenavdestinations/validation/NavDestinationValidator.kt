package com.welu.composenavdestinations.validation

import com.welu.composenavdestinations.exceptions.DuplicateDestinationException
import com.welu.composenavdestinations.exceptions.DuplicateRouteException
import com.welu.composenavdestinations.model.NavDestinationInfo

object NavDestinationValidator {

    fun validate(destinations: Sequence<NavDestinationInfo>) {
        val areDuplicateNamesPresent = destinations.groupBy(NavDestinationInfo::simpleName).any { it.value.size > 1 }
        if(areDuplicateNamesPresent) {
            throw DuplicateDestinationException
        }

        val areDuplicateRoutesPresent = destinations.groupBy(NavDestinationInfo::route).any { it.value.size > 1 }
        if(areDuplicateRoutesPresent) {
            throw DuplicateRouteException
        }

    }

}