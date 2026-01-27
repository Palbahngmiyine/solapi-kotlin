package com.solapi.sdk.message.lib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class JsonSupportTest {

    @Serializable
    private data class InstantHolder(
        @Contextual
        val timestamp: Instant
    )

    @Serializable
    private data class NullableInstantHolder(
        @Contextual
        val timestamp: Instant? = null
    )

    @Test
    fun `Instant serializes to ISO-8601 format`() {
        // Given
        val instant = Instant.parse("2024-01-15T10:30:45.123456789Z")
        val holder = InstantHolder(instant)

        // When
        val json = JsonSupport.json.encodeToString(holder)

        // Then
        assertEquals("""{"timestamp":"2024-01-15T10:30:45.123456789Z"}""", json)
    }

    @Test
    fun `Instant deserializes from ISO-8601 format`() {
        // Given
        val json = """{"timestamp":"2024-01-15T10:30:45.123456789Z"}"""

        // When
        val holder = JsonSupport.json.decodeFromString<InstantHolder>(json)

        // Then
        val expected = Instant.parse("2024-01-15T10:30:45.123456789Z")
        assertEquals(expected, holder.timestamp)
    }

    @Test
    fun `Instant round-trip serialization preserves value`() {
        // Given
        val original = InstantHolder(Instant.parse("2024-12-31T23:59:59.999999999Z"))

        // When
        val json = JsonSupport.json.encodeToString(original)
        val restored = JsonSupport.json.decodeFromString<InstantHolder>(json)

        // Then
        assertEquals(original, restored)
    }

    @Test
    fun `null Instant serializes correctly`() {
        // Given
        val holder = NullableInstantHolder(timestamp = null)

        // When
        val json = JsonSupport.json.encodeToString(holder)

        // Then - explicitNulls = false 이므로 null 필드는 생략됨
        assertEquals("{}", json)
    }

    @Test
    fun `missing Instant field deserializes to null`() {
        // Given
        val json = "{}"

        // When
        val holder = JsonSupport.json.decodeFromString<NullableInstantHolder>(json)

        // Then
        assertNull(holder.timestamp)
    }

    @Test
    fun `Instant with zero nanoseconds serializes correctly`() {
        // Given
        val instant = Instant.parse("2024-01-15T10:30:45Z")
        val holder = InstantHolder(instant)

        // When
        val json = JsonSupport.json.encodeToString(holder)

        // Then
        assertNotNull(json)
        val restored = JsonSupport.json.decodeFromString<InstantHolder>(json)
        assertEquals(instant, restored.timestamp)
    }

    @Test
    fun `epoch Instant serializes correctly`() {
        // Given
        val epoch = Instant.fromEpochSeconds(0)
        val holder = InstantHolder(epoch)

        // When
        val json = JsonSupport.json.encodeToString(holder)
        val restored = JsonSupport.json.decodeFromString<InstantHolder>(json)

        // Then
        assertEquals(epoch, restored.timestamp)
    }
}
