package com.solapi.sdk.message.lib

import com.solapi.sdk.message.exception.SolapiApiKeyException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AuthenticatorTest {

    @Test
    fun `generateAuthInfo returns HMAC-SHA256 format`() {
        // Given
        val authenticator = Authenticator("test-api-key", "test-api-secret")

        // When
        val authInfo = authenticator.generateAuthInfo()

        // Then
        assertTrue(authInfo.startsWith("HMAC-SHA256 "))
        assertTrue(authInfo.contains("Apikey=test-api-key"))
        assertTrue(authInfo.contains("Date="))
        assertTrue(authInfo.contains("salt="))
        assertTrue(authInfo.contains("signature="))
    }

    @Test
    fun `generateAuthInfo includes UTC timestamp`() {
        // Given
        val authenticator = Authenticator("test-api-key", "test-api-secret")

        // When
        val authInfo = authenticator.generateAuthInfo()

        // Then - UTC ISO-8601 형식의 타임스탬프가 포함되어야 함
        val dateMatch = Regex("Date=([^,]+)").find(authInfo)
        assertNotNull(dateMatch)

        val dateValue = dateMatch.groupValues[1]
        // kotlin.time.Instant.toString() 형식: "2024-01-15T10:30:45.123456789Z"
        assertTrue(dateValue.endsWith("Z"), "Timestamp should be in UTC format (ends with Z)")
        assertTrue(dateValue.contains("T"), "Timestamp should contain 'T' separator")
    }

    @Test
    fun `generateAuthInfo includes 32-character salt`() {
        // Given
        val authenticator = Authenticator("test-api-key", "test-api-secret")

        // When
        val authInfo = authenticator.generateAuthInfo()

        // Then
        val saltMatch = Regex("salt=([a-f0-9]+)").find(authInfo)
        assertNotNull(saltMatch)
        assertEquals(32, saltMatch.groupValues[1].length, "Salt should be 32 hex characters (UUID without dashes)")
    }

    @Test
    fun `generateAuthInfo includes 64-character signature`() {
        // Given
        val authenticator = Authenticator("test-api-key", "test-api-secret")

        // When
        val authInfo = authenticator.generateAuthInfo()

        // Then - HMAC-SHA256 produces 64 hex characters
        val signatureMatch = Regex("signature=([a-f0-9]+)").find(authInfo)
        assertNotNull(signatureMatch)
        assertEquals(64, signatureMatch.groupValues[1].length, "Signature should be 64 hex characters (SHA-256)")
    }

    @Test
    fun `generateAuthInfo throws exception for empty apiKey`() {
        // Given
        val authenticator = Authenticator("", "test-api-secret")

        // When & Then
        assertFailsWith<SolapiApiKeyException> {
            authenticator.generateAuthInfo()
        }
    }

    @Test
    fun `generateAuthInfo throws exception for empty apiSecretKey`() {
        // Given
        val authenticator = Authenticator("test-api-key", "")

        // When & Then
        assertFailsWith<SolapiApiKeyException> {
            authenticator.generateAuthInfo()
        }
    }

    @Test
    fun `generateAuthInfo throws exception for both empty keys`() {
        // Given
        val authenticator = Authenticator("", "")

        // When & Then
        assertFailsWith<SolapiApiKeyException> {
            authenticator.generateAuthInfo()
        }
    }

    @Test
    fun `generateAuthInfo produces different signatures for different calls`() {
        // Given
        val authenticator = Authenticator("test-api-key", "test-api-secret")

        // When
        val authInfo1 = authenticator.generateAuthInfo()
        val authInfo2 = authenticator.generateAuthInfo()

        // Then - salt가 다르므로 signature도 달라야 함
        val signature1 = Regex("signature=([a-f0-9]+)").find(authInfo1)?.groupValues?.get(1)
        val signature2 = Regex("signature=([a-f0-9]+)").find(authInfo2)?.groupValues?.get(1)

        assertNotEquals(signature1, signature2, "Each call should produce different signature due to unique salt")
    }

    @Test
    fun `generateAuthInfo produces consistent format`() {
        // Given
        val authenticator = Authenticator("my-key", "my-secret")

        // When
        val authInfo = authenticator.generateAuthInfo()

        // Then - 정확한 형식 검증
        val pattern = Regex("^HMAC-SHA256 Apikey=my-key, Date=[^,]+, salt=[a-f0-9]{32}, signature=[a-f0-9]{64}$")
        assertTrue(pattern.matches(authInfo), "Auth info format should match expected pattern")
    }
}
