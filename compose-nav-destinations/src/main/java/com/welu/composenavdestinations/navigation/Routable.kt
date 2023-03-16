package com.welu.composenavdestinations.navigation

interface Routable {
    val parameterizedRoute: String

    companion object {
        fun of(parameterizedRoute: String) = object : Routable{
            override val parameterizedRoute: String = parameterizedRoute
        }
    }
}