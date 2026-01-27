package com.solapi.sdk.message.lib

import kotlin.time.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * 중앙 집중식 Json 및 직렬화 설정.
 * Instant 는 ISO-8601(Z) 문자열로 직렬화/역직렬화합니다.
 */
object JsonSupport {
    private object InstantIsoSerializer : KSerializer<Instant> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Instant) {
            encoder.encodeString(value.toString())
        }

        override fun deserialize(decoder: Decoder): Instant {
            val text = decoder.decodeString()
            return Instant.parse(text)
        }
    }

    val serializersModule: SerializersModule = SerializersModule {
        contextual(Instant::class, InstantIsoSerializer)
    }

    val json: Json = Json {
        coerceInputValues = true
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
        serializersModule = JsonSupport.serializersModule
    }
}


