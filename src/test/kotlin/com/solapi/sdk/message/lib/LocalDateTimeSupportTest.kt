package com.solapi.sdk.message.lib

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class LocalDateTimeSupportTest {

    @Test
    fun `toKotlinInstant converts LocalDateTime with system default timezone`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        
        // When
        val instant = localDateTime.toKotlinInstant()
        
        // Then
        val expectedJavaInstant = localDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
        val actualJavaInstant = java.time.Instant.parse(instant.toString())
        assertEquals(expectedJavaInstant, actualJavaInstant)
    }

    @Test
    fun `toKotlinInstant converts LocalDateTime with explicit UTC timezone`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        val utcZone = ZoneOffset.UTC
        
        // When
        val instant = localDateTime.toKotlinInstant(utcZone)
        
        // Then
        val expectedInstant = Instant.parse("2024-06-15T14:30:00Z")
        assertEquals(expectedInstant, instant)
    }

    @Test
    fun `toKotlinInstant converts LocalDateTime with Asia Seoul timezone`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        val seoulZone = ZoneId.of("Asia/Seoul")
        
        // When
        val instant = localDateTime.toKotlinInstant(seoulZone)
        
        // Then
        val expectedInstant = Instant.parse("2024-06-15T05:30:00Z")
        assertEquals(expectedInstant, instant)
    }

    @Test
    fun `LocalDateTimeSupport static method works for Java interop`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        val utcZone = ZoneOffset.UTC
        
        // When
        val instant = LocalDateTimeSupport.toKotlinInstant(localDateTime, utcZone)
        
        // Then
        val expectedInstant = Instant.parse("2024-06-15T14:30:00Z")
        assertEquals(expectedInstant, instant)
    }

    @Test
    fun `toKotlinInstant preserves nanosecond precision`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0, 123456789)
        val utcZone = ZoneOffset.UTC
        
        // When
        val instant = localDateTime.toKotlinInstant(utcZone)
        
        // Then
        val expectedInstant = Instant.parse("2024-06-15T14:30:00.123456789Z")
        assertEquals(expectedInstant, instant)
    }
}
