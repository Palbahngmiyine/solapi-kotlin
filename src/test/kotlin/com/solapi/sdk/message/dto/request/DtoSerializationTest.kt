package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.lib.JsonSupport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant
import kotlinx.serialization.encodeToString

class DtoSerializationTest {

    @Test
    fun `SendRequestConfig serializes scheduledDate as ISO-8601`() {
        // Given
        val config = SendRequestConfig(
            appId = "test-app",
            scheduledDate = Instant.parse("2024-06-15T14:30:00Z")
        )

        // When
        val json = JsonSupport.json.encodeToString(config)

        // Then
        assertTrue(json.contains("\"scheduledDate\":\"2024-06-15T14:30:00Z\""))
    }

    @Test
    fun `SendRequestConfig deserializes scheduledDate from ISO-8601`() {
        // Given
        val json = """{"appId":"test-app","allowDuplicates":false,"showMessageList":false,"scheduledDate":"2024-06-15T14:30:00Z"}"""

        // When
        val config = JsonSupport.json.decodeFromString<SendRequestConfig>(json)

        // Then
        assertEquals("test-app", config.appId)
        assertEquals(Instant.parse("2024-06-15T14:30:00Z"), config.scheduledDate)
    }

    @Test
    fun `SendRequestConfig handles null scheduledDate`() {
        // Given
        val config = SendRequestConfig(appId = "test-app")

        // When
        val json = JsonSupport.json.encodeToString(config)
        val restored = JsonSupport.json.decodeFromString<SendRequestConfig>(json)

        // Then
        assertNull(restored.scheduledDate)
    }

    @Test
    fun `MessageListRequest serializes date range as Instant`() {
        // Given
        val request = MessageListRequest(
            to = "01012345678",
            startDate = Instant.parse("2024-01-01T00:00:00Z"),
            endDate = Instant.parse("2024-01-31T23:59:59Z")
        )

        // When
        val json = JsonSupport.json.encodeToString(request)

        // Then
        assertTrue(json.contains("\"startDate\":\"2024-01-01T00:00:00Z\""))
        assertTrue(json.contains("\"endDate\":\"2024-01-31T23:59:59Z\""))
    }

    @Test
    fun `MessageListRequest deserializes date range from Instant`() {
        // Given
        val json = """{"to":"01012345678","startDate":"2024-01-01T00:00:00Z","endDate":"2024-01-31T23:59:59Z"}"""

        // When
        val request = JsonSupport.json.decodeFromString<MessageListRequest>(json)

        // Then
        assertEquals(Instant.parse("2024-01-01T00:00:00Z"), request.startDate)
        assertEquals(Instant.parse("2024-01-31T23:59:59Z"), request.endDate)
    }

    @Test
    fun `MessageListBaseRequest serializes date range as Instant`() {
        // Given
        val request = MessageListBaseRequest(
            to = "01012345678",
            startDate = Instant.parse("2024-02-01T00:00:00Z"),
            endDate = Instant.parse("2024-02-28T23:59:59Z")
        )

        // When
        val json = JsonSupport.json.encodeToString(request)

        // Then
        assertTrue(json.contains("\"startDate\":\"2024-02-01T00:00:00Z\""))
        assertTrue(json.contains("\"endDate\":\"2024-02-28T23:59:59Z\""))
    }

    @Test
    fun `MessageListBaseRequest deserializes date range from Instant`() {
        // Given
        val json = """{"to":"01012345678","startDate":"2024-02-01T00:00:00Z","endDate":"2024-02-28T23:59:59Z"}"""

        // When
        val request = JsonSupport.json.decodeFromString<MessageListBaseRequest>(json)

        // Then
        assertEquals(Instant.parse("2024-02-01T00:00:00Z"), request.startDate)
        assertEquals(Instant.parse("2024-02-28T23:59:59Z"), request.endDate)
    }

    @Test
    fun `MultipleDetailMessageSendingRequest serializes scheduledDate`() {
        // Given
        val request = MultipleDetailMessageSendingRequest(
            messages = emptyList(),
            scheduledDate = Instant.parse("2024-03-15T09:00:00Z")
        )

        // When
        val json = JsonSupport.json.encodeToString(request)

        // Then
        assertTrue(json.contains("\"scheduledDate\":\"2024-03-15T09:00:00Z\""))
    }

    @Test
    fun `MultipleDetailMessageSendingRequest round-trip preserves scheduledDate`() {
        // Given
        val original = MultipleDetailMessageSendingRequest(
            messages = emptyList(),
            scheduledDate = Instant.parse("2024-03-15T09:00:00Z"),
            showMessageList = true
        )

        // When
        val json = JsonSupport.json.encodeToString(original)
        val restored = JsonSupport.json.decodeFromString<MultipleDetailMessageSendingRequest>(json)

        // Then
        assertEquals(original.scheduledDate, restored.scheduledDate)
        assertEquals(original.showMessageList, restored.showMessageList)
    }

    @Test
    fun `Instant with nanoseconds precision is preserved`() {
        // Given
        val preciseInstant = Instant.parse("2024-06-15T14:30:00.123456789Z")
        val config = SendRequestConfig(scheduledDate = preciseInstant)

        // When
        val json = JsonSupport.json.encodeToString(config)
        val restored = JsonSupport.json.decodeFromString<SendRequestConfig>(json)

        // Then
        assertEquals(preciseInstant, restored.scheduledDate)
    }
}
