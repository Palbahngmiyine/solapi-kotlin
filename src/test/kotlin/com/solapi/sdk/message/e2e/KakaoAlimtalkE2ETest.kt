package com.solapi.sdk.message.e2e

import com.solapi.sdk.message.e2e.base.BaseE2ETest
import com.solapi.sdk.message.e2e.lib.E2ETestUtils
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageType
import com.solapi.sdk.message.model.kakao.KakaoOption
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * 카카오 알림톡 (ATA) E2E 테스트
 *
 * 알림톡은 카카오톡으로 발송되는 정보성 메시지입니다.
 * 사전에 등록된 템플릿을 사용해야 합니다.
 *
 * 환경변수 설정 필요:
 * - SOLAPI_API_KEY: SOLAPI API 키
 * - SOLAPI_API_SECRET: SOLAPI API 시크릿
 * - SOLAPI_SENDER: 등록된 발신번호
 * - SOLAPI_RECIPIENT: 테스트 수신번호
 * - SOLAPI_KAKAO_PF_ID: 카카오 비즈니스 채널 ID
 * - SOLAPI_KAKAO_TEMPLATE_ID: 카카오 알림톡 템플릿 ID
 */
class KakaoAlimtalkE2ETest : BaseE2ETest() {

    @Test
    fun `알림톡 발송 - 기본 템플릿`() {
        if (!assumeKakaoTemplateConfigured()) return

        // Given
        val message = E2ETestUtils.createAlimtalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            pfId = pfId!!,
            templateId = templateId!!
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("알림톡 기본 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `알림톡 발송 - 변수 치환`() {
        if (!assumeKakaoTemplateConfigured()) return

        // Given - 템플릿에 맞는 변수 설정 (실제 템플릿에 따라 변수명 조정 필요)
        val variables = mapOf(
            "name" to "테스트 사용자",
            "code" to "123456"
        )

        val message = E2ETestUtils.createAlimtalkMessage(
            from = senderNumber,
            to = testPhoneNumber,
            pfId = pfId!!,
            templateId = templateId!!,
            variables = variables
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("알림톡 변수 치환 발송 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `알림톡 발송 - 잘못된 템플릿 ID`() {
        if (!assumeKakaoEnvironmentConfigured()) return

        // Given - 존재하지 않는 템플릿 ID
        val message = Message(
            type = MessageType.ATA,
            from = senderNumber,
            to = testPhoneNumber,
            kakaoOptions = KakaoOption(
                pfId = pfId,
                templateId = "invalid-template-id-12345"
            )
        )

        // When & Then
        var errorOccurred = false
        try {
            messageService!!.send(message)
        } catch (e: Exception) {
            errorOccurred = true
            printExceptionDetails(e)
        }

        assertTrue(errorOccurred, "잘못된 템플릿 ID로 알림톡 발송 시 에러가 발생해야 함")
    }

    @Test
    fun `알림톡 대체 발송 비활성화`() {
        if (!assumeKakaoTemplateConfigured()) return

        // Given - disableSms = true
        val message = E2ETestUtils.createAlimtalkMessageWithoutFallback(
            from = senderNumber,
            to = testPhoneNumber,
            pfId = pfId!!,
            templateId = templateId!!
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("알림톡 대체 발송 비활성화 성공 - groupId: ${response.groupInfo?.groupId}")
    }

    @Test
    fun `알림톡 발송 - 변수 자동 포맷팅`() {
        if (!assumeKakaoTemplateConfigured()) return

        // Given - #{} 없이 변수 키 지정 (KakaoOption에서 자동 포맷팅)
        val variables = mapOf(
            "name" to "자동포맷테스트",
            "#{code}" to "999999" // 이미 포맷된 키도 허용
        )

        val message = Message(
            type = MessageType.ATA,
            from = senderNumber,
            to = testPhoneNumber,
            kakaoOptions = KakaoOption(
                pfId = pfId,
                templateId = templateId,
                variables = variables
            )
        )

        // When
        val response = messageService!!.send(message)

        // Then
        assertNotNull(response)
        assertNotNull(response.groupInfo?.groupId)
        println("알림톡 변수 자동 포맷팅 성공 - groupId: ${response.groupInfo?.groupId}")
    }
}
