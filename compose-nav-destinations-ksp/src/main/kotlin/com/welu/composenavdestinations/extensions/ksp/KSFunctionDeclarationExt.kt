package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate

val KSFunctionDeclaration.validParameters get() = parameters.filter(KSValueParameter::validate)
