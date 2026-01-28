package com.solapi.sdk.message.dto.request.kakao

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class KakaoTemplateDateQueryTest {

    @Test
    fun `secondary constructor creates query from LocalDateTime`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        
        // When
        val query = KakaoTemplateDateQuery(
            localDateTime,
            KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.EQUALS,
            ZoneOffset.UTC
        )
        
        // Then
        assertEquals(Instant.parse("2024-06-15T14:30:00Z"), query.date)
        assertEquals(KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.EQUALS, query.queryCondition)
    }

    @Test
    fun `fromLocalDateTime factory method creates query`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        
        // When
        val query = KakaoTemplateDateQuery.fromLocalDateTime(
            localDateTime,
            KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.GREATER_THEN,
            ZoneOffset.UTC
        )
        
        // Then
        assertEquals(Instant.parse("2024-06-15T14:30:00Z"), query.date)
        assertEquals(KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.GREATER_THEN, query.queryCondition)
    }

    @Test
    fun `existing Instant constructor still works - backward compatibility`() {
        // Given
        val instant = Instant.parse("2024-06-15T14:30:00Z")
        
        // When
        val query = KakaoTemplateDateQuery(
            instant,
            KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.LESS_THEN
        )
        
        // Then
        assertEquals(instant, query.date)
        assertEquals(KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.LESS_THEN, query.queryCondition)
    }

    @Test
    fun `secondary constructor uses system default timezone when not specified`() {
        // Given
        val localDateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 0)
        
        // When
        val query = KakaoTemplateDateQuery(
            localDateTime,
            KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.EQUALS
        )
        
        // Then
        val expectedJavaInstant = localDateTime
            .atZone(java.time.ZoneId.systemDefault())
            .toInstant()
        val actualJavaInstant = java.time.Instant.parse(query.date.toString())
        assertEquals(expectedJavaInstant, actualJavaInstant)
    }
}
