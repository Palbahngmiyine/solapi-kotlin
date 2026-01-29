package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import com.solapi.sdk.message.model.Message
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * SMS 발송 E2E 테스트
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class SmsE2ETest : BaseE2ETest() {

    @Test
    fun `SMS 단건 발송 - 기본`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val message = E2ETestUtils.createSmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = "[SDK 테스트] SMS 단건 발송 테스트입니다."
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("SMS 단건 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `SMS 단건 발송 - 대시 포함 전화번호`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 대시가 포함된 전화번호 (Message init에서 자동 제거)
        val message = Message(
            from = senderNumber.chunked(3).joinToString("-").let {
                if (senderNumber.length == 11) "${senderNumber.substring(0, 3)}-${senderNumber.substring(3, 7)}-${senderNumber.substring(7)}"
                else senderNumber
            },
            to = testPhoneNumber.let {
                if (it.length == 11) "${it.substring(0, 3)}-${it.substring(3, 7)}-${it.substring(7)}"
                else it
            },
            text = "[SDK 테스트] 대시 포함 전화번호 테스트입니다."
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("SMS 발송 (대시 포함 전화번호) 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `SMS 배치 발송 - 다중 수신자`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 동일 수신자에게 여러 메시지 (테스트 환경에서는 동일 번호 사용)
        val messages = E2ETestUtils.createBatchSmsMessages(
            from = senderNumber,
            toList = listOf(testPhoneNumber, testPhoneNumber),
            textPrefix = "[SDK 테스트] 배치 SMS"
        )

        // When
        val response = messageService!!.send(messages)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("SMS 배치 발송 성공 - groupId: ${response.groupInfo?.groupId}, count: ${response.groupInfo?.count}")
    }

    @Test
    fun `SMS 발송 - 잘못된 발신번호`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 등록되지 않은 발신번호
        val message = E2ETestUtils.createSmsMessage(
            from = "00000000000",
            to = testPhoneNumber,
            text = "[SDK 테스트] 잘못된 발신번호 테스트"
        )

        // When & Then
        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "등록되지 않은 발신번호로 발송 시 에러가 발생해야 함")
    }

    @Test
    fun `SMS 발송 - 빈 메시지`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 빈 텍스트
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = ""
        )

        // When & Then
        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "빈 메시지 발송 시 에러가 발생해야 함")
    }
}
