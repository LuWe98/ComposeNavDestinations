package com.welu.composenavdestinations.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
@Suppress("UNCHECKED_CAST")
internal val <T : Any> KClass<T>.ktxSerializer get(): KSerializer<T> = serializer(java) as KSerializer<T>

internal fun <T : Any> KClass<T>.serialize(value: T): String = ktxSerializer.serialize(value)

internal fun <T : Any> KSerializer<T>.serialize(value: T): String = Json.encodeToString(this, value).toBase64()

internal fun <T : Any> KClass<T>.deserialize(value: String): T = ktxSerializer.deserialize(value)

internal fun <T : Any> KSerializer<T>.deserialize(value: String): T = Json.decodeFromString(this, value.fromBase64())

//@Suppress("UNCHECKED_CAST")
//internal val <T : Any> KClass<T>.ktxSerializerCustom
//    get(): KSerializer<T>? {
//        val objInstance = objectInstance ?: companionObjectInstance ?: return null
//        return objInstance::class.serializerMethod?.call(objInstance) as KSerializer<T>?
//    }
//
//internal val <T : Any> KClass<T>.serializerMethod
//    get(): KCallable<*>? = members.firstOrNull {
//        it.name == "serializer" && it.parameters.size == 1 && it.returnType.classifier?.starProjectedType == KSerializer::class.starProjectedType
//    }