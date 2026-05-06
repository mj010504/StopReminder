package com.choiminjun.network.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive

// items 필드가 빈 문자열("")로 오는 경우 null 반환
@OptIn(ExperimentalSerializationApi::class)
open class EmptyStringOrObjectSerializer<T : Any>(
    private val dataSerializer: KSerializer<T>,
) : KSerializer<T?> {
    override val descriptor = dataSerializer.nullable.descriptor

    override fun serialize(encoder: Encoder, value: T?) =
        encoder.encodeNullableSerializableValue(dataSerializer, value)

    override fun deserialize(decoder: Decoder): T? {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Only JSON supported")
        val element = input.decodeJsonElement()
        if (element is JsonPrimitive && element.isString && element.content.isEmpty()) return null
        return input.json.decodeFromJsonElement(dataSerializer, element)
    }
}

// item 필드가 단일 객체 {}로 오는 경우 리스트로 감싸서 반환
open class SingleOrListSerializer<T>(
    private val serializer: KSerializer<T>,
) : KSerializer<List<T>> {
    override val descriptor = ListSerializer(serializer).descriptor

    override fun serialize(encoder: Encoder, value: List<T>) =
        encoder.encodeSerializableValue(ListSerializer(serializer), value)

    override fun deserialize(decoder: Decoder): List<T> {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Only JSON supported")
        val element = input.decodeJsonElement()
        return if (element is JsonArray) {
            input.json.decodeFromJsonElement(ListSerializer(serializer), element)
        } else {
            listOf(input.json.decodeFromJsonElement(serializer, element))
        }
    }
}

// routeNo 등 숫자/문자열 혼용 필드를 항상 String으로 반환
object AnyToStringSerializer : KSerializer<String> {
    override val descriptor = PrimitiveSerialDescriptor("AnyToString", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: String) = encoder.encodeString(value)
    override fun deserialize(decoder: Decoder): String {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Only JSON supported")
        return input.decodeJsonElement().jsonPrimitive.content
    }
}
