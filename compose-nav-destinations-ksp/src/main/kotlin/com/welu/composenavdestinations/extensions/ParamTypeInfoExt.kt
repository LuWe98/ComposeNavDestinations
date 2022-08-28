package com.welu.composenavdestinations.extensions

import com.welu.composenavdestinations.model.ImportInfo
import com.welu.composenavdestinations.model.ParameterType
import com.welu.composenavdestinations.model.ParameterTypeArgument
import com.welu.composenavdestinations.model.ParameterTypeInfo
import java.io.Serializable
import kotlin.reflect.KClass

fun KClass<*>.asParameterTypeInfo(
    vararg typeArgs: ParameterTypeInfo,
    isNullable: Boolean = false,
    isSerializable: Boolean = Serializable::class.java.isAssignableFrom(javaObjectType)
) = ParameterTypeInfo(
    isNullable = isNullable,
    type = ParameterType(
        import = ImportInfo(qualifiedName!!),
        typeArguments = typeArgs.map(ParameterTypeArgument::Typed),
        isEnum = java.isEnum,
        isSerializable = isSerializable
    )
)

fun KClass<*>.asParamTypeInfo(vararg typeArgs: KClass<*>): ParameterTypeInfo = asBasicParameterTypeInfo(*typeArgs.map(KClass<*>::asParamTypeInfo).toTypedArray())

fun KClass<*>.asBasicParameterTypeInfo(vararg typeArgs: ParameterTypeInfo): ParameterTypeInfo = ParameterTypeInfo(
    ParameterType(
        import = ImportInfo(qualifiedName!!),
        typeArguments = typeArgs.map(ParameterTypeArgument::Typed),
    )
)

fun ImportInfo.asParamTypeInfo(vararg typeArgs: KClass<*>): ParameterTypeInfo = asBasicParameterTypeInfo(*typeArgs.map(KClass<*>::asParamTypeInfo).toTypedArray())

fun ImportInfo.asBasicParameterTypeInfo(vararg typeArgs: ParameterTypeInfo): ParameterTypeInfo = ParameterTypeInfo(
    ParameterType(
        import = this,
        typeArguments = typeArgs.map(ParameterTypeArgument::Typed),
    )
)