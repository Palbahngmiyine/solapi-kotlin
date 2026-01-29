package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * LMS 발송 E2E 테스트
 *
 * LMS는 80바이트 이상 2000바이트 미만의 장문 메시지입니다.
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 */
class LmsE2ETest : BaseE2ETest() {

    @Test
    fun `LMS 단건 발송 - 명시적 타입 지정`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - MessageType.LMS 명시적 지정
        val message = E2ETestUtils.createLmsMessage(
            from = senderNumber,
            to = testPhoneNumber,
            text = E2ETestUtils.generateLongText(100),
            subject = "LMS 제목"
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("LMS 단건 발송 (명시적 타입) 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `LMS 자동 감지 - autoTypeDetect`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 타입 지정 없이 80바이트 초과 메시지 (자동으로 LMS 변환)
        val longText = E2ETestUtils.generateLongText(100)
        val message = Message(
            from = senderNumber,
            to = testPhoneNumber,
            text = longText,
            autoTypeDetect = true
        )

        println("메시지 바이트 길이: ${longText.toByteArray(Charsets.UTF_8).size}")

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("LMS 자동 감지 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `LMS 최대 길이 테스트`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given - 약 2000바이트에 가까운 긴 메시지
        val maxLengthText = E2ETestUtils.generateMaxLengthLmsText()
        val message = Message(
            type = MessageType.LMS,
            from = senderNumber,
            to = testPhoneNumber,
            text = maxLengthText,
            subject = "LMS 최대 길이 테스트"
        )

        println("메시지 바이트 길이: ${maxLengthText.toByteArray(Charsets.UTF_8).size}")

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("LMS 최대 길이 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `LMS 발송 - 제목 포함`() {
        if (!assumeBasicEnvironmentConfigured()) return

        // Given
        val message = Message(
            type = MessageType.LMS,
            from = senderNumber,
            to = testPhoneNumber,
            text = E2ETestUtils.generateLongText(150),
            subject = "[SDK 테스트] LMS 제목입니다"
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("LMS 제목 포함 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }
}
