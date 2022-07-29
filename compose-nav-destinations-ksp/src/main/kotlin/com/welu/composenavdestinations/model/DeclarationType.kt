package com.welu.composenavdestinations.model

sealed class DeclarationType {
    object Class: DeclarationType()
    object Function: DeclarationType()
    object Variable: DeclarationType()
    object Unknown: DeclarationType()
}

// UM potentiell zu schauen, um was es sich für eine Deklaration handelt
//            val declarationType: List<DeclarationType> = when {
//                isInsideGenericDefinition -> listOf(DeclarationType.Class)
//                currentChar == '>' -> listOf(DeclarationType.Class)
//                currentChar == '.' -> listOf(DeclarationType.Class, DeclarationType.Variable)
//                currentChar.isOneOf('(', '<' ) -> listOf(DeclarationType.Class, DeclarationType.Function)
//                else -> listOf(DeclarationType.Unknown)
//            }