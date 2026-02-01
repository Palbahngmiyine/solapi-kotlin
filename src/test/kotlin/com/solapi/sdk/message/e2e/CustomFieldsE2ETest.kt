package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Custom Fields E2E 테스트
 *
 * Custom Fields는 메시지에 사용자 정의 데이터를 추가할 수 있는 기능입니다.
 * 발송 후 메시지 조회 시에도 해당 필드가 포함됩니다.
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class CustomFieldsE2ETest : BaseE2ETest() {

    @Test
    fun `Custom Fields 포함 발송`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val customFields = mutableMapOf(
            "orderId" to "ORD-12345",
            "userId" to "USER-67890",
            "category" to "notification"
        )

        val message = E2ETestUtils.createMessageWithCustomFields(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] Custom Fields 테스트입니다.",
            customFields = customFields
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("Custom Fields 포함 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  customFields: $customFields")
    }

    @Test
    fun `Custom Fields 다양한 값`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 특수문자, 유니코드 포함
        val customFields = mutableMapOf(
            "key_with_underscore" to "value1",
            "한글키" to "한글값",
            "emoji" to "🚀🎉",
            "special" to "!@#\$%^&*()",
            "number" to "12345",
            "empty" to ""
        )

        val message = E2ETestUtils.createMessageWithCustomFields(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] Custom Fields 다양한 값 테스트",
            customFields = customFields
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("Custom Fields 다양한 값 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `Custom Fields 단일 필드`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 하나의 커스텀 필드만 사용
        val customFields = mutableMapOf(
            "trackingId" to "TRK-${System.currentTimeMillis()}"
        )

        val message = E2ETestUtils.createMessageWithCustomFields(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] Custom Fields 단일 필드 테스트",
            customFields = customFields
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("Custom Fields 단일 필드 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  trackingId: ${customFields["trackingId"]}")
    }

    @Test
    fun `Custom Fields 긴 값`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 긴 문자열 값
        val longValue = "A".repeat(200)
        val customFields = mutableMapOf(
            "longField" to longValue
        )

        val message = E2ETestUtils.createMessageWithCustomFields(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] Custom Fields 긴 값 테스트",
            customFields = customFields
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("Custom Fields 긴 값 발송 성공 - groupId: ${response.groupInfo?.groupId}")
        println("  longField 길이: ${longValue.length}")
    }
}
